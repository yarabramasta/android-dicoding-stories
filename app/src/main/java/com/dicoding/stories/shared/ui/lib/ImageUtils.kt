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
    bitmap: Bitmap,
    uri: Uri,
  ): File {
    return withContext(Dispatchers.IO) {
      var fileName = File(uri.path ?: "").name
      listOf(".jpg", ".jpeg", ".png").let { extensions ->
        if (!extensions.any { fileName.endsWith(it) }) {
          fileName = "$fileName.webp"
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
    }
  }


  private fun File.createFileAndDirs() = apply {
    parentFile?.mkdirs()
    createNewFile()
  }

  @Suppress("DEPRECATION")
  private fun File.write(
    bitmap: Bitmap,
    format: Bitmap.CompressFormat = Bitmap.CompressFormat.WEBP,
    quality: Int = 95,
  ) = apply {
    createFileAndDirs()
    outputStream().use { out ->
      bitmap.compress(format, quality, out)
      out.flush()
    }
  }
}
