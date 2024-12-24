package com.dicoding.stories.features.stories.presentation.viewmodel.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.R
import com.dicoding.stories.features.stories.domain.business.CreateStoryUseCase
import com.dicoding.stories.shared.lib.utils.validateFileSize
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class CreateStoryViewModel @Inject constructor(
  private val createStoryUseCase: CreateStoryUseCase,
) : ViewModel(),
  ContainerHost<CreateStoryState, CreateStorySideEffect> {

  override val container =
    viewModelScope.container<CreateStoryState, CreateStorySideEffect>(
      CreateStoryState.initial()
    )

  fun onImageUriChanged(uri: Uri?) {
    intent {
      reduce { state.copy(image = uri) }
      validateImageFull(uri)
    }
  }

  private fun validateImageFull(uri: Uri?): UiText? {
    var error: UiText? = null

    intent {
      withContext(Dispatchers.Main) {
        error = state.validateImage(uri)
        val stringUri = uri?.toString() ?: ""
        if (stringUri.isNotEmpty()) {
          if (!validateFileSize(stringUri, 1)) {
            error = UiText.StringResource(R.string.err_form_image_too_large)
          }
        }
        reduce { state.copy(imageError = error) }
      }
    }

    return error
  }

  fun onDescriptionChanged(text: String) {
    intent {
      reduce {
        state.copy(
          description = text,
          descriptionError = state.validateDescription(text)
        )
      }
    }
  }

  fun onClear() {
    intent {
      reduce { CreateStoryState.initial() }
    }
  }

  fun onSubmit() {
    intent {
      reduce { state.copy(status = UiStatus.Loading) }

      val isValid = withContext(Dispatchers.Main) {
        reduce {
          state.copy(
            descriptionError = state.validateDescription(state.description)
          )
        }
        return@withContext validateImageFull(state.image) == null
          && state.descriptionError == null
      }

      if (isValid) {
        container.scope.launch {
          createStoryUseCase(
            CreateStoryUseCase.Params(
              image = (state.image ?: Uri.EMPTY).toString(),
              description = state.description
            )
          )
            .fold(
              onSuccess = {
                reduce { CreateStoryState.initial() }
                postSideEffect(CreateStorySideEffect.OnSuccessNavigateBack)
                postSideEffect(
                  CreateStorySideEffect.ShowMessage(
                    "Story created successfully!"
                  )
                )
              },
              onFailure = {
                postSideEffect(
                  CreateStorySideEffect.ShowMessage(
                    it.message ?: "Uh oh! Something went wrong..."
                  )
                )
              }
            )
        }
      }
    }
  }
}
