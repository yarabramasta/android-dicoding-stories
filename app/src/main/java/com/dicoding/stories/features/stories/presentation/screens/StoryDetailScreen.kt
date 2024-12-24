package com.dicoding.stories.features.stories.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Fingerprint
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil3.compose.SubcomposeAsyncImage
import com.dicoding.stories.R
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.shared.ui.composables.ShimmerBox
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoryDetailScreen(
  story: Story?,
  onBack: () -> Unit,
) {
  BackHandler(enabled = true) { onBack() }

  val details = story ?: Story.placeholder()

  val scrollState = rememberLazyListState()
  val isScrolling by remember {
    derivedStateOf { scrollState.isScrollInProgress }
  }
  val topAppBarBgColor by animateColorAsState(
    if (isScrolling) MaterialTheme.colorScheme.background
    else Color.Transparent,
    label = "TopAppBarBackgroundColor"
  )

  Scaffold(
    modifier = Modifier.fillMaxSize(),
  ) { innerPadding ->
    Box {
      TopAppBar(
        modifier = Modifier
          .drawBehind { drawRect(color = topAppBarBgColor) }
          .zIndex(1f),
        colors = TopAppBarDefaults
          .topAppBarColors()
          .copy(containerColor = Color.Transparent),
        title = {
          AnimatedVisibility(
            visible = isScrolling,
            enter = fadeIn(
              animationSpec = tween(
                durationMillis = 300,
                delayMillis = 50,
                easing = LinearEasing
              )
            ),
            exit = fadeOut(
              animationSpec = tween(
                durationMillis = 300,
                delayMillis = 150,
                easing = LinearEasing
              )
            )
          ) {
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
          }
        },
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
              contentDescription = "Back to home"
            )
          }
        }
      )
      LazyColumn(
        state = scrollState,
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding),
        verticalArrangement = Arrangement.spacedBy(16.dp)
      ) {
        item {
          SubcomposeAsyncImage(
            model = details.photoUrl,
            contentDescription = stringResource(
              R.string.story_uploaded_by,
              details.name
            ),
            contentScale = ContentScale.FillBounds,
            loading = {
              ShimmerBox(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(360.dp),
                rounded = false,
              )
            },
            error = {
              ShimmerBox(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(360.dp),
                animate = false,
                rounded = false,
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(360.dp)
          )
        }
        item {
          Text(
            text = stringResource(R.string.story_uploaded_by, details.name),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
              .padding(
                horizontal = 24.dp,
                vertical = 8.dp
              )
          )
          Text(
            text = details.description,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier
              .padding(horizontal = 24.dp)
              .padding(bottom = 8.dp)
          )
        }
        listOf(
          DetailItem(
            icon = Icons.Outlined.Fingerprint,
            text = "ID - ${details.id}"
          ),
          DetailItem(
            icon = Icons.Outlined.LocationOn,
            text = "Location: latlong(${details.lat ?: -0.0}, ${details.lon ?: 0.0})"
          ),
          DetailItem(
            icon = Icons.Outlined.CalendarToday,
            text = "Created at ${details.createdAt}"
          ),
        ).forEach { item ->
          item {
            DetailItemListTile(item)
          }
        }
      }
    }
  }
}

private data class DetailItem(
  val icon: ImageVector,
  val text: String,
)

@Composable
private fun DetailItemListTile(item: DetailItem) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(8.dp),
    modifier = Modifier.padding(horizontal = 24.dp)
  ) {
    Icon(
      imageVector = item.icon,
      contentDescription = null,
      modifier = Modifier.size(16.dp),
      tint = MaterialTheme.colorScheme.primary
    )
    Text(
      text = item.text,
      style = MaterialTheme.typography.labelMedium,
      color = MaterialTheme.colorScheme.primary
    )
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StoryDetailScreenPreview() {
  val story = Story.dummy()
  DicodingStoriesTheme {
    StoryDetailScreen(
      story = story,
      onBack = {}
    )
  }
}
