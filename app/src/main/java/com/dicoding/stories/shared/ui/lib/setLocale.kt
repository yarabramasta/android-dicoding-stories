package com.dicoding.stories.shared.ui.lib

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

fun setLocale(context: Context, localeTag: String) {
  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    context
      .getSystemService(LocaleManager::class.java)
      .applicationLocales = LocaleList.forLanguageTags(localeTag)
  } else {
    AppCompatDelegate.setApplicationLocales(
      LocaleListCompat.forLanguageTags(localeTag)
    )
  }

  context.findActivity()?.recreate()
}
