package com.dicoding.stories.features.stories.presentation.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.dicoding.stories.BuildConfig
import com.dicoding.stories.R
import com.dicoding.stories.features.stories.presentation.viewmodel.create.CreateStoryState
import com.dicoding.stories.shared.lib.utils.validateFileSize
import com.dicoding.stories.shared.ui.composables.EditText
import com.dicoding.stories.shared.ui.composables.ShimmerBox
import com.dicoding.stories.shared.ui.lib.*
import com.dicoding.stories.shared.ui.theme.DicodingStoriesTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.launch


@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CreateStoryScreen(
  state: CreateStoryState,
  onDescriptionChanged: (String) -> Unit = {},
  onImageUriChanged: (Uri?) -> Unit = {},
  onBack: () -> Unit = {},
  onUpload: () -> Unit = {},
  onClear: () -> Unit = {},
) {
  val context = LocalContext.current

  var imageUri by remember { mutableStateOf<Uri?>(Uri.EMPTY) }

  val cameraPermissionState = rememberPermissionState(
    permission = Manifest.permission.CAMERA,
  )
  val mediaPermissionState = rememberMultiplePermissionsState(
    permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
      listOf(Manifest.permission.READ_MEDIA_IMAGES)
    } else {
      listOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
      )
    }
  )

  val file = context.createImageFile()
  val uri = FileProvider.getUriForFile(
    context,
    BuildConfig.APPLICATION_ID + ".fileProvider",
    file
  )

  val cameraLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.TakePicture()
  ) { success ->
    if (success) imageUri = uri
    if (imageUri.toString().isNotEmpty()) {
      Log.d("CreateStoryScreen", "Image URI from camera: $imageUri")
      if (validateFileSize(imageUri.toString(), 1)) {
        onImageUriChanged(imageUri)
      } else {
        context.showToast(context.getString(R.string.err_form_image_too_large))
      }
    } else {
      context.showToast(
        context.getString(R.string.err_camera_interrupted)
      )
    }
  }

  val galleryLauncher = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.StartActivityForResult()
  ) {
    if (it.resultCode == Activity.RESULT_OK) {
      val data: Intent? = it.data
      if (data != null) {
        imageUri = Uri.parse(data.data.toString())

        if (imageUri.toString().isNotEmpty()) {
          Log.d("CreateStoryScreen", "Image URI from gallery: $imageUri")
          if (validateFileSize(imageUri.toString(), 1)) {
            onImageUriChanged(imageUri)
          } else {
            context.showToast(context.getString(R.string.err_form_image_too_large))
          }
        } else {
          context.showToast(
            context.getString(R.string.err_pick_from_gallery_interrupted)
          )
        }
      }
    }
  }

  val hasCameraPermission = cameraPermissionState.status.isGranted
  val hasMediaPermission = mediaPermissionState.allPermissionsGranted

  val sheetState = rememberModalBottomSheetState()
  val scope = rememberCoroutineScope()
  var showBottomSheet by remember { mutableStateOf(false) }

  CreateStoryScreenContent(
    state = state,
    sheetState = sheetState,
    onBottomSheetActionClick = {
      scope.launch { sheetState.hide() }.invokeOnCompletion {
        if (!sheetState.isVisible) {
          showBottomSheet = false
        }
      }
    },
    onDismissBottomSheet = {
      showBottomSheet = false
    },
    onBack = onBack,
    onUpload = onUpload,
    onClear = {
      imageUri = Uri.EMPTY
      onClear()
    },
    onDescriptionChanged = onDescriptionChanged,
    onImageUriChanged = onImageUriChanged,
    onCameraTileClick = {
      if (hasCameraPermission) {
        cameraLauncher.launch(uri)
      } else {
        cameraPermissionState.launchPermissionRequest()
      }
    },
    onGalleryTileClick = {
      if (hasMediaPermission) {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        galleryLauncher.launch(intent)
      } else {
        mediaPermissionState.launchMultiplePermissionRequest()
      }
    }
  )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateStoryScreenContent(
  state: CreateStoryState,
  sheetState: SheetState,
  showBottomSheet: Boolean = false,
  onBottomSheetActionClick: () -> Unit = {},
  onDismissBottomSheet: () -> Unit = {},
  onBack: () -> Unit = {},
  onUpload: () -> Unit = {},
  onClear: () -> Unit = {},
  onDescriptionChanged: (String) -> Unit = {},
  onImageUriChanged: (Uri?) -> Unit = {},
  onCameraTileClick: () -> Unit = {},
  onGalleryTileClick: () -> Unit = {},
) {
  val isKeyboardOpen by keyboardAsState()
  BackHandler(enabled = !isKeyboardOpen) {
    onBack()
  }

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
          if ((state.image?.toString() ?: "").isNotEmpty()) {
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
    if (showBottomSheet) {
      ModalBottomSheet(
        onDismissRequest = onDismissBottomSheet,
        sheetState = sheetState
      ) {
        Column {
          ListItem(
            modifier = Modifier.clickable { onCameraTileClick() },
            headlineContent = {
              Text(
                text = stringResource(R.string.capture_from_camera),
                style = MaterialTheme.typography.labelMedium
              )
            },
          )
          ListItem(
            modifier = Modifier.clickable { onGalleryTileClick() },
            headlineContent = {
              Text(
                text = stringResource(R.string.pick_from_gallery),
                style = MaterialTheme.typography.labelMedium
              )
            },
          )
        }
      }
    }
    LazyColumn(
      modifier = Modifier
        .fillMaxSize()
        .padding(innerPadding),
      verticalArrangement = Arrangement.spacedBy(16.dp),
      contentPadding = PaddingValues(24.dp)
    ) {
      item {
        if ((state.image?.toString() ?: "").isNotEmpty()) {
          Card(
            modifier = Modifier
              .fillMaxWidth()
              .height(360.dp),
            onClick = {},
            enabled = state.status !is UiStatus.Loading
          ) {
            SubcomposeAsyncImage(
              model = state.image?.toString() ?: "",
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
            onClick = {},
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
                tint = MaterialTheme.colorScheme.outline,
              )
              Text(
                text = stringResource(R.string.pick_picture_description),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateStoryScreenPreview() {
  DicodingStoriesTheme {
    CreateStoryScreenContent(
      state = CreateStoryState.initial(),
      sheetState = rememberModalBottomSheetState(),
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateStoryScreenRevealBottomSheetPreview() {
  DicodingStoriesTheme {
    CreateStoryScreenContent(
      state = CreateStoryState.initial(),
      sheetState = rememberModalBottomSheetState(),
      showBottomSheet = true,
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateStoryScreenHasImagePreview() {
  DicodingStoriesTheme {
    CreateStoryScreenContent(
      state = CreateStoryState
        .initial()
        .copy(image = Uri.parse("https://placehold.co/1080.png")),
      sheetState = rememberModalBottomSheetState()
    )
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun CreateStoryFormErrorPreview() {
  DicodingStoriesTheme {
    CreateStoryScreenContent(
      state = CreateStoryState
        .initial()
        .copy(
          imageError = UiText.StringResource(R.string.err_form_image_required),
          descriptionError = UiText.StringResource(R.string.err_form_description_empty)
        ),
      sheetState = rememberModalBottomSheetState()
    )
  }
}


