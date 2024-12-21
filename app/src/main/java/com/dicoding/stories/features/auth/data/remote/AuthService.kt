package com.dicoding.stories.features.auth.data.remote

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Field
import retrofit2.http.POST

interface AuthService {
  @POST("register")
  suspend fun register(
    @Field("name") name: String,
    @Field("email") email: String,
    @Field("password") password: String,
  ): ApiResponse<RegisterResponse>

  @POST("login")
  suspend fun login(
    @Field("email") email: String,
    @Field("password") password: String,
  ): ApiResponse<LoginResponse>
}
