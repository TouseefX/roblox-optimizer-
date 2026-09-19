# Building Optimizer by Tabi

This is an Android project written in **Kotlin** (not Java) and configured for:

- Android Gradle Plugin 8.5.0
- Gradle 8.7 (via the committed Gradle Wrapper)
- JDK 17
- Android API 34

## Build with GitHub Actions

1. Create a GitHub repository and upload the complete contents of this folder.
2. Push to the `main` branch (or open the **Actions** tab and select **Build Android APK** → **Run workflow**).
3. Wait for the **Build debug APK** job to succeed.
4. Open that run and download the `optimizer-by-tabi-debug-apk` artifact. It contains `app-debug.apk`.

The workflow is at `.github/workflows/build-apk.yml`. It installs Android API 34, uses JDK 17, builds with `./gradlew assembleDebug`, and uploads the APK artifact.

## Build locally

Use a machine with JDK 17 and Android SDK API 34 installed:

```bash
./gradlew assembleDebug
```

The debug APK is written to:

```text
app/build/outputs/apk/debug/app-debug.apk
```

A debug APK is suitable for personal testing. Use Android Studio's **Generate Signed Bundle / APK** flow before distributing a release build.
