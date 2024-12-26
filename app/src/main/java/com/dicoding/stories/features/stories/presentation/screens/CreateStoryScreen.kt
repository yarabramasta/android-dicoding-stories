@file:Suppress("UNUSED_ANONYMOUS_PARAMETER")

package com.dicoding.stories.features.stories.presentation.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil3.compose.SubcomposeAsyncImage
import com.composables.core.DragIndication
import com.composables.core.Scrim
import com.composables.core.Sheet
import com.composables.core.SheetDetent.Companion.FullyExpanded
import com.composables.core.SheetDetent.Companion.Hidden
import com.dicoding.stories.BuildConfig
import com.dicoding.stories.R
import com.dicoding.stories.features.locations.helper.LocationUtil
import com.dicoding.stories.features.stories.presentation.viewmodel.create.CreateStoryState
import com.dicoding.stories.shared.ui.composables.EditText
import com.dicoding.stories.shared.ui.composables.ShimmerBox
import com.dicoding.stories.shared.ui.lib.*
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun CreateStoryScreen(
  state: CreateStoryState,
  onDescriptionChanged: (String) -> Unit = {},
  onImageUriChanged: (Uri?, Bitmap?) -> Unit = { uri, bitmap -> },
  onLocationChanged: (LatLng?) -> Unit = {},
  onUpload: (Context) -> Unit = {},
  onClear: () -> Unit = {},
  onBack: () -> Unit = {},
) {
  val isKeyboardOpen by keyboardAsState()
  BackHandler(enabled = !isKeyboardOpen) { onBack() }

  val context = LocalContext.current
  var imageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }
  var imageBitmap by remember { mutableStateOf<Bitmap?>(null) }

  val cameraTargetfile = ImageUtils.createImageFile(context)
  val cameraTargetUri = FileProvider.getUriForFile(
    context,
    BuildConfig.APPLICATION_ID + ".fileProvider",
    cameraTargetfile
  )

  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
  ) { success ->
    if (success) {
      imageUri = cameraTargetUri
      val source = ImageDecoder.createSource(
        context.contentResolver, cameraTargetUri
      )
      imageBitmap = ImageDecoder.decodeBitmap(source)
    }
    if ((imageUri?.toString() ?: "").isNotEmpty()) {
      onImageUriChanged(imageUri, imageBitmap)
    } else {
      imageUri = Uri.EMPTY
      imageBitmap = null
      onImageUriChanged(Uri.EMPTY, null)
      context.showToast(
        context.getString(R.string.err_camera_interrupted)
      )
    }
  }

  val cameraPermissionState = rememberPermissionState(
    permission = Manifest.permission.CAMERA,
  ) { hasPermission ->
    if (hasPermission) {
      cameraLauncher.launch(cameraTargetUri)
    }
  }

  val galleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.PickVisualMedia()
  ) { uri ->
    if (uri != null) {
      imageUri = uri
      val source = ImageDecoder.createSource(
        context.contentResolver, uri
      )
      imageBitmap = ImageDecoder.decodeBitmap(source)
    }

    if ((imageUri?.toString() ?: "").isNotEmpty()) {
      onImageUriChanged(imageUri, imageBitmap)
    } else {
      imageUri = Uri.EMPTY
      imageBitmap = null
      onImageUriChanged(Uri.EMPTY, null)
      context.showToast(
        context.getString(R.string.err_pick_from_gallery_interrupted)
      )
    }
  }

  val mediaPermissionState = rememberMultiplePermissionsState(
    permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      listOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
      listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      )
    }
  ) { map ->
    if (map.all { it.value }) {
      galleryLauncher.launch(
        PickVisualMediaRequest(
          ActivityResultContracts.PickVisualMedia.ImageOnly
        )
      )
    }
  }

  val hasCameraPermission = cameraPermissionState.status.isGranted
  val hasMediaPermission = mediaPermissionState.allPermissionsGranted

  CreateStoryScreenScaffold(
    state = state,
    onBack = onBack,
    onUpload = {
      onUpload(context)
    },
    onClear = {
      imageUri = Uri.EMPTY
      onClear()
    },
    onCameraTileClick = {
      if (hasCameraPermission) {
        cameraLauncher.launch(cameraTargetUri)
      } else {
        cameraPermissionState.launchPermissionRequest()
      }
    },
    onGalleryTileClick = {
      if (hasMediaPermission) {
        galleryLauncher.launch(
          PickVisualMediaRequest(
            ActivityResultContracts.PickVisualMedia.ImageOnly
          )
        )
      } else {
        mediaPermissionState.launchMultiplePermissionRequest()
      }
    }
  ) { modifier, showBottomSheet ->
    CreateStoryScreenContent(
      modifier = modifier,
      state = state,
      onImagePickerCardClick = {
        showBottomSheet()
      },
      onDescriptionChanged = onDescriptionChanged,
      onImageUriChanged = onImageUriChanged,
      onLocationChanged = onLocationChanged
    )
  }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateStoryScreenScaffold(
  state: CreateStoryState,
  sheetDetent: com.composables.core.SheetDetent = Hidden,
  onBack: () -> Unit = {},
  onUpload: () -> Unit = {},
  onClear: () -> Unit = {},
  onCameraTileClick: () -> Unit = {},
  onGalleryTileClick: () -> Unit = {},
  content: @Composable (
    modifier: Modifier,
    showBottomSheet: () -> Unit,
  ) -> Unit,
) {
  val sheetState = com.composables.core.rememberModalBottomSheetState(
    initialDetent = sheetDetent,
  )

  Scaffold(
    modifier = Modifier.fillMaxSize(),
    topBar = {
      TopAppBar(
        title = {
          Text(
            text = "Upload Story",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
          )
        },
        navigationIcon = {
          IconButton(
            onClick = onBack,
            enabled = state.status !is UiStatus.Loading
          ) {
            Icon(
              imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
              contentDescription = "Back to Home"
            )
          }
        }
      )
    },
    bottomBar = {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .background(MaterialTheme.colorScheme.background)
          .padding(24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
      ) {
        Column(
          modifier = Modifier.fillMaxWidth(),
          verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
          Button(
            modifier = Modifier.fillMaxWidth(),
            enabled = state.status !is UiStatus.Loading,
            onClick = onUpload
          ) {
            Text(text = "Upload")
          }
          if ((state.imageUri?.toString() ?: "").isNotEmpty()) {
            OutlinedButton(
              modifier = Modifier.fillMaxWidth(),
              enabled = state.status !is UiStatus.Loading,
              onClick = onClear
            ) {
              Text(text = "Clear")
            }
          }
        }
      }
    }
  ) { innerPadding ->
    content(Modifier.padding(innerPadding)) {
      sheetState.currentDetent = FullyExpanded
    }
    com.composables.core.ModalBottomSheet(state = sheetState) {
      Scrim(
        scrimColor = Color.Black.copy(0.5f),
        enter = fadeIn(),
        exit = fadeOut()
      )
      Sheet(
        modifier = Modifier
          .padding(top = 12.dp)
          .displayCutoutPadding()
          .statusBarsPadding()
          .padding(
            WindowInsets
              .navigationBars
              .only(WindowInsetsSides.Horizontal)
              .asPaddingValues()
          )
          .shadow(4.dp, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
          .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
          .background(MaterialTheme.colorScheme.background)
          .fillMaxWidth()
          .imePadding(),
      ) {
        Column(
          modifier = Modifier.height(200.dp),
          horizontalAlignment = Alignment.CenterHorizontally
        ) {
          DragIndication(
            modifier = Modifier
              .padding(top = 24.dp)
              .background(
                MaterialTheme.colorScheme.outline.copy(0.4f),
                RoundedCornerShape(100)
              )
              .width(32.dp)
              .height(4.dp)
          )
          Spacer(modifier = Modifier.height(16.dp))
          ListItem(
            modifier = Modifier.clickable {
              onCameraTileClick()
              sheetState.currentDetent = Hidden
            },
            leadingContent = {
              Icon(
                imageVector = Icons.Outlined.CameraAlt,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
              )
            },
            headlineContent = {
              Text(
                text = stringResource(R.string.capture_from_camera),
              )
            },
          )
          ListItem(
            modifier = Modifier.clickable {
              onGalleryTileClick()
              sheetState.currentDetent = Hidden
            },
            leadingContent = {
              Icon(
                imageVector = Icons.Outlined.Image,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
              )
            },
            headlineContent = {
              Text(
                text = stringResource(R.string.pick_from_gallery),
              )
            },
          )
        }
      }
    }
  }
}

@Composable
private fun CreateStoryScreenContent(
  modifier: Modifier = Modifier,
  state: CreateStoryState,
  onImagePickerCardClick: () -> Unit = {},
  onDescriptionChanged: (String) -> Unit = {},
  onImageUriChanged: (Uri?, Bitmap?) -> Unit = { uri, bitmap -> },
  onLocationChanged: (LatLng?) -> Unit = {},
) {
  LazyColumn(
    modifier = modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(16.dp),
    contentPadding = PaddingValues(24.dp)
  ) {
    item {
      if ((state.imageUri?.toString() ?: "").isNotEmpty()) {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .height(360.dp),
          onClick = onImagePickerCardClick,
          enabled = state.status !is UiStatus.Loading
        ) {
          SubcomposeAsyncImage(
            model = state.imageUri?.toString() ?: "",
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            loading = {
              ShimmerBox(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(360.dp),
                rounded = false
              )
            },
            error = {
              ShimmerBox(
                modifier = Modifier
                  .fillMaxWidth()
                  .height(360.dp),
                animate = false,
                rounded = false,
              )
            },
            modifier = Modifier
              .fillMaxWidth()
              .height(360.dp)
          )
        }
      } else {
        OutlinedCard(
          modifier = Modifier
            .fillMaxWidth()
            .height(360.dp),
          onClick = onImagePickerCardClick,
          border = BorderStroke(
            1.dp,
            if (state.imageError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
          ),
          enabled = state.status !is UiStatus.Loading
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .height(360.dp),
            verticalArrangement = Arrangement.spacedBy(
              space = 8.dp,
              alignment = Alignment.CenterVertically
            ),
            horizontalAlignment = Alignment.CenterHorizontally
          ) {
            Icon(
              imageVector = Icons.Outlined.Image,
              contentDescription = null,
              modifier = Modifier.size(32.dp),
              tint = if (state.imageError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
            )
            Text(
              text = stringResource(R.string.pick_picture_description),
              style = MaterialTheme.typography.bodySmall,
              color = if (state.imageError != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline,
              textAlign = TextAlign.Center
            )
          }
        }
      }
    }
    item {
      Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Icon(
          imageVector = Icons.Outlined.Info,
          contentDescription = null,
          tint = MaterialTheme.colorScheme.outline,
          modifier = Modifier.size(16.dp)
        )
        Text(
          text = stringResource(R.string.form_image_max_size_info),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.outline
        )
      }
    }
    if (state.imageError != null) {
      item {
        Text(
          text = state.imageError.asString(),
          style = MaterialTheme.typography.labelMedium,
          color = MaterialTheme.colorScheme.error
        )
      }
    }
    item {
      LocationTrackingSwitch(
        state = state,
        onLocationChanged = { location ->
          Log.d("CreateStoryScreen", "User Location: $location")
          onLocationChanged(location)
        }
      )
    }
    item {
      val keyboardController = LocalSoftwareKeyboardController.current
      EditText(
        label = stringResource(R.string.form_label_description),
        value = state.description,
        onValueChange = onDescriptionChanged,
        enabled = state.status !is UiStatus.Loading,
        modifier = Modifier.fillMaxWidth(),
        minLines = 5,
        maxLines = 8,
        singleLine = false,
        isError = state.descriptionError != null,
        errorMessage = state.descriptionError,
        keyboardActions = KeyboardActions { keyboardController?.hide() },
        keyboardOptions = KeyboardOptions(
          keyboardType = KeyboardType.Text,
          imeAction = ImeAction.Done
        ),
      )
    }
  }
}

lateinit var locationCallback: LocationCallback
lateinit var locationProvider: FusedLocationProviderClient

@SuppressLint("MissingPermission")
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun LocationTrackingSwitch(
  state: CreateStoryState,
  onLocationChanged: (LatLng) -> Unit = {},
) {
  val context = LocalContext.current

  locationProvider = LocationServices.getFusedLocationProviderClient(context)

  val locationPermissionState = rememberMultiplePermissionsState(
    permissions = listOf(
      Manifest.permission.ACCESS_FINE_LOCATION,
      Manifest.permission.ACCESS_COARSE_LOCATION
    )
  )
  val hasLocationPermission = locationPermissionState.allPermissionsGranted

  var currentUserLocation by remember { mutableStateOf<LatLng?>(null) }
  var enabled by remember { mutableStateOf(false) }

  val address = currentUserLocation?.let {
    LocationUtil.getReadableLocation(context, it.latitude, it.longitude)
  }

  DisposableEffect(key1 = locationProvider) {
    locationCallback = object : LocationCallback() {
      override fun onLocationResult(result: LocationResult) {
        locationProvider.lastLocation
          .addOnSuccessListener { location ->
            Log.d("CreateStoryScreen", "User Location: $location")
            location?.let {
              currentUserLocation = LatLng(it.latitude, it.longitude)
              onLocationChanged(currentUserLocation!!)
            }
          }
          .addOnFailureListener { exception ->
            context.showToast(
              context.getString(
                R.string.err_failed_to_get_location,
                exception.message ?: "Unknown"
              )
            )
          }
      }
    }

    if (hasLocationPermission) {
      if (enabled) {
        LocationUtil.locationUpdate(
          provider = locationProvider,
          callback = locationCallback,
        )
      }
    } else {
      locationPermissionState.launchMultiplePermissionRequest()
    }

    onDispose {
      LocationUtil.stopLocationUpdate(
        provider = locationProvider,
        callback = locationCallback,
      )
    }
  }

  Column(
    modifier = Modifier.fillMaxWidth(),
    verticalArrangement = Arrangement.spacedBy(8.dp)
  ) {
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Text(
        text = stringResource(R.string.form_location_switch_label),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.SemiBold
      )
      Switch(
        checked = enabled,
        onCheckedChange = {

          if (LocationUtil.locationServiceIsActive(context)) {
            enabled = it
          } else {
            context.showToast(
              context.getString(
                R.string.err_form_location_switch_need_location_service
              )
            )
          }
        },
        enabled = state.status !is UiStatus.Loading,
      )
    }
    Row(
      modifier = Modifier.fillMaxWidth(),
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Icon(
        imageVector = Icons.Outlined.LocationOn,
        contentDescription = null,
        tint = MaterialTheme.colorScheme.outline,
        modifier = Modifier.size(16.dp)
      )
      Text(
        text = when {
          !enabled -> stringResource(R.string.form_location_switch_not_enabled)
          address == null -> stringResource(R.string.form_location_switch_loading)
          else -> stringResource(
            R.string.form_location_switch_success,
            address
          )
        },
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.outline
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateStoryScreenPreview() {
  val state = CreateStoryState.initial()

  DicodingStoriesTheme {
    CreateStoryScreenScaffold(state = state) { modifier, _ ->
      CreateStoryScreenContent(
        modifier = modifier,
        state = state,
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateStoryScreenRevealBottomSheetPreview() {
  val state = CreateStoryState.initial()

  DicodingStoriesTheme {
    CreateStoryScreenScaffold(
      state = state,
      sheetDetent = FullyExpanded
    ) { modifier, _ ->
      CreateStoryScreenContent(
        modifier = modifier,
        state = state,
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateStoryScreenHasImagePreview() {
  val state = CreateStoryState
    .initial()
    .copy(imageUri = Uri.parse("https://placehold.co/1080.png"))

  DicodingStoriesTheme {
    CreateStoryScreenScaffold(state = state) { modifier, _ ->
      CreateStoryScreenContent(
        modifier = modifier,
        state = state,
      )
    }
  }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateStoryFormErrorPreview() {
  val state = CreateStoryState
    .initial()
    .copy(
      imageError = UiText.StringResource(R.string.err_form_image_required),
      descriptionError = UiText.StringResource(R.string.err_form_description_empty)
    )

  DicodingStoriesTheme {
    CreateStoryScreenScaffold(state = state) { modifier, _ ->
      CreateStoryScreenContent(
        modifier = modifier,
        state = state,
      )
    }
  }
}
