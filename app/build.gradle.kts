plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.rasadhana"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rasadhana"
        minSdk = 21
        this.targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField ("String", "API_KEY", "\"your_production_api_key_here\"")
            buildConfigField ("String", "BASE_URL", "\"your_production_base_url_here\"")
        }
        debug {
            buildConfigField ("String", "API_KEY", "\"your_debug_api_key_here\"")
            buildConfigField ("String", "BASE_URL", "\"https://be-rasadhana-245949327575.asia-southeast2.run.app/\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.legacy.support.v4)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    implementation(libs.androidx.activity.ktx)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

//    retrofit to networking api
    implementation(libs.retrofit)
    implementation(libs.logging.interceptor)
//    gson converter to parse json auto
    implementation (libs.converter.gson)
//    image load
    implementation (libs.glide)
    ksp(libs.glideCompiler)
//    room orm
    implementation(libs.androidx.room.runtime)
    ksp(libs.room.compiler)
//    coroutine support
    implementation(libs.androidx.room.ktx)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

//    dependency injection koin
//    When you update the BOM version, all the libraries koin that you're using are automatically updated to their new versions.
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.android)

//    work manager
    implementation(libs.androidx.work.runtime.ktx)

    // Import the Firebase BoM
    implementation(platform(libs.firebase.bom))

    // When using the BoM, don't specify versions in Firebase dependencies
    implementation(libs.firebase.analytics)

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    // otpview
    implementation (libs.otpview)

    // datastore
    implementation(libs.androidx.datastore.preferences)
}