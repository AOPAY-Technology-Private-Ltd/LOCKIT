# LOCKIT

1. Project Overview
   AOPAYMobileFinance is a specialized Android application designed for financial management with integrated Enterprise Device Management (EDM) and Kiosk Mode capabilities. The app is built to be deployed on company-owned devices, allowing administrators to remotely control device policies (like camera access, kiosk locking, and remote uninstallation) via Firebase Cloud Messaging (FCM) while providing financial services to users.
2. Features
   •
   Financial Dashboard: Real-time tracking and visualization of financial data .
   •
   Kiosk Mode (COSU): Locks the device to a single application to prevent unauthorized use.
   •
   Remote Policy Management:
   ◦
   Enable/Disable camera remotely.
   ◦
   Remote App Uninstallation/Device Owner clearing.
   ◦
   Remote Kiosk activation/deactivation.
   •
   Push Notifications: Deeply integrated with Firebase Cloud Messaging for real-time command execution.
   •
   Secure Authentication: PIN-based security using specialized PinView components.
   •
   Lottie Animations: High-quality UI transitions and feedback.
3. Tech Stack
   •
   Language: Kotlin
   •
   UI Framework: XML (DataBinding/ViewBinding)
   •
   Networking: Retrofit 2 & OkHttp 3
   •
   Local Persistence: Jetpack Room / SharedPreferences
   •
   Async Processing: Kotlin Coroutines & WorkManager
   •
   Dependency Injection: Manual/Standard Android patterns
   •
   UI Components: Material Design, SDP (Scalable DP), SSP (Scalable SP)
   •
   External Libraries: Lottie, MPAndroidChart, PinView
4. Architecture Pattern
   The project follows the MVVM (Model-View-ViewModel) architectural pattern recommended by Google:
   •
   View: Activities and Fragments using ViewBinding to observe ViewModel data.
   •
   ViewModel: Handles UI logic and communicates with the Repository.
   •
   Model: Data classes and API response schemas.
   •
   Repository: Acts as a single source of truth for data from APIs and local storage.
5. Module Explanation
   •
   :app: The main entry point containing UI components and business logic.
   •
   kioskmode: Dedicated logic for DeviceAdminReceiver and KioskPolicyService to handle system-level locks.
   •
   notification: Contains MyFirebaseMessagingService for handling background payloads and remote triggers.
6. API Integration Details
   The app uses Retrofit 2 for RESTful API communication.
   •
   Base URL: https://uatapi.aopay.co.in/
   •
   Endpoints: Includes modules for authentication, transaction history, and device registration.
   •
   Interceptors: Logging interceptor included for debugging network requests.
7. Folder Structure
   Java
   app/src/main/java/com/bosandroidapp/aopayfinance/
   ├── api/                # Retrofit interfaces & API clients
   ├── kioskmode/          # Kiosk policies, DeviceAdmin, & Lock logic
   ├── models/             # Data classes/POJOs
   ├── notification/       # Firebase Messaging Service
   ├── repository/         # Data management logic
   ├── ui/                 # Activities, Fragments, & Adapters
   │   ├── dashboard/
   │   ├── auth/
   │   └── views/          # Custom views
   └── viewmodel/          # Lifecycle-aware ViewModels
8. Build and Setup Steps

1. Clone the repository:
   Shell Script
   git clone https://github.com/AOPAY-Technology-Private-Ltd/LOCKIT.git
2. Open in Android Studio: Select "Open an existing project" and choose the AOPAYMobileFinance folder.
3. Firebase Setup:
   ◦ Create a project in the Firebase Console.
   ◦ Add an Android app with package name com.bosandroidapp.aopayfinance.
   ◦ Download google-services.json and place it in the app/ directory.

4. Sync Gradle: Click File > Sync Project with Gradle Files.

9. Required Environment/Configuration
   •
   Android Studio: Hedgehog | 2023.1.1 or newer.
   •
   JDK: Java 17.
   •
   SDK: Compile SDK 34, Min SDK 24.
   •
   Device Owner Permissions: To use Kiosk features, the app must be set as Device Owner via ADB:
   Shell Script
   adb shell dpm set-device-owner com.bosandroidapp.aopayfinance/com.bosandroidapp.aopayfinance.kioskmode.KioskDeviceAdminReceiver

10. Gradle Dependencies
    Key dependencies used in the project:
    Gradle
    dependencies {
    implementation "androidx.core:core-ktx:1.10.1"    
    implementation "com.squareup.retrofit2:retrofit:2.9.0"
    implementation "com.google.firebase:firebase-messaging-ktx:23.x.x"
    implementation "com.github.PhilJay:MPAndroidChart:v3.1.0"
    implementation "com.airbnb.android:lottie:3.4.0"
    implementation "com.intuit.sdp:sdp-android:1.0.6"
    implementation "io.github.chaosleung:pinview:1.4.4"
    }

11. APK Generation Steps

1. Go to Build > Generate Signed Bundle / APK....
2. Select APK and click Next.
3. Create/Select your KeyStore file.
4. Select build variant release.
5. Check V1 (Jar Signature) and V2 (Full APK Signature).
6. Click Finish. The APK will be generated in app/release/.

12. Screenshots
    Login Screen
    Dashboard
    Kiosk Mode
    Login
    Dashboard
    Kiosk

13. Contribution Guidelines

1. Fork the Project.
2. Create your Feature Branch (git checkout -b feature/AmazingFeature).
3. Commit your Changes (git commit -m 'Add some AmazingFeature').
4. Push to the Branch (git push origin feature/AmazingFeature).
5. Open a Pull Request.

14. Contact Information
    Company: AOPAY FINANCE
    Email: info@aopay.in

### 15/05/2026
1) Modified the Manage Loan API for both Retailer and Customer applications to support EMI payment flow changes.

### 16/05/2026

1) Issues rectify apply emi for customer and retailer

### 18/05/2026
1. Updated EMI receiving flow and charge management for both retailer and customer.
2. Fixed the phone lock/unlock issue after updating the grace period for customers.
3. Implemented customer contact number copy functionality and direct click-to-dial functionality. feature on the retailer side.


### 19/05/2026
(Customer Side )
1. Implemented app uninstall functionality once all EMIs are completed.
2. Modified the Loan Page due date status according to EMI completion.
3. Managed permissions during the app uninstall process.


### 20/05/2026
(Retailer Panel)
1. Implemented KYC flow for references only in Online Mode.
2. Loan charges API implemented for each loan.


### 21/05/2026
(Retailer Panel)
1. Implemented KYC flow api and validate condition for online mode.
2. Resolved issue related kyc


### 22/05/2026
1. Fixed errors in the complete flow from customer creation to loan creation.


### 25/05/2026
1. Resolved issues in app


### 26/05/2026
1. Validate kyc details with bank details
2. Resolved bugs


### 27/05/2026
1. eNACH API has been implemented, callback handling is completed, and popups are shown to the user accordingly.


### 28/05/2026
1. Customer EMI Status report api has been implemented.
2. Retailer Hold Wallet Ledger report api has been implemented.
3. eNACH Api flow modification .