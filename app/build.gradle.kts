@file:Suppress("UnstableApiUsage")

import com.android.build.api.dsl.ManagedVirtualDevice
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.dagger.hilt)
    alias(libs.plugins.google.services)
    alias(libs.plugins.crashlytics)
    id("jacoco")
}

apply(from = "${rootProject.rootDir}/gradle/jacoco.gradle.kts")

fun readProperties(path: String) = Properties().apply {
    this.load(FileInputStream(project.file(path)))
}

val versionMajor = property("version.major")?.toString()?.toIntOrNull() ?: 0
val versionMinor = property("version.minor")?.toString()?.toIntOrNull() ?: 0
val versionPatch = property("version.patch")?.toString()?.toIntOrNull() ?: 0
val versionBuild = property("version.build")?.toString()?.toIntOrNull() ?: 1
val isCoverageRequested = property("build.coverage")?.toString()?.toBooleanStrictOrNull() ?: false
val key: String = gradleLocalProperties(rootDir).getProperty("AGORA_APP_ID")
val agoraSelfHostedUrl: String = gradleLocalProperties(rootDir).getProperty("AGORA_SELF_HOSTED_URL")

android {
    namespace = "fr.deuspheara.callapp"
    compileSdk = 34

    signingConfigs {
        create("callapp-release") {
            val properties =
                readProperties("${rootProject.rootDir}/.signing/release/release.properties")
            this.storePassword = properties.getProperty("storePassword")
            this.keyPassword = properties.getProperty("keyPassword")
            this.keyAlias = properties.getProperty("keyAlias")
            this.storeFile = file("${rootProject.rootDir}/.signing/release/${properties.getProperty("storeFile")}")
        }
        create("callapp-debug") {
            val properties =
                readProperties("${rootProject.rootDir}/.signing/debug/debug.properties")
            this.storePassword = properties.getProperty("storePassword")
            this.keyPassword = properties.getProperty("keyPassword")
            this.keyAlias = properties.getProperty("keyAlias")
            this.storeFile = file("${rootProject.rootDir}/.signing/debug/${properties.getProperty("storeFile")}")
        }
    }

    defaultConfig {
        applicationId = "fr.deuspheara.callapp"
        minSdk = 26
        targetSdk = 34
        versionCode = versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        resourceConfigurations.add("fr")
        testInstrumentationRunner = "fr.deuspheara.callapp.CallAppRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            versionNameSuffix = "-debug+$versionBuild"
            isMinifyEnabled = false
            signingConfig = signingConfigs.getAt("callapp-debug")
            enableAndroidTestCoverage = isCoverageRequested
            manifestPlaceholders["firebase_crashlytics_collection_enabled"] = false
            buildConfigField("String", "AGORA_APP_ID", key)
            buildConfigField("String", "AGORA_SELF_HOSTED_URL", agoraSelfHostedUrl)
        }
        release {
            versionNameSuffix = "+$versionBuild"
            isMinifyEnabled = true
            isShrinkResources = true
            signingConfig = signingConfigs.getAt("callapp-release")
            manifestPlaceholders["firebase_crashlytics_collection_enabled"] = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-opt-in=" + "kotlinx.coroutines.ExperimentalCoroutinesApi," + "kotlin.contracts.ExperimentalContracts," + "kotlinx.coroutines.FlowPreview," + "androidx.compose.material3.ExperimentalMaterial3Api," + "androidx.compose.animation.ExperimentalAnimationApi," + "androidx.compose.ui.ExperimentalComposeUiApi," + "androidx.compose.animation.graphics.ExperimentalAnimationGraphicsApi," + "androidx.compose.foundation.ExperimentalFoundationApi"
        )
    }
    buildFeatures {
        compose = true
        buildConfig =  true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/gradle/incremental.annotation.processors"
            excludes += "META-INF/LICENSE.md"
            excludes += "META-INF/LICENSE-notice.md"
        }
    }

    testOptions {
        managedDevices {
            devices {
                maybeCreate<ManagedVirtualDevice>("pixel5Api30").apply {
                    device = "Pixel 5"
                    apiLevel = 30
                    systemImageSource = "google-atd"
                }
            }
        }
    }
}

kapt {
    correctErrorTypes = true
}


dependencies {
    //region Androidx
    implementation (libs.bundles.androidx)
    implementation(libs.androidx.appcompat)
    implementation (libs.bundles.coroutine)
    implementation(libs.androidx.compose.materialWindow)
    implementation(libs.google.android.material)
    //endregion

    //region Retrofit
    implementation(libs.bundles.networking)
    //endregion

    //region Hilt
    implementation (libs.bundles.hilt)
    kapt(libs.hilt.compiler)
    implementation (libs.androidx.recyclerview)
    androidTestImplementation (libs.hilt.testing)
    //endregion

    //region Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.compose)
    debugImplementation(libs.bundles.compose.debug)
    androidTestImplementation (libs.bundles.compose.test)
    //endregion

    //region Firebase
    implementation (platform(libs.firebase.bom))
    implementation (libs.bundles.firebase)
    //endregion

    // region Room
    implementation(libs.bundles.room)
    ksp(libs.room.compiler)
    androidTestImplementation (libs.room.testing)
    //endregion

    testImplementation (libs.junit)
    testImplementation (libs.coroutine.test)
    debugImplementation (libs.mockk)
    androidTestImplementation (libs.robolectric)

    androidTestImplementation (libs.androidx.junit)
    androidTestImplementation (libs.androidx.test.runner)
    androidTestImplementation (libs.hilt.testing)
    kaptAndroidTest (libs.hilt.compiler)

    // region Agora
    implementation(libs.agora)
    // endregion
}
