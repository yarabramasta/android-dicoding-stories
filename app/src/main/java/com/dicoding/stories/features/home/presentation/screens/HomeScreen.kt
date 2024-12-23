package com.dicoding.stories.features.home.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Interests
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.stories.R
import com.dicoding.stories.features.auth.presentation.viewmodel.signout.SignOutState
import com.dicoding.stories.features.home.presentation.viewmodel.HomeState
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.presentation.atoms.StoryListItem
import com.dicoding.stories.shared.ui.composables.ShimmerItem
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.setLocale
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
  signOutState: SignOutState = SignOutState.initial(),
  onSignOut: () -> Unit = {},
  onRefresh: () -> Unit = {},
  state: HomeState = HomeState.initial(),
  onStoryClick: (Story) -> Unit = {},
  onNavigateCreateStory: () -> Unit = {},
) {
  val context = LocalContext.current
  val locale = LocalContext.current.resources.configuration.locales[0].language

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
      if (state.stories.isEmpty()) {
        buildEmptyStories()
      } else {
        buildStories(
          status = state.status,
          stories = state.stories,
          onStoryClick = onStoryClick
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
  status: UiStatus,
  stories: List<Story>,
  onStoryClick: (Story) -> Unit,
) {
  if (status is UiStatus.Failure) {
    item {
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
          text = "Exception: ${status.message}",
          style = MaterialTheme.typography.bodySmall,
          color = MaterialTheme.colorScheme.outline,
          textAlign = TextAlign.Center
        )
      }
    }
  }

  items(stories.size,
    key = { index -> stories[index].id }
  ) { index ->
    when (status) {
      is UiStatus.Loading -> {
        ShimmerItem(
          modifier = Modifier.padding(horizontal = 8.dp)
        )
      }

      is UiStatus.Failure -> {
        ShimmerItem(
          animate = false,
          modifier = Modifier.padding(horizontal = 8.dp),
        )
      }

      else -> {
        val story = stories[index]
        StoryListItem(
          story = story,
          onClick = { onStoryClick(story) },
          modifier = Modifier.padding(horizontal = 8.dp),
        )
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenPreview() {
  DicodingStoriesTheme {
    HomeScreen(
      state = HomeState.initial()
        .copy(status = UiStatus.Success),
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenEmptyStoriesPreview() {
  DicodingStoriesTheme {
    HomeScreen(
      state = HomeState.initial().copy(
        status = UiStatus.Success,
        stories = emptyList()
      ),
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenLoadingPreview() {
  DicodingStoriesTheme {
    HomeScreen(
      state = HomeState.initial()
        .copy(status = UiStatus.Loading),
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun HomeScreenFailurePreview() {
  DicodingStoriesTheme {
    HomeScreen(
      state = HomeState.initial()
        .copy(status = UiStatus.Failure("Cannot retrieve stories from server due to network error...")),
    )
  }
}
