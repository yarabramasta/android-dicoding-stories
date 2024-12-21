package com.dicoding.stories.features.auth.presentation.atoms

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.dicoding.stories.shared.ui.lib.UiText
import com.dicoding.stories.shared.ui.lib.keyboardAsState

@Composable
fun AuthFormLayout(
  headline: UiText,
  subHeadline: UiText,
  onBack: () -> Unit,
  form: LazyListScope.() -> Unit,
) {
  val isKeyboardOpen by keyboardAsState()

  BackHandler(enabled = !isKeyboardOpen) { onBack() }

  Scaffold(
    modifier = Modifier.fillMaxSize()
  ) { innerPadding ->
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      contentPadding = PaddingValues(24.dp),
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(
        space = 16.dp,
        alignment = Alignment.CenterVertically
      )
    ) {
      buildHeadline(
        headline = headline,
        subHeadline = subHeadline
      )
      form()
    }
  }
}

private fun LazyListScope.buildHeadline(
  headline: UiText,
  subHeadline: UiText,
) {
  item {
    Text(
      text = headline.asString(),
      modifier = Modifier.fillMaxWidth(),
      style = MaterialTheme.typography.headlineSmall,
      fontWeight = FontWeight.Bold
    )
    Spacer(modifier = Modifier.height(8.dp))
    Text(
      text = subHeadline.asString(),
      modifier = Modifier.fillMaxWidth(),
      style = MaterialTheme.typography.bodyMedium,
      color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    Spacer(modifier = Modifier.height(8.dp))
  }
}
