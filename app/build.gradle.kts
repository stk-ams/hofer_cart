plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")

    // Add the Google services Gradle plugin

    id("com.google.gms.google-services")

}

android {
    namespace = "at.fhj.hofer_cart"
    compileSdk = 34

    defaultConfig {
        applicationId = "at.fhj.hofer_cart"
        minSdk = 28
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("com.google.firebase:firebase-database:20.0.4")
    implementation("com.google.firebase:firebase-messaging-ktx:23.1.1")
    implementation("com.google.firebase:firebase-messaging:23.1.1")

    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))




    implementation("com.google.firebase:firebase-analytics")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0")
    implementation("androidx.room:room-runtime:2.4.3")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")
    // annotationProcessor("androidx.room:room-compiler:2.4.3")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:2.4.3")
    // To use Kotlin Symbol Processing (KSP)
    //ksp("androidx.room:room-compiler:2.4.3")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:2.4.3")

    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:2.4.3")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:2.4.3")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:2.4.3")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:2.4.3")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:2.4.3")

    implementation("androidx.compose.material3:material3-android:1.2.0-alpha03")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}