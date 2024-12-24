package com.dicoding.stories.shared.ui.lib

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun Context.createImageFile(): File {
  val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
  val filename = "story_${timeStamp}_"
  val storageDir: File? = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
  return File.createTempFile(filename, ".jpg", storageDir)
}
