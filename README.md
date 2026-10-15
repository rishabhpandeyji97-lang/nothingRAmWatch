# RAM Watch for Nothing Phone (3a)

A small native Android app + home-screen widget showing system total, used (total minus available), and available RAM. The widget updates when Android sends its scheduled widget update (30-minute minimum interval) and when the app is opened/refreshed.

## Important limitations
- Android exposes system-wide memory through `ActivityManager.MemoryInfo`.
- The app attempts to list processes Android exposes through `getRunningAppProcesses()` and estimates process PSS using `getProcessMemoryInfo()`.
- On modern Android releases, third-party apps are commonly restricted from seeing other apps' process information. The per-app list may be incomplete or empty. Do not interpret missing apps as zero RAM use.
- Usage Access is for app-usage history, not a permission to read every app's RAM.
- This app does not root the phone, kill processes, or claim to boost FPS.

## Build APK
This project source is provided; it is **not a compiled APK**. Build with Android Studio / an Android Gradle build service using JDK 17+, Android SDK 35, and Gradle plugin 8.7.3. Open the project, allow Gradle sync, then build `app` > `assembleDebug`. The APK should be generated at `app/build/outputs/apk/debug/app-debug.apk`.
