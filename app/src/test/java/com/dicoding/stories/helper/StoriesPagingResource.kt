package com.dicoding.stories.helper

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.stories.features.stories.data.local.StoryEntity
import com.dicoding.stories.features.stories.data.remote.StoriesService
import com.dicoding.stories.features.stories.data.repositories.StoriesPagingRepositoryImpl
import com.dicoding.stories.features.stories.domain.business.GetStoriesPagingUseCase
import com.dicoding.stories.features.stories.domain.repositories.StoriesPagingRepository
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineDispatcher
import retrofit2.Response

class StoriesPagingResource(
  private val testResource: TestResource,
  private val token: String,
) {
  private var repository: StoriesPagingRepository? = null
  private var useCase: GetStoriesPagingUseCase? = null

  companion object {
    const val DEFAULT_PAGING_CONFIG_PAGE_SIZE = 10
  }

  fun providePagingSource(): StoriesPagingSource {
    return StoriesPagingSource(
      token = token,
      api = testResource.provideRetrofit()
        .create(StoriesService::class.java),
    )
  }

  fun providePager(
    pageSize: Int = DEFAULT_PAGING_CONFIG_PAGE_SIZE,
  ): Pager<Int, StoryEntity> {
    return Pager(
      config = PagingConfig(pageSize = pageSize),
      pagingSourceFactory = {
        providePagingSource()
      }
    )
  }

  private fun provideRepository(
    pageSize: Int = DEFAULT_PAGING_CONFIG_PAGE_SIZE,
  ): StoriesPagingRepository {
    return repository ?: StoriesPagingRepositoryImpl(providePager(pageSize))
  }

  fun provideUseCase(
    dispatcher: CoroutineDispatcher,
    pageSize: Int = DEFAULT_PAGING_CONFIG_PAGE_SIZE,
  ): GetStoriesPagingUseCase {
    return useCase ?: GetStoriesPagingUseCase(
      dispatcher = dispatcher,
      repository = provideRepository(pageSize)
    ).also { useCase = it }
  }

  class StoriesPagingSource(
    private val api: StoriesService,
    private val token: String,
  ) : PagingSource<Int, StoryEntity>() {
    override fun getRefreshKey(state: PagingState<Int, StoryEntity>): Int? {
      return state.anchorPosition?.let { anchorPosition ->
        val anchorPage = state.closestPageToPosition(anchorPosition)
        anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
      }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, StoryEntity> {
      val nextPageNumber = params.key ?: 0
      val apiResponse = api.getStoriesWithExplicitHeader(
        token = "Bearer $token",
        page = nextPageNumber,
        size = params.loadSize
      )
      var results: List<StoryEntity> = emptyList()
      var exception: Throwable? = null

      apiResponse
        .onSuccess {
          results = data.listStory.map { StoryEntity.fromDomain(it) }
        }
        .onFailure { exception = Throwable("NetworkError") }
        .onError {
          val statusCode = (payload as? Response<*>)?.code() ?: 500
          exception = when (statusCode) {
            401 -> Throwable("Unauthorized")
            else -> Throwable("UnkownError")
          }
        }

      if (exception != null) {
        return LoadResult.Error(exception!!)
      }

      val endOfPaginationReached = results.size < params.loadSize

      return LoadResult.Page(
        data = results,
        prevKey = null,
        nextKey = if (endOfPaginationReached) null else nextPageNumber
      )
    }
  }
}
