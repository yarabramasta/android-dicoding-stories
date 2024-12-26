package com.dicoding.stories.features.locations.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.dicoding.stories.R
import com.dicoding.stories.features.locations.presentation.viewmodel.StoriesLocationsState
import com.dicoding.stories.shared.ui.lib.UiStatus
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoriesLocationsScreen(
  state: StoriesLocationsState,
  onRefresh: () -> Unit = {},
  onBack: () -> Unit = {},
) {
  val latLng = LatLng(-7.97754588970656, 112.5945144200797)
  val cameraPositionState = rememberCameraPositionState {
    position = CameraPosition.fromLatLngZoom(latLng, 6f)
  }

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
        navigationIcon = {
          IconButton(onClick = onBack) {
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
              contentDescription = "Back to home"
            )
          }
        }
      )
    }
  ) { innerPadding ->
    AnimatedVisibility(
      visible = state.status !is UiStatus.Success,
      enter = fadeIn(),
      exit = fadeOut()
    ) {
      Box(
        modifier = Modifier
          .fillMaxSize()
          .padding(innerPadding)
          .background(MaterialTheme.colorScheme.background)
          .zIndex(1f),
        contentAlignment = Alignment.Center
      ) {
        when (state.status) {
          is UiStatus.Loading -> CircularProgressIndicator()

          is UiStatus.Failure -> {
            Column(
              modifier = Modifier.fillMaxSize(),
              verticalArrangement = Arrangement.spacedBy(
                16.dp,
                alignment = Alignment.CenterVertically
              ),
              horizontalAlignment = Alignment.CenterHorizontally
            ) {
              Icon(
                imageVector = Icons.Outlined.Warning,
                contentDescription = "Failed to load stories locations",
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.outline,
              )
              Text(
                text =
                when (state.status.message) {
                  "UnknownException" -> "Exception: " + stringResource(R.string.err_general_trouble)
                  "Unauthorized" -> "Exception: " + stringResource(R.string.err_unauthorized)
                  else -> "Exception: ${state.status.message}"
                },
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
                textAlign = TextAlign.Center
              )
            }
          }

          else -> Box {}
        }
      }
    }

    if (state.stories.isNotEmpty()) {
      PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh
      ) {
        Box(
          modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
          GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
          ) {
            state.stories
              .filter { it.getLatLng() != null }
              .forEach { story ->
                Marker(
                  state = rememberMarkerState(
                    key = story.id,
                    position = story.getLatLng()!!,
                  ),
                  title = stringResource(
                    R.string.story_uploaded_by,
                    story.name
                  ),
                  snippet = "Created at: ${story.getFormattedDate()}",
                  contentDescription = story.description
                )
              }
          }
        }
      }
    }
  }
}
