package com.dicoding.stories.features.stories.presentation.viewmodel.create

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.stories.R
import com.dicoding.stories.features.stories.domain.business.CreateStoryUseCase
import com.dicoding.stories.shared.ui.lib.ImageUtils
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.UiText
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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

  fun onImageUriChanged(uri: Uri?, bitmap: Bitmap?) {
    intent {
      reduce {
        state.copy(
          imageUri = uri,
          imageBitmap = bitmap,
          imageError = state.validateImage(uri, bitmap)
        )
      }
    }
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

  fun onLatLngChanged(latLng: LatLng?) {
    intent {
      reduce { state.copy(latLng = latLng) }
    }
  }

  fun onClear() {
    intent {
      reduce { CreateStoryState.initial() }
    }
  }

  private fun validate(): Boolean {
    var imageError: UiText? = null
    var descError: UiText? = null

    intent {
      imageError = state.validateImage(state.imageUri, state.imageBitmap)
      descError = state.validateDescription(state.description)

      reduce {
        state.copy(
          status = UiStatus.Idle,
          imageError = imageError,
          descriptionError = descError
        )
      }
    }

    return imageError == null && descError == null
  }

  fun onSubmit(context: Context, onSuccess: () -> Unit = {}) {
    intent {
      container.scope.launch {
        if (validate()) {
          val imageBitmap = state.imageBitmap
          val imageUri = state.imageUri

          if (imageBitmap != null && imageUri != null) {
            reduce { state.copy(status = UiStatus.Loading) }
            val file = ImageUtils.processImage(context, imageBitmap, imageUri)

            createStoryUseCase(
              CreateStoryUseCase.Params(
                image = file,
                description = state.description,
                lat = state.latLng?.latitude,
                lon = state.latLng?.longitude
              )
            )
              .fold(
                onSuccess = {
                  reduce {
                    CreateStoryState.initial().copy(status = UiStatus.Success)
                  }
                  onSuccess()
                  postSideEffect(CreateStorySideEffect.OnSuccessNavigateBack)
                  postSideEffect(
                    CreateStorySideEffect.ShowMessage(
                      context.getString(R.string.story_created_successfully)
                    )
                  )
                },
                onFailure = {
                  val message: String = when (it.message) {
                    "FileTooLarge" -> context.getString(R.string.err_form_image_too_large)
                    "Unauthorized" -> context.getString(R.string.err_unauthorized)
                    else -> context.getString(R.string.err_general_trouble)
                  }
                  reduce {
                    state.copy(
                      status = UiStatus.Failure(message),
                      imageError = UiText.StringResource(R.string.err_form_image_too_large)
                    )
                  }
                  postSideEffect(CreateStorySideEffect.ShowMessage(message))
                }
              )
          }
        }
      }
    }
  }
}

