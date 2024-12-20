package com.dicoding.stories.features.auth.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.stories.R
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
      verticalArrangement = Arrangement.spacedBy(
        space = 16.dp,
        alignment = Alignment.CenterVertically
      ),
      horizontalAlignment = Alignment.CenterHorizontally,
      contentPadding = PaddingValues(24.dp)
    ) {
      item {
        Image(
          painter = painterResource(R.drawable.img_onboarding_illustration),
          contentDescription = "Onboarding Illustration",
          modifier = Modifier.size(240.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
      }
      item {
        Text(
          text = stringResource(R.string.onboarding_headline),
          modifier = Modifier.fillMaxWidth(),
          style = MaterialTheme.typography.headlineSmall,
          fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = stringResource(R.string.onboarding_sub_headline),
          style = MaterialTheme.typography.bodyMedium,
          color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
      }
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
