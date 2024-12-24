package com.dicoding.stories.shared.lib.utils

import java.io.File

fun validateFileSize(
  path: String,
  maxSizeInMB: Int,
): Boolean {
  val fileSize = File(path).length()
  val sizeInMB = fileSize / (1024 * 1024)
  return sizeInMB <= maxSizeInMB
}
