package com.dicoding.stories.features.stories.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dicoding.stories.R
import com.dicoding.stories.features.auth.presentation.viewmodel.signout.SignOutState
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.dicoding.stories.shared.ui.lib.setLocale
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
  signOutState: SignOutState,
  onSignOut: () -> Unit,
) {
  val context = LocalContext.current
  val locale = Locale.current.toLanguageTag()

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
                  "en" -> "id"
                  else -> "en"
                }
              )
            }
          ) {
            Text(
              text = when (locale) {
                "id" -> "🇮🇩 ID"
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
    }
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier.padding(innerPadding),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(24.dp)
    ) {
      item {
        Text(
          text = "Welcome to Dicoding Stories",
          style = MaterialTheme.typography.titleLarge,
          fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
          text = "See the latest stories from Dicoding, or create your own stories using the (+) button below.",
        )
      }
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
  DicodingStoriesTheme {
    MainScreen(
      signOutState = SignOutState.initial(),
      onSignOut = {}
    )
  }
}
