import java.util.Properties

val keystoreFile = rootProject.file("app/secrets.properties")
val properties = Properties()
if (keystoreFile.exists()) {
  keystoreFile.inputStream().use {
    properties.load(it)
  }
}

plugins {
  alias(libs.plugins.android.application)
  alias(libs.plugins.kotlin.android)
  alias(libs.plugins.kotlin.compose)
  alias(libs.plugins.kotlin.serialization)
  alias(libs.plugins.ksp)
  alias(libs.plugins.ktorfit)
}

android {
  namespace = "com.dicoding.stories"
  compileSdk = 34

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
  }

  buildTypes {
    release {
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
}

dependencies {

  implementation(libs.androidx.core.ktx)
  implementation(libs.androidx.lifecycle.runtime.ktx)
  implementation(libs.androidx.activity.compose)
  implementation(platform(libs.androidx.compose.bom))
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
  //  androidx additional libs
  implementation(libs.androidx.lifecycle.runtime.compose)
  implementation(libs.androidx.lifecycle.viewmodel.compose)
  implementation(libs.androidx.ui.text.google.fonts)
  implementation(libs.androidx.material3.icons.extended)
  implementation(libs.androidx.appcompat)
  implementation(libs.androidx.appcompat.resources)
  implementation(libs.androidx.datastore.preferences)
  //  koin dependency injection
  implementation(libs.koin.android)
  implementation(libs.koin.test)
  implementation(libs.koin.annotations)
  ksp(libs.koin.ksp)
  //  networking
  implementation(platform(libs.okhttp.bom))
  implementation(libs.okhttp)
  implementation(libs.okhttp.loggingInterceptor)
  implementation(libs.ktor.client.okhttp)
  implementation(libs.ktor.client.serialization)
  implementation(libs.ktor.client.content.negotiation)
  implementation(libs.ktor.serialization)
  implementation(libs.ktorfit.lib)
  implementation(libs.ktorfit.converter.responses)
  implementation(libs.ktorfit.converter.call)
  implementation(libs.ktorfit.converter.flow)
  implementation(libs.sandwich)
  implementation(libs.sandwich.ktorfit)
  //  network image resource
  implementation(libs.coil.compose)
  implementation(libs.coil.network.okhttp)
  implementation(libs.landscapist.coil)
  //  orbit mvi
  implementation(libs.orbitMvi.core)
  implementation(libs.orbitMvi.viewmodel)
  implementation(libs.orbitMvi.compose)
  implementation(libs.orbitMvi.test)
}
