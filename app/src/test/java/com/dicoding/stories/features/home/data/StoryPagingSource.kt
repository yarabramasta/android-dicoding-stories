package com.dicoding.stories.features.home.data

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dicoding.stories.features.stories.domain.models.Story

class StoryPagingSource : PagingSource<Int, Story>() {
  companion object {
    fun snapshot(items: List<Story>): PagingData<Story> {
      return PagingData.from(items)
    }
  }

  override fun getRefreshKey(state: PagingState<Int, Story>): Int {
    return 0
  }

  override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Story> {
    return LoadResult.Page(
      data = emptyList(),
      prevKey = 0,
      nextKey = 1
    )
  }
}
