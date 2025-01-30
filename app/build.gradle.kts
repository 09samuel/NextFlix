plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.compose.compiler)
    kotlin("plugin.serialization") version "2.0.0"
}

android {
    namespace = "com.nextflix.app"
    compileSdk = 34

    val key: String = com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir,providers).getProperty("supabaseKey")
    val url: String = com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir,providers).getProperty("supabaseUrl")
    val tmdb: String = com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir,providers).getProperty("tmdbKey")
    val googleClientId: String = com.android.build.gradle.internal.cxx.configure.gradleLocalProperties(rootDir,providers).getProperty("googleClientId")

    defaultConfig {
        applicationId = "com.nextflix.app"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String","supabaseKey","\"$key\"")
        buildConfigField("String","supabaseUrl","\"$url\"")
        buildConfigField("String","tmdbKey","\"$tmdb\"")
        buildConfigField("String","googleClientId","\"$googleClientId\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig=true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
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
    implementation(libs.androidx.ui.test.android)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Supabase
    implementation(libs.gotrue.kt)
    implementation(libs.ktor.client.cio)
    implementation(libs.postgrest.kt)


    // needed fot google auth
    implementation("androidx.credentials:credentials:1.3.0")

    // optional - needed for credentials support from play services, for devices running
    // Android 13 and below.
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation ("com.google.android.libraries.identity.googleid:googleid:1.1.0")

    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Room
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)

    // Dagger - Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    ksp(libs.androidx.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Coil
    implementation(libs.coil.compose)

    // Extended Icons
    implementation(libs.androidx.material.icons.extended)

    // system UI Controller
    implementation(libs.accompanist.systemuicontroller)

    implementation ("com.valentinilk.shimmer:compose-shimmer:1.3.1")

}