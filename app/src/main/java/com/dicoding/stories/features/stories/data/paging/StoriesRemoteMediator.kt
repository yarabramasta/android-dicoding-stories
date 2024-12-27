package com.dicoding.stories.features.stories.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.dicoding.stories.features.stories.data.local.RemoteKeyEntity
import com.dicoding.stories.features.stories.data.local.StoryEntity
import com.dicoding.stories.features.stories.data.remote.StoriesService
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.shared.data.local.DicodingStoriesDatabase
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import retrofit2.Response
import javax.inject.Inject

private const val REMOTE_KEY_ID = "stories"

@OptIn(ExperimentalPagingApi::class)
class StoriesRemoteMediator @Inject constructor(
  private val db: DicodingStoriesDatabase,
  private val api: StoriesService,
) : RemoteMediator<Int, StoryEntity>() {

  override suspend fun load(
    loadType: LoadType,
    state: PagingState<Int, StoryEntity>,
  ): MediatorResult {
    try {
      val page = when (loadType) {
        LoadType.REFRESH -> 0
        LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
        LoadType.APPEND -> {
          val remoteKey = db.remoteKeysDao.getById(REMOTE_KEY_ID)
          if (remoteKey?.nextPage == null) {
            return MediatorResult.Success(endOfPaginationReached = true)
          }
          remoteKey.nextPage
        }
      }

      val apiResponse = api.getStories(
        page = page,
        size = state.config.pageSize
      )
      var results: List<Story> = emptyList()
      var exception: Throwable? = null

      apiResponse
        .onSuccess { results = data.listStory }
        .onFailure { exception = Throwable("NetworkError") }
        .onError {
          val statusCode = (payload as? Response<*>)?.code() ?: 500
          exception = when (statusCode) {
            401 -> Throwable("Unauthorized")
            else -> Throwable("UnkownError")
          }
        }

      if (exception != null) throw exception!!

      val endOfPaginationReached = results.size < state.config.pageSize

      db.withTransaction {
        if (loadType == LoadType.REFRESH) {
          db.storiesDao.clearAll()
          db.remoteKeysDao.deleteById(REMOTE_KEY_ID)
        }

        db.storiesDao.insertAll(results.map { it.toEntity() })
        db.remoteKeysDao.insert(
          RemoteKeyEntity(
            id = REMOTE_KEY_ID,
            nextPage = if (endOfPaginationReached) 0 else page + 1
          )
        )
      }

      return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
    } catch (e: Exception) {
      return MediatorResult.Error(e)
    }
  }
}
