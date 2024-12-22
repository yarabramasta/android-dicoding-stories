package com.dicoding.stories

import com.dicoding.stories.features.auth.data.remote.AuthService
import com.dicoding.stories.features.auth.data.remote.LoginBody
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun login() {
    val client = OkHttpClient.Builder()
      .addInterceptor(
        HttpLoggingInterceptor().apply {
          level = HttpLoggingInterceptor.Level.BODY
        }
      )
      .build()

    val service = Retrofit.Builder()
      .baseUrl(BuildConfig.DICODING_EVENTS_API_BASE_URL)
      .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
      .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
      .client(client)
      .build()
      .create(AuthService::class.java)

    val body = LoginBody(
      email = "test@mail.co",
      password = "test12345"
    )

    runBlocking {
      service.login(body)
        .onSuccess { assertEquals(false, data.error) }
        .onError {
          val res = payload?.toString()
          assertEquals(true, res?.contains("401") ?: false)
        }
    }
  }
}
