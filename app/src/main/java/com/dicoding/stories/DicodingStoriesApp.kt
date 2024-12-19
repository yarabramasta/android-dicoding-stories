package com.dicoding.stories

import android.app.Application
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.CachePolicy
import coil3.request.crossfade
import coil3.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DicodingStoriesApp : Application(), SingletonImageLoader.Factory {

  override fun newImageLoader(
    context: PlatformContext,
  ): ImageLoader = ImageLoader.Builder(context)
    .crossfade(true)
    .memoryCachePolicy(CachePolicy.ENABLED)
    .diskCachePolicy(CachePolicy.ENABLED)
    .logger(DebugLogger())
    .build()
}
