package com.dicoding.stories.features.stories.presentation.viewmodel.create

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.Immutable
import com.dicoding.stories.R
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.UiText
import com.google.android.gms.maps.model.LatLng

@Immutable
data class CreateStoryState(
  val status: UiStatus,
  val imageUri: Uri?,
  val imageError: UiText?,
  val imageBitmap: Bitmap?,
  val description: String,
  val descriptionError: UiText?,
  val latLng: LatLng?,
) {
  companion object {
    fun initial() = CreateStoryState(
      status = UiStatus.Idle,
      imageUri = Uri.EMPTY,
      imageError = null,
      imageBitmap = null,
      description = "",
      descriptionError = null,
      latLng = null
    )
  }

  fun validateImage(uri: Uri?, bitmap: Bitmap?): UiText? {
    return if ((uri?.toString() ?: "").isEmpty() && bitmap == null) {
      UiText.StringResource(R.string.err_form_image_required)
    } else {
      null
    }
  }

  fun validateDescription(description: String): UiText? {
    return if (description.isEmpty()) {
      UiText.StringResource(R.string.err_form_description_empty)
    } else {
      null
    }
  }
}
