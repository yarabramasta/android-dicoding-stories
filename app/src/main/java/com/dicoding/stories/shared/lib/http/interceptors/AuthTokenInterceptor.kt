package com.dicoding.stories.shared.lib.http.interceptors

import com.dicoding.stories.di.IoDispatcher
import com.dicoding.stories.features.auth.domain.business.GetSessionUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthTokenInterceptor @Inject constructor(
  @IoDispatcher private val dispatcher: CoroutineDispatcher,
  private val getSessionUseCase: GetSessionUseCase,
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val token: String? = runBlocking(dispatcher) {
      getSessionUseCase.invoke(Unit)
        .first()
        .getOrNull()?.token
    }

    val request = chain.request()
    if (token != null) {
      request
        .newBuilder()
        .addHeader("Authorization", "Bearer $token")
        .build()
    }

    return chain.proceed(request)
  }
}
