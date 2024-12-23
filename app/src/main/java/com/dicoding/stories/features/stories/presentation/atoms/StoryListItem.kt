package com.dicoding.stories.features.stories.presentation.atoms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import com.dicoding.stories.R
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.shared.ui.composables.ShimmerBox
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@Composable
fun StoryListItem(
  modifier: Modifier = Modifier,
  story: Story,
  onClick: (Story) -> Unit,
) {
  ListItem(
    modifier = modifier.clickable { onClick(story) },
    leadingContent = {
      SubcomposeAsyncImage(
        model = story.photoUrl,
        contentDescription = stringResource(
          R.string.story_uploaded_by,
          story.name
        ),
        loading = { ShimmerBox(modifier = Modifier.size(64.dp)) },
        error = {
          ShimmerBox(
            modifier = Modifier.size(64.dp),
            animate = false
          )
        },
        modifier = Modifier
          .size(80.dp)
          .aspectRatio(1f)
          .clip(MaterialTheme.shapes.small)
      )
    },
    headlineContent = {
      Text(
        text = stringResource(R.string.story_uploaded_by, story.name),
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
      )
    },
    supportingContent = {
      Text(
        text = story.description,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
      )
    }
  )
}

@Preview(showBackground = true)
@Composable
private fun StoryListItemPreview() {
  val story = Story.dummy()

  DicodingStoriesTheme {
    StoryListItem(
      story = story,
      onClick = {}
    )
  }
}
