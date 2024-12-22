package com.dicoding.stories.features.stories.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.stories.R
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
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
      )
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier.padding(innerPadding),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(24.dp)
    ) {

    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
  DicodingStoriesTheme {
    MainScreen()
  }
}
