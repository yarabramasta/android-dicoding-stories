package com.dicoding.stories.shared.ui.lib

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
  data class DynamicString(val value: String) : UiText

  class StringResource(
    @StringRes val resId: Int,
    vararg val args: Any,
  ) : UiText

  @Composable
  fun asString(): String {
    return when (this) {
      is DynamicString -> value
      is StringResource -> stringResource(resId, *args)
    }
  }

//  fun asString(context: Context): String {
//    return when (this) {
//      is DynamicString -> value
//      is StringResource -> context.getString(resId, *args)
//    }
//  }

//  companion object {
//    @Composable
//    fun fromStringResource(@StringRes resId: Int, vararg args: Any): String {
//      return StringResource(resId, *args).asString()
//    }

//    fun fromStringResource(context: Context, @StringRes resId: Int, vararg args: Any): String {
//      return StringResource(resId, *args).asString(context)
//    }
//  }
}