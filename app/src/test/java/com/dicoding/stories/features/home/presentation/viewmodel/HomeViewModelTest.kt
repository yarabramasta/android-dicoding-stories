package com.dicoding.stories.features.home.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.dicoding.stories.features.home.data.StoryPagingSource
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.domain.repositories.StoriesPagingRepository
import com.dicoding.stories.helper.DataDummy
import com.dicoding.stories.helper.MainDispatcherRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineScheduler
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doReturn

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class HomeViewModelTest {

  @get:Rule
  val instantTaskExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val mainDispatcherRule = MainDispatcherRule()

  private val testScheduler = TestCoroutineScheduler()
  private val testDispatcher = StandardTestDispatcher(testScheduler)

  @Mock
  private lateinit var repository: StoriesPagingRepository

  @Suppress("DEPRECATION")
  @Before
  fun setUp() {
    MockitoAnnotations.initMocks(this)
  }

  @Test
  fun `when get story should not null and return data`() =
    runTest(testDispatcher) {
      val dummyStory = DataDummy.generateDummyStoryResponse()
      val storyPagingData = StoryPagingSource.snapshot(dummyStory)
      val expectedStory = flowOf(storyPagingData)

      doReturn(expectedStory).`when`(repository).getStoriesList()

      val viewModel = HomeViewModel(
        dispatcher = testDispatcher,
        repository = repository
      )
      val actualStory = mutableListOf<PagingData<Story>>()
      val job = launch(testDispatcher) {
        viewModel.storiesPaging.collectLatest {
          actualStory.add(it)
        }
      }

      advanceUntilIdle()
      job.cancelAndJoin()

      val differ = AsyncPagingDataDiffer(
        DIFF_CALLBACK,
        noopListUpdateCallback,
        Dispatchers.Main
      )
      differ.submitData(actualStory.first())

      Assert.assertNotNull(differ.snapshot())
      Assert.assertEquals(dummyStory.size, differ.snapshot().size)
      Assert.assertEquals(dummyStory[0], differ.snapshot()[0])
    }

  @Test
  fun `when get story empty should return no data`() =
    runTest(testDispatcher) {
      val storyPagingData = StoryPagingSource.snapshot(emptyList())
      val expectedStory = flowOf(storyPagingData)

      doReturn(expectedStory).`when`(repository).getStoriesList()

      val viewModel = HomeViewModel(
        dispatcher = testDispatcher,
        repository = repository
      )
      val actualStory = mutableListOf<PagingData<Story>>()
      val job = launch(testDispatcher) {
        viewModel.storiesPaging.collectLatest {
          actualStory.add(it)
        }
      }

      advanceUntilIdle()
      job.cancelAndJoin()

      val differ = AsyncPagingDataDiffer(
        DIFF_CALLBACK,
        noopListUpdateCallback,
        Dispatchers.Main
      )
      differ.submitData(actualStory.first())

      Assert.assertEquals(0, differ.snapshot().size)
      Assert.assertTrue(differ.snapshot().isEmpty())
    }
}

val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>() {
  override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
    return oldItem == newItem
  }

  override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
    return oldItem.id == newItem.id
  }
}

val noopListUpdateCallback = object : ListUpdateCallback {
  override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
  override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
  override fun onInserted(position: Int, count: Int) = Unit
  override fun onRemoved(position: Int, count: Int) = Unit
}
