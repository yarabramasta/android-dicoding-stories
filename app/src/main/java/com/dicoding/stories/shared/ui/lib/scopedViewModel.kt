package com.dicoding.stories.shared.ui.lib

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController

@Composable
inline fun <reified T : ViewModel> NavBackStackEntry.scopedViewModel(
  navController: NavController,
): T {
  val navGraphRoute = destination.parent?.route ?: return hiltViewModel()
  Log.d("scopedViewModel", "navGraphRoute: $navGraphRoute")
  val parentEntry: NavBackStackEntry = remember(this) {
    navController.getBackStackEntry(navGraphRoute)
  }
  return hiltViewModel(parentEntry)
}
