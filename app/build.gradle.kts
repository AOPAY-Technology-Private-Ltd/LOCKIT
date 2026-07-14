plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.bosandroidapp.aopaykit"
    compileSdk = 34


    defaultConfig {
        applicationId = "com.bosandroidapp.aopaykit"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }


    buildFeatures {
        viewBinding = true
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }


    kotlinOptions {
        jvmTarget = "1.8"
    }


    sourceSets {
        getByName("main") {
            assets {
                srcDirs("src\\main\\assets", "src\\main\\assets")
            }
        }
    }


}

dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)


    // Retrofit and GSON
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.converter.scalars)
    implementation(libs.pinview)
    implementation(libs.mpandroidchart)
    implementation(libs.androidx.browser)


    implementation(libs.lottie)
    implementation(libs.glide)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.play.services.location)
    implementation(libs.colormath)
    implementation(libs.androidx.work.runtime.ktx)
    // for qrcode...................................
    implementation(libs.core)

    // google map..................................
    implementation(libs.play.services.maps)
    implementation(libs.maps.ktx)


    // firebase ...................................
    implementation(libs.firebase.messaging)
    // ✅ Firebase BOM MUST be platform()
    implementation(platform(libs.firebase.bom))
    // ✅ Firestore KTX
    implementation(libs.firebase.firestore.ktx)
    // (optional)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.swiperefreshlayout)

    implementation(libs.googleid)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    implementation(libs.play.services.auth)

}