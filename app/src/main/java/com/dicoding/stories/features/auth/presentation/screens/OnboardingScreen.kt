package com.dicoding.stories.features.auth.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import kotlinx.coroutines.delay

@Composable
fun OnboardingScreen(
  onNavigate: (AppRoutes) -> Unit,
) {
  var isVisible by remember { mutableStateOf(false) }

  LaunchedEffect(Unit) {
    delay(300L)
    isVisible = true
  }

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
        AnimatedVisibility(
          visible = isVisible,
          enter = fadeIn(
            animationSpec = tween(
              durationMillis = 300,
              delayMillis = 50,
              easing = LinearEasing
            )
          ) + slideInVertically(
            tween(
              durationMillis = 500,
              easing = FastOutSlowInEasing
            ),
            initialOffsetY = { it / 6 }
          ),
          exit = fadeOut(
            animationSpec = tween(
              durationMillis = 300,
              delayMillis = 150,
              easing = LinearEasing
            )
          )
        ) {
          Image(
            painter = painterResource(R.drawable.img_onboarding_illustration),
            contentDescription = "Onboarding Illustration",
            modifier = Modifier.size(240.dp)
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
      }
      item {
        AnimatedVisibility(
          visible = isVisible,
          enter = fadeIn(
            animationSpec = tween(
              durationMillis = 300,
              delayMillis = 150,
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
          Text(
            text = stringResource(R.string.onboarding_headline),
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
        AnimatedVisibility(
          visible = isVisible,
          enter = fadeIn(
            animationSpec = tween(
              durationMillis = 300,
              delayMillis = 200,
              easing = LinearEasing
            )
          ),
          exit = fadeOut(
            animationSpec = tween(
              durationMillis = 300,
              delayMillis = 200,
              easing = LinearEasing
            )
          )
        ) {
          Text(
            text = stringResource(R.string.onboarding_sub_headline),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
          )
        }
        Spacer(modifier = Modifier.height(8.dp))
      }
      item {
        AnimatedVisibility(
          visible = isVisible,
          enter = fadeIn(
            animationSpec = tween(
              durationMillis = 300,
              delayMillis = 200,
              easing = LinearEasing
            )
          ),
          exit = fadeOut(
            animationSpec = tween(
              durationMillis = 300,
              delayMillis = 200,
              easing = LinearEasing
            )
          )
        ) {
          Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onNavigate(AppRoutes.SignIn) }
          ) {
            Text("Sign In")
          }
        }
      }
      item {
        AnimatedVisibility(
          visible = isVisible,
          enter = fadeIn(
            animationSpec = tween(
              durationMillis = 300,
              delayMillis = 200,
              easing = LinearEasing
            )
          ),
          exit = fadeOut(
            animationSpec = tween(
              durationMillis = 300,
              delayMillis = 200,
              easing = LinearEasing
            )
          )
        ) {
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
