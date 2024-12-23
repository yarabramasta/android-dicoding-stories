package com.dicoding.stories.shared.ui.lib

import android.app.Activity
import android.app.LocaleManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

fun setLocale(context: Context, localeTag: String) {
  context.findActivity()?.runOnUiThread {
    Locale.setDefault(Locale.forLanguageTag(localeTag))

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      context
        .getSystemService(LocaleManager::class.java)
        .applicationLocales = LocaleList.forLanguageTags(localeTag)
    } else {
      AppCompatDelegate.setApplicationLocales(
        LocaleListCompat.forLanguageTags(localeTag)
      )
    }

    context.startActivity(
      Intent.makeRestartActivityTask((context as Activity).intent?.component)
    )
  }
}
