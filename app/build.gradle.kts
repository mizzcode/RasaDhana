plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.rasadhana"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.rasadhana"
        minSdk = 23
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
            buildConfigField ("String", "BASE_URL", "\"https://be-rasadhana-245949327575.asia-southeast2.run.app/\"")
            buildConfigField ("String", "ML_BASE_URL", "\"https://ml-rasadhana-245949327575.asia-southeast2.run.app/\"")
            buildConfigField ("String", "WEB_CLIENT_ID", "\"502059075242-2o80df9u03gbha284q1824dq3b8lig32.apps.googleusercontent.com\"")
        }
        debug {
            buildConfigField ("String", "API_KEY", "\"your_debug_api_key_here\"")
            buildConfigField ("String", "BASE_URL", "\"https://be-rasadhana-245949327575.asia-southeast2.run.app/\"")
            buildConfigField ("String", "ML_BASE_URL", "\"https://ml-rasadhana-245949327575.asia-southeast2.run.app/\"")
            buildConfigField ("String", "WEB_CLIENT_ID", "\"502059075242-2o80df9u03gbha284q1824dq3b8lig32.apps.googleusercontent.com\"")
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

    // Add the dependency for the Firebase Authentication library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation(libs.firebase.auth)

    // Also add the dependency for the Google Play services library and specify its version
    implementation(libs.play.services.auth)

    implementation (libs.androidx.credentials)
    implementation (libs.androidx.credentials.play.services.auth)
    implementation (libs.googleid)

    // Add the dependencies for any other desired Firebase products
    // https://firebase.google.com/docs/android/setup#available-libraries

    // otpview
    implementation (libs.otpview)

    // datastore
    implementation(libs.androidx.datastore.preferences)

    // image cir  cle
    implementation (libs.circleimageview)
}