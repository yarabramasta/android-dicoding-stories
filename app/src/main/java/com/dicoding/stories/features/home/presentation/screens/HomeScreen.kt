package com.dicoding.stories.features.home.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Interests
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.dicoding.stories.R
import com.dicoding.stories.features.auth.presentation.viewmodel.signout.SignOutState
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.presentation.atoms.StoryListItem
import com.dicoding.stories.shared.ui.composables.ShimmerItem
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.setLocale
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  signOutState: SignOutState = SignOutState.initial(),
  onSignOut: () -> Unit = {},
  onRefresh: () -> Unit = {},
  stories: LazyPagingItems<Story>,
  onStoryClick: (Story) -> Unit = {},
  onNavigateCreateStory: () -> Unit = {},
  onNavigateStoriesLocations: () -> Unit = {},
) {
  val context = LocalContext.current
  val locale = LocalContext.current.resources.configuration.locales[0].language

  PullToRefreshBox(
    isRefreshing = stories.loadState.refresh == LoadState.Loading,
    onRefresh = onRefresh,
  ) {
    Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
        TopAppBar(
          title = {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp),
              modifier = Modifier.width(IntrinsicSize.Max)
            ) {
              Image(
                painter = painterResource(R.drawable.img_onboarding_illustration),
                contentDescription = "Dicoding Stories",
                modifier = Modifier.size(32.dp)
              )
              Text(
                text = "Dicoding Story",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
              )
            }
          },
          actions = {
            TextButton(
              enabled = signOutState.status == UiStatus.Idle,
              onClick = {
                setLocale(
                  context,
                  when (locale) {
                    "en" -> "in"
                    else -> "en"
                  }
                )
              }
            ) {
              Text(
                text = when (locale) {
                  "in" -> "🇮🇩 ID"
                  else -> "🇺🇸 EN"
                },
              )
            }
            IconButton(
              enabled = signOutState.status == UiStatus.Idle,
              onClick = onSignOut
            ) {
              Icon(
                imageVector = Icons.AutoMirrored.Outlined.Logout,
                contentDescription = "Sign out"
              )
            }
          }
        )
      },
      floatingActionButton = {
        FloatingActionButton(
          onClick = onNavigateCreateStory,
        ) {
          Icon(
            imageVector = Icons.Outlined.Edit,
            contentDescription = "Create story"
          )
        }
      }
    ) { innerPadding ->
      LazyColumn(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 24.dp)
      ) {
        item {
          Text(
            text = stringResource(R.string.home_headline),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 24.dp),
          )
          Spacer(modifier = Modifier.height(8.dp))
          Text(
            text = stringResource(R.string.home_sub_headline),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 24.dp),
          )
        }
        if (stories.itemCount == 0) {
          buildEmptyStories()
        } else {
          buildStoriesLocationsButton(
            loading = stories.loadState.refresh == LoadState.Loading,
            onNavigateStoriesLocations = onNavigateStoriesLocations
          )
          buildStories(
            stories = stories,
            onStoryClick = onStoryClick
          )
        }
      }
    }
  }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun LazyListScope.buildStoriesLocationsButton(
  loading: Boolean,
  onNavigateStoriesLocations: () -> Unit,
) {
  item {
    val locationPermissionState = rememberMultiplePermissionsState(
      permissions = listOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
      )
    ) { map ->
      if (map.all { it.value }) {
        onNavigateStoriesLocations()
      }
    }
    val hasLocationPermission = locationPermissionState.allPermissionsGranted

    Box(
      modifier = Modifier.padding(horizontal = 24.dp)
    ) {
      TextButton(
        onClick = {
          if (hasLocationPermission) {
            onNavigateStoriesLocations()
          } else {
            locationPermissionState.launchMultiplePermissionRequest()
          }
        },
        enabled = !loading
      ) {
        Text(
          text = stringResource(R.string.see_stories_locations),
          textDecoration = TextDecoration.Underline
        )
      }
    }
  }
}

private fun LazyListScope.buildEmptyStories() {
  item {
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(horizontal = 32.dp)
        .padding(top = 64.dp),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(
        imageVector = Icons.Outlined.Interests,
        contentDescription = "No stories",
        modifier = Modifier.size(32.dp),
        tint = MaterialTheme.colorScheme.outline,
      )
      Text(
        text = "Currently there are no more stories to show.\nYou can try to refresh it by pulling down the screen.",
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.outline,
        textAlign = TextAlign.Center
      )
    }
  }
}

private fun LazyListScope.buildStories(
  stories: LazyPagingItems<Story>,
  onStoryClick: (Story) -> Unit,
) {
  item {
    if (stories.loadState.refresh is LoadState.Error) {
      val error = (stories.loadState.refresh as LoadState.Error).error
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
      ) {
        Icon(
          imageVector = Icons.Outlined.Warning,
          contentDescription = "Failed to load stories",
          modifier = Modifier.size(32.dp),
          tint = MaterialTheme.colorScheme.outline,
        )
        Text(
          text = "Uh oh! Failed to load stories.",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.outline,
          textAlign = TextAlign.Center
        )
        Text(
          text = "Exception: ${error.message}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.outline,
          textAlign = TextAlign.Center
        )
      }
    }
  }

  when (stories.loadState.refresh) {
    LoadState.Loading -> {
      items(List(5) { Story.dummy() }) {
        ShimmerItem(
          modifier = Modifier.padding(horizontal = 8.dp)
        )
      }
    }

    is LoadState.Error -> {
      items(List(5) { Story.dummy() }) {
        ShimmerItem(
          animate = false,
          modifier = Modifier.padding(horizontal = 8.dp),
        )
      }
    }

    else -> {
      items(
        count = stories.itemCount,
        key = stories.itemKey { it.id }
      ) { index ->
        val story = stories[index]
        if (story != null) {
          StoryListItem(
            story = story,
            onClick = { onStoryClick(story) },
            modifier = Modifier.padding(horizontal = 8.dp),
          )
        }
      }
    }
  }

  item {
    if (stories.loadState.append == LoadState.Loading) {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        contentAlignment = Alignment.Center
      ) {
        CircularProgressIndicator()
      }
    }
    if (stories.loadState.append.endOfPaginationReached) {
      Text(
        text = stringResource(R.string.err_home_paging_end_of_pagination),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        textAlign = TextAlign.Center,
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp)
      )
    }
    if (stories.loadState.append is LoadState.Error) {
      val error = (stories.loadState.append as LoadState.Error).error
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(
          8.dp,
          alignment = Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
      ) {
        Icon(
          imageVector = Icons.Outlined.Warning,
          contentDescription = "Failed to load stories",
          modifier = Modifier.size(32.dp),
          tint = MaterialTheme.colorScheme.outline,
        )
        Text(
          text = "Exception: ${error.message}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.outline,
          textAlign = TextAlign.Center
        )
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
  val data = PagingData.from(List(5) { Story.dummy() })
  val flow = MutableStateFlow(data)
  val stories = flow.collectAsLazyPagingItems()

  DicodingStoriesTheme {
    HomeScreen(stories = stories)
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenEmptyStoriesPreview() {
  val data = PagingData.from(emptyList<Story>())
  val flow = MutableStateFlow(data)
  val stories = flow.collectAsLazyPagingItems()

  DicodingStoriesTheme {
    HomeScreen(stories = stories)
  }
}
