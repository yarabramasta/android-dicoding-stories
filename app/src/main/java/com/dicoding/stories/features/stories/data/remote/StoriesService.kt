package com.dicoding.stories.features.stories.data.remote

import com.skydoves.sandwich.ApiResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface StoriesService {

  @Multipart
  @POST("stories")
  suspend fun createStory(
    @Part("description") description: RequestBody,
    @Part("lat") lat: RequestBody?,
    @Part("lon") lon: RequestBody?,
    @Part photo: MultipartBody.Part,
  ): ApiResponse<CreateStoryResponse>

  @GET("stories")
  suspend fun getStories(
    @Query("page") page: Int = 0,
    @Query("limit") size: Int = 10,
    @Query("location") location: Int = 0,
  ): ApiResponse<GetStoriesResponse>

  @GET("stories/{id}")
  suspend fun getStoryDetails(
    @Path("id") id: String,
  ): ApiResponse<GetStoryDetailResponse>

  @GET("stories")
  suspend fun getStoriesWithExplicitHeader(
    @Header("Authorization") token: String,
    @Query("page") page: Int = 0,
    @Query("limit") size: Int = 10,
    @Query("location") location: Int = 0,
  ): ApiResponse<GetStoriesResponse>
}
