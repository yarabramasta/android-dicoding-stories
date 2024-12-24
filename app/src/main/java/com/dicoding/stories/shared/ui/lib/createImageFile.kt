package com.dicoding.stories.shared.ui.lib

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.createImageFile(): File {
  val currentLocale = AppCompatDelegate.getApplicationLocales().toLanguageTags()
  val timeStamp = SimpleDateFormat(
    "yyyyMMdd_HHmmss",
    Locale(currentLocale)
  ).format(Date())
  val imageFileName = "JPEG_${timeStamp}_"

  return File.createTempFile(
    imageFileName,
    ".jpg",
    externalCacheDir
  )
}
