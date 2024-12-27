package com.dicoding.stories

import androidx.paging.PagingSource
import com.dicoding.stories.features.auth.domain.business.SignInUseCase
import com.dicoding.stories.features.stories.data.local.StoryEntity
import com.dicoding.stories.helper.AuthResource
import com.dicoding.stories.helper.StoriesPagingResource
import com.dicoding.stories.helper.TestResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class StoriesPagingUnitTest {
  private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
  private val testResource = TestResource()

  private lateinit var auth: AuthResource
  private var token: String? = null

  private lateinit var resource: StoriesPagingResource

  @Before
  fun setupDispatcher() {
    Dispatchers.setMain(testDispatcher)
  }

  @Before
  @Throws(Exception::class)
  fun setUp() = runTest {
    auth = AuthResource(testResource)
    token = signIn()

    resource = StoriesPagingResource(
      token = token!!,
      testResource = testResource
    )
  }

  private suspend fun fetchStories(
    page: Int = 0,
    size: Int = StoriesPagingResource.DEFAULT_PAGING_CONFIG_PAGE_SIZE,
  ): PagingSource.LoadResult<Int, StoryEntity> {
    return resource.providePagingSource().load(
      PagingSource.LoadParams.Refresh(
        key = page,
        loadSize = size,
        placeholdersEnabled = false
      )
    )
  }

  @Test
  @Throws(Exception::class)
  fun `should not return null value`() = runTest {
    val loadResult = fetchStories()

    Assert.assertTrue(loadResult is PagingSource.LoadResult.Page)
    Assert.assertNotNull((loadResult as? PagingSource.LoadResult.Page)?.data)
  }

  @Test
  @Throws(Exception::class)
  fun `should have the correct data size as the page size`() = runTest {
    val pageSize = 10

    val firstPageLoadResult = fetchStories(size = pageSize)
    val secondPageLoadResult = fetchStories(page = 1, size = pageSize)

    Assert.assertTrue(
      firstPageLoadResult is PagingSource.LoadResult.Page
        && secondPageLoadResult is PagingSource.LoadResult.Page
    )
    Assert.assertEquals(
      pageSize,
      (firstPageLoadResult as PagingSource.LoadResult.Page).data.size
    )
    Assert.assertEquals(
      pageSize,
      (secondPageLoadResult as PagingSource.LoadResult.Page).data.size
    )
  }

  @Test
  @Throws(Exception::class)
  fun `should have the correct data`() = runTest {
    val loadResult = fetchStories()

    Assert.assertTrue(loadResult is PagingSource.LoadResult.Page)
    Assert.assertTrue(
      (loadResult as PagingSource.LoadResult.Page).data.all {
        it is StoryEntity
      }
    )
  }

  @Test
  @Throws
  fun `should have exactly 0 data size`() = runTest {
    // assuming server response for page 1000 is always empty
    val loadResult = fetchStories(page = 1000)

    Assert.assertTrue(loadResult is PagingSource.LoadResult.Page)
    Assert.assertEquals(
      0,
      (loadResult as PagingSource.LoadResult.Page).data.size
    )
  }

  private suspend fun signIn(): String {
    auth.provideSignInUseCase(testDispatcher).invoke(
      SignInUseCase.Params(
        "bramasta.yb@gmail.com",
        "test12345"
      )
    )
      .fold(
        onSuccess = { token = it.token },
        onFailure = { token = null }
      )

    if (token == null) {
      throw Exception("Token is null!")
    }

    return token!!
  }
}
