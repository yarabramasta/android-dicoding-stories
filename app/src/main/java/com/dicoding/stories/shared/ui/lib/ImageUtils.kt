package com.dicoding.stories.shared.ui.lib

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ImageUtils {
  fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat(
      "yyyyMMdd_HHmmss", Locale.US
    ).format(Date())
    val filename = "story_${timeStamp}_"
    return File.createTempFile(filename, ".jpg", context.externalCacheDir)
  }

  suspend fun processImage(
    context: Context,
    bitmap: Bitmap? = null,
    uri: Uri? = null,
  ): File {
    return withContext(Dispatchers.IO) {
      if (bitmap != null && uri != null) {
        var fileName = File(uri.path ?: "").name
        listOf(".jpg", ".jpeg", ".png").let { extensions ->
          if (!extensions.any { fileName.endsWith(it) }) {
            fileName = "$fileName.jpeg"
          }
        }

        try {
          val file = File(context.externalCacheDir, fileName)
          file.write(bitmap)
          return@withContext file
        } catch (e: IOException) {
          Log.e("CreateStoryViewModel", "Error writing bitmap", e)
          throw e
        }
      } else {
        throw IllegalArgumentException("Bitmap and Uri cannot be null at the same time")
      }
    }
  }


  fun File.createFileAndDirs() = apply {
    parentFile?.mkdirs()
    createNewFile()
  }

  fun File.write(
    bitmap: Bitmap,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG,
    quality: Int = 95,
  ) = apply {
    createFileAndDirs()
    outputStream().use { out ->
      bitmap.compress(format, quality, out)
      out.flush()
    }
  }
}
