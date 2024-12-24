import java.util.Properties

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.hilt.android)
  alias(libs.plugins.ksp)
  id("kotlin-parcelize")
}

val keystoreFile = rootProject.file("app/secrets.properties")
val properties = Properties()
if (keystoreFile.exists()) {
  keystoreFile.inputStream().use {
    properties.load(it)
  }
}

android {
  namespace = "com.dicoding.stories"
  compileSdk = 35

  defaultConfig {
    applicationId = "com.dicoding.stories"
    minSdk = 26
    //noinspection OldTargetApi
    targetSdk = 34
    versionCode = 1
    versionName = "1.0"

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    fun loadConfig(type: String, key: String, defaultValue: String) {
      val value = properties.getProperty(key) ?: defaultValue
      manifestPlaceholders[key] = value
      buildConfigField(type, key, "\"$value\"")
    }

    loadConfig("String", "DICODING_EVENTS_API_BASE_URL", "")

    resourceConfigurations.plus(listOf("en", "in"))
  }

  buildTypes {
    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }
  kotlinOptions {
    jvmTarget = JavaVersion.VERSION_11.toString()
  }
  buildFeatures {
    compose = true
    buildConfig = true
  }
  androidResources {
    @Suppress("UnstableApiUsage")
    generateLocaleConfig = true
  }
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(platform(libs.androidx.compose.bom))
  implementation(libs.androidx.activity.compose)
  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.ui)
  implementation(libs.androidx.ui.graphics)
  implementation(libs.androidx.ui.tooling.preview)
  implementation(libs.androidx.material3)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.junit)
  androidTestImplementation(libs.androidx.espresso.core)
  androidTestImplementation(platform(libs.androidx.compose.bom))
  androidTestImplementation(libs.androidx.ui.test.junit4)
  debugImplementation(libs.androidx.ui.tooling)
  debugImplementation(libs.androidx.ui.test.manifest)
  //  additional jdk core lib
  coreLibraryDesugaring(libs.android.desugar.jdk)
  //  androidx additional libs
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.ui.text.google.fonts)
  implementation(libs.androidx.material3.iconsExtended)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.appcompat.resources)
  implementation(libs.androidx.datastore.preferences)
  implementation(libs.androidx.room.runtime)
  ksp(libs.androidx.room.compiler)
  implementation(libs.androidx.room)
  implementation(libs.androidx.work.runtime.ktx)
  //  additional kotlinx libs
  implementation(libs.kotlinx.serialization.json)
  implementation(libs.kotlinx.datetime)
  //  dependency injection
  implementation(libs.hilt)
  ksp(libs.hilt.compiler)
  implementation(libs.hilt.navigation.compose)
  implementation(libs.hilt.work)
  //  networking
  implementation(platform(libs.okhttp.bom))
  implementation(libs.okhttp)
  implementation(libs.okhttp.loggingInterceptor)
  implementation(libs.retrofit)
  implementation(libs.retrofit.serialization)
  implementation(libs.sandwich)
  implementation(libs.sandwich.retrofit)
  implementation(libs.sandwich.retrofit.serialization)
  //  network image resource
  implementation(libs.coil.compose)
  implementation(libs.coil.network.okhttp)
  implementation(libs.landscapist.coil)
  //  orbit mvi
  implementation(libs.orbitMvi.core)
  implementation(libs.orbitMvi.viewmodel)
  implementation(libs.orbitMvi.compose)
  implementation(libs.orbitMvi.test)
  //  accompanist
  implementation(libs.accompanist.permission)
}
