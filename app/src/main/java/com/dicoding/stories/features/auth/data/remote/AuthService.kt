package com.dicoding.stories.features.auth.data.remote

import com.skydoves.sandwich.ApiResponse
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {

  @POST("register")
  suspend fun register(@Body body: RegisterBody): ApiResponse<RegisterResponse>

  @POST("login")
  suspend fun login(@Body body: LoginBody): ApiResponse<LoginResponse>
}

@Serializable
data class RegisterBody(
  val name: String,
  val email: String,
  val password: String,
)

@Serializable
data class LoginBody(
  val email: String,
  val password: String,
)
