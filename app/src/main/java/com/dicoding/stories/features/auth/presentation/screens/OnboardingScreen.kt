package com.dicoding.stories.features.auth.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.stories.shared.ui.navigation.AppRoutes
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@Composable
fun OnboardingScreen(
  onNavigate: (AppRoutes) -> Unit,
) {
  Scaffold(
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      verticalArrangement = Arrangement.Center,
      horizontalAlignment = Alignment.CenterHorizontally,
      contentPadding = PaddingValues(24.dp)
    ) {
      item {
        Button(
          modifier = Modifier.fillMaxWidth(),
          onClick = { onNavigate(AppRoutes.SignIn) }
        ) {
          Text("Sign In")
        }
      }
      item {
        OutlinedButton(
          modifier = Modifier.fillMaxWidth(),
          onClick = { onNavigate(AppRoutes.SignUp) }
        ) {
          Text("Sign Up")
        }
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun OnboardingScreenPreview() {
  DicodingStoriesTheme {
    OnboardingScreen(
      onNavigate = {}
    )
  }
}
