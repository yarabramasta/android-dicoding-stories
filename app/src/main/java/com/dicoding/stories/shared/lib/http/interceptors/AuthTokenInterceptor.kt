package com.dicoding.stories.shared.lib.http.interceptors

import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.auth.data.local.SessionManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthTokenInterceptor @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val sessionManager: SessionManager,
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val token: String? = runBlocking(dispatcher) {
      sessionManager.load().first()?.token
    }

    val builder = chain.request().newBuilder()
    var request = builder.build()

    if (token != null) {
      request = builder
        .addHeader("Authorization", "Bearer $token")
        .build()
    }

    return chain.proceed(request)
  }
}
