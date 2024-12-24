package com.dicoding.stories

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dicoding.stories.features.auth.presentation.screens.OnboardingScreen
import com.dicoding.stories.features.auth.presentation.screens.SignInScreen
import com.dicoding.stories.features.auth.presentation.screens.SignUpScreen
import com.dicoding.stories.features.auth.presentation.viewmodel.auth.AuthViewModel
import com.dicoding.stories.features.auth.presentation.viewmodel.signin.SignInSideEffect
import com.dicoding.stories.features.auth.presentation.viewmodel.signin.SignInViewModel
import com.dicoding.stories.features.auth.presentation.viewmodel.signout.SignOutSideEffect
import com.dicoding.stories.features.auth.presentation.viewmodel.signout.SignOutViewModel
import com.dicoding.stories.features.auth.presentation.viewmodel.signup.SignUpSideEffect
import com.dicoding.stories.features.auth.presentation.viewmodel.signup.SignUpViewModel
import com.dicoding.stories.features.home.presentation.screens.HomeScreen
import com.dicoding.stories.features.home.presentation.viewmodel.HomeSideEffect
import com.dicoding.stories.features.home.presentation.viewmodel.HomeViewModel
import com.dicoding.stories.features.stories.domain.models.Story
import com.dicoding.stories.features.stories.presentation.screens.StoryDetailScreen
import com.dicoding.stories.shared.ui.lib.scopedViewModel
import com.dicoding.stories.shared.ui.lib.showToast
import com.dicoding.stories.shared.ui.navigation.AppRoutes
import com.dicoding.stories.shared.ui.navigation.navType
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme
import dagger.hilt.android.AndroidEntryPoint
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import kotlin.reflect.typeOf

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

  private val authViewModel: AuthViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    enableEdgeToEdge()

    setContent {
      val authState by authViewModel.collectAsState()
      val startDestination =
        if (authState.session != null) AppRoutes.Home
        else AppRoutes.Onboarding

      val navController = rememberNavController()

      DicodingStoriesTheme {
        NavHost(
          navController = navController,
          startDestination = startDestination,
          enterTransition = { EnterTransition.None },
          exitTransition = { ExitTransition.None }
        ) {
          addOnboarding(navController = navController)
          addSignIn(navController = navController)
          addSignUp(navController = navController)
          addHome(navController = navController)
          addStoryDetail(navController = navController)
        }
      }
    }
  }
}

private fun NavGraphBuilder.addOnboarding(navController: NavHostController) {
  composable<AppRoutes.Onboarding> {
    OnboardingScreen(
      onNavigate = { navController.navigate(it) }
    )
  }
}

private fun NavGraphBuilder.addSignIn(navController: NavHostController) {
  composable<AppRoutes.SignIn> {
    val context = LocalContext.current

    val authViewModel = it.scopedViewModel<AuthViewModel>(navController)

    val signInViewModel = hiltViewModel<SignInViewModel>()
    val state by signInViewModel.collectAsState()

    signInViewModel.collectSideEffect { effect ->
      when (effect) {
        SignInSideEffect.OnSubmitSuccessNavigate -> {
          navController.navigate(AppRoutes.Home) {
            popUpTo(AppRoutes.Home) { inclusive = true }
          }
        }

        is SignInSideEffect.ShowToast -> {
          context.showToast(effect.message)
        }
      }
    }

    SignInScreen(
      state = state,
      onEmailChanged = signInViewModel::onEmailChanged,
      onPasswordChanged = signInViewModel::onPasswordChanged,
      onSubmit = {
        signInViewModel.onSubmit(context) { session ->
          authViewModel.set(session)
        }
      },
      onBack = {
        navController.navigate(AppRoutes.Onboarding) {
          popUpTo(AppRoutes.Onboarding) { inclusive = true }
        }
      }
    )
  }
}

private fun NavGraphBuilder.addSignUp(navController: NavHostController) {
  composable<AppRoutes.SignUp> {
    val context = LocalContext.current

    val signUpViewModel = hiltViewModel<SignUpViewModel>()
    val state by signUpViewModel.collectAsState()

    signUpViewModel.collectSideEffect { effect ->
      when (effect) {
        SignUpSideEffect.OnSubmitSuccessNavigate -> {
          navController.navigate(AppRoutes.SignIn) {
            popUpTo(AppRoutes.SignIn) { inclusive = true }
          }
        }

        is SignUpSideEffect.ShowToast -> {
          context.showToast(effect.message)
        }
      }
    }

    SignUpScreen(
      state = state,
      onNameChanged = signUpViewModel::onNameChanged,
      onEmailChanged = signUpViewModel::onEmailChanged,
      onPasswordChanged = signUpViewModel::onPasswordChanged,
      onSubmit = {
        signUpViewModel.onSubmit(context)
      },
      onBack = {
        navController.navigate(AppRoutes.Onboarding) {
          popUpTo(AppRoutes.Onboarding) { inclusive = true }
        }
      }
    )
  }
}

private fun NavGraphBuilder.addHome(navController: NavHostController) {
  composable<AppRoutes.Home> {
    val context = LocalContext.current

    val authViewModel = it.scopedViewModel<AuthViewModel>(navController)

    val signOutViewModel = hiltViewModel<SignOutViewModel>()
    val signOutState by signOutViewModel.collectAsState()

    val homeViewModel = it.scopedViewModel<HomeViewModel>(navController)
    val homeState by homeViewModel.collectAsState()

    signOutViewModel.collectSideEffect { effect ->
      when (effect) {
        SignOutSideEffect.OnSignOutNavigate -> {
          navController.navigate(AppRoutes.Onboarding) {
            popUpTo(AppRoutes.Onboarding) { inclusive = true }
          }
        }

        is SignOutSideEffect.ShowMessage -> {
          context.showToast(effect.message)
        }
      }
    }

    homeViewModel.collectSideEffect { effect ->
      when (effect) {
        is HomeSideEffect.OnStoryClickNavigate -> {
          navController.navigate(AppRoutes.DetailStory(effect.story))
        }
      }
    }

    HomeScreen(
      state = homeState,
      onRefresh = homeViewModel::refresh,
      onStoryClick = homeViewModel::onStoryClick,
      signOutState = signOutState,
      onSignOut = {
        signOutViewModel.signOut(context) {
          authViewModel.set(null)
        }
      }
    )
  }
}

private fun NavGraphBuilder.addStoryDetail(navController: NavHostController) {
  composable<AppRoutes.DetailStory>(
    typeMap = mapOf(
      typeOf<Story>() to navType<Story>(),
      typeOf<Story?>() to navType<Story?>()
    )
  ) {
    StoryDetailScreen(
      story = null,
      onBack = {
        navController.popBackStack()
      }
    )
  }
}
