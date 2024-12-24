package com.dicoding.stories.features.stories.data.remote

import com.skydoves.sandwich.ApiResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface StoriesService {
  
  @POST("stories")
  suspend fun createStory(
    @Body body: RequestBody,
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
}
