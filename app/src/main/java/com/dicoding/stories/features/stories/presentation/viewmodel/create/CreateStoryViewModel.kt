package com.dicoding.stories.features.stories.presentation.viewmodel.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.R
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.shared.lib.utils.validateFileSize
import com.dicoding.stories.shared.ui.lib.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.container
import javax.inject.Inject

@HiltViewModel
class CreateStoryViewModel @Inject constructor() : ViewModel(),
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

  fun onSubmit(onSuccess: (Story) -> Unit = {}) {
    intent {
      withContext(Dispatchers.Main) {
        val isValid = validateImageFull(state.image) == null
        if (isValid) onSuccess(Story.dummy())
        reduce { CreateStoryState.initial() }
      }
    }
  }
}
