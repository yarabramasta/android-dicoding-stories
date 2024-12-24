package com.dicoding.stories.features.stories.presentation.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@Composable
fun StoryDetailScreen(
  onBack: () -> Unit,
) {
  BackHandler(enabled = true) { onBack() }

  Scaffold(
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun StoryDetailScreenPreview() {
  DicodingStoriesTheme {
    StoryDetailScreen(
      onBack = {}
    )
  }
}
