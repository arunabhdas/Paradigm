import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.realm.plugin)
    alias(libs.plugins.kotlinxSerialization)
    alias(libs.plugins.sqlDelight)
    alias(libs.plugins.room)
    alias(libs.plugins.ksp)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    sourceSets {
        iosMain.dependencies {
            implementation(libs.sqldelight.ios)
            implementation(libs.ktor.client.darwin)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)

            implementation(libs.sqldelight.android)
            implementation(libs.ktor.client.android)

            implementation(libs.koin.android)

            implementation(libs.timber)  // Add Timber for logging
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.koin.core)
            implementation(libs.voyager.navigator)
            implementation(libs.voyager.tabNavigator)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.screenModel)
            implementation(libs.voyager.transitions)
            implementation(libs.voyager.koin)

            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)

            implementation(libs.kotlinx.datetime)

            implementation(libs.multiplatform.settings.no.arg)
            implementation(libs.multiplatform.settings.coroutines)

            implementation(libs.mongodb.realm)
            implementation(libs.kotlin.coroutines)
            implementation(libs.stately.common)

            implementation(libs.landscapist.coil3)
            implementation(libs.room.runtime)
            implementation(libs.sqlite.bundled)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

        }

        iosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

android {
    namespace = "app.paradigmatic.paradigmaticapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "app.paradigmatic.paradigmaticapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        // Debug: Print current directory
        println("Current directory: ${project.projectDir}")

        val localProperties = Properties().apply {
            val localPropertiesFile = rootProject.file("local.properties")
            println("Looking for local.properties at: ${localPropertiesFile.absolutePath}")
            if (localPropertiesFile.exists()) {
                load(FileInputStream(localPropertiesFile))
                println("Properties loaded. Available keys: ${propertyNames().toList()}")
            }
        }

        val apiKey = localProperties.getProperty("CURRENCY_API_KEY") ?: ""
        println("API Key found: ${apiKey.isNotBlank()}")

        if (apiKey.isBlank()) {
            throw GradleException(
                """
                Missing CURRENCY_API_KEY in local.properties
                Please add: CURRENCY_API_KEY=your_api_key_here
                to your local.properties file at:
                ${rootProject.file("local.properties").absolutePath}
            """.trimIndent()
            )
        }
        buildConfigField("String", "CURRENCY_API_KEY", "\"$apiKey\"")
    }
    buildFeatures {
        compose = true
        buildConfig = true  // Enable BuildConfig generation
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.ui.text.google.fonts)
    implementation(libs.androidx.material3.android)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.runtime.android)
    implementation(libs.androidx.foundation.layout.android)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.foundation.android)
    // Lifecycle dependencies from version catalog
    implementation(libs.androidx.lifecycle.runtime.compose)

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.koin.core)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)

    implementation(libs.kotlinx.datetime)

    implementation(libs.multiplatform.settings)
    implementation(libs.multiplatform.settings.no.arg)

    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.foundation.layout.android)
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.foundation.android)
    implementation(libs.androidx.animation.android)
    implementation(libs.androidx.animation.core.android)
    implementation(libs.ui.android)
    implementation(libs.androidx.room.runtime.android)
    implementation(libs.firebase.crashlytics.buildtools)
    implementation(libs.androidx.lifecycle.viewmodel.android)
    debugImplementation(compose.uiTooling)
}

sqldelight {
    databases {
        create("ParadigmaticDatabase") {
            packageName.set("app.paradigmatic.paradigmaticapp")
        }
    }
}