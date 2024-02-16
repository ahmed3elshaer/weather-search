@Suppress("DSL_SCOPE_VIOLATION") // Remove when fixed https://youtrack.jetbrains.com/issue/KTIJ-19369
plugins {
	alias(libs.plugins.android.application)
	alias(libs.plugins.kotlin.android)
	alias(libs.plugins.kotlin.kapt)
	alias(libs.plugins.hilt.gradle)
	alias(libs.plugins.ksp)
}

android {
	namespace = "com.ahmed3elshaer.weather"
	compileSdk = 34

	defaultConfig {
		applicationId = "com.ahmed3elshaer.weather"
		minSdk = 21
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "com.ahmed3elshaer.weather.HiltTestRunner"
		vectorDrawables {
			useSupportLibrary = true
		}

		// Enable room auto-migrations
		ksp {
			arg("room.schemaLocation", "$projectDir/schemas")
		}
	}

	buildTypes {
		getByName("release") {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_17
		targetCompatibility = JavaVersion.VERSION_17
	}

	kotlinOptions {
		jvmTarget = "17"
	}

	buildFeatures {
		compose = true
		aidl = false
		buildConfig = false
		renderScript = false
		shaders = false
	}

	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.androidxComposeCompiler.get()
	}

	packaging {
		resources {
			excludes += "/META-INF/{AL2.0,LGPL2.1}"
		}
	}
}

dependencies {

	val composeBom = platform(libs.androidx.compose.bom)
	implementation(composeBom)
	androidTestImplementation(composeBom)

	// Core Android dependencies
	implementation(libs.androidx.core.ktx)
	implementation(libs.androidx.lifecycle.runtime.ktx)
	implementation(libs.androidx.activity.compose)


	// Hilt Dependency Injection
	implementation(libs.hilt.android)
	kapt(libs.hilt.compiler)
	// Hilt and instrumented tests.
	androidTestImplementation(libs.hilt.android.testing)
	kaptAndroidTest(libs.hilt.android.compiler)
	// Hilt and Robolectric tests.
	testImplementation(libs.hilt.android.testing)
	kaptTest(libs.hilt.android.compiler)

	// Arch Components
	implementation(libs.androidx.lifecycle.runtime.compose)
	implementation(libs.androidx.lifecycle.viewmodel.compose)
	implementation(libs.androidx.navigation.compose)
	implementation(libs.androidx.hilt.navigation.compose)

	// Compose
	implementation(libs.androidx.compose.ui)
	implementation(libs.androidx.compose.runtime)
	implementation(libs.androidx.compose.ui.tooling.preview)
	implementation(libs.androidx.compose.material3)
	implementation(libs.androidx.compose.material.icons)
	implementation(libs.androidx.compose.material.icons.extended)
	implementation(libs.accompanist.permissions)
	// Retrofit
	implementation(libs.okhttp.core)
	implementation(libs.okhttp.interceptor)
	implementation(libs.retrofit.core)
	implementation(libs.retrofit.gson)

	// Location
	implementation(libs.play.services.location)
	implementation(libs.kotlinx.coroutines.play.services)


	// Tooling
	debugImplementation(libs.androidx.compose.ui.tooling)
	// Instrumented tests
	androidTestImplementation(libs.androidx.compose.ui.test.junit4)
	debugImplementation(libs.androidx.compose.ui.test.manifest)

	// Local tests: jUnit, coroutines, Android runner
	testImplementation(libs.junit)
	testImplementation(libs.kotlinx.coroutines.test)
	testImplementation(libs.mockito.kotlin)
	testImplementation(libs.truth)
	testImplementation(libs.mockk)
	testImplementation(libs.turbine)
	testImplementation(libs.androidx.core.testing)


	// Instrumented tests: jUnit rules and runners

	androidTestImplementation(libs.androidx.test.core)
	androidTestImplementation(libs.androidx.test.ext.junit)
	androidTestImplementation(libs.androidx.test.runner)
}
