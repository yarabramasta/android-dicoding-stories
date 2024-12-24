package com.dicoding.stories.features.stories.presentation.viewmodel.create

import android.net.Uri
import androidx.compose.runtime.Immutable
import com.dicoding.stories.R
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.UiText

@Immutable
data class CreateStoryState(
  val status: UiStatus,
  val image: Uri?,
  val imageError: UiText?,
  val description: String,
  val descriptionError: UiText?,
) {
  companion object {
    fun initial() = CreateStoryState(
      status = UiStatus.Idle,
      image = Uri.EMPTY,
      imageError = null,
      description = "",
      descriptionError = null,
    )
  }

  fun validateImage(uri: Uri?): UiText? {
    return if ((uri?.toString() ?: "").isEmpty()) {
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
