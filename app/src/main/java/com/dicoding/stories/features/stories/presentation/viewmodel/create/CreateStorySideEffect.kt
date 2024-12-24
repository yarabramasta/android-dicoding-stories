package com.dicoding.stories.features.stories.presentation.viewmodel.create

sealed class CreateStorySideEffect {
  data object OnSuccessNavigateBack : CreateStorySideEffect()
  data class ShowMessage(val message: String) : CreateStorySideEffect()
}
