# Optimizer by Tabi — Android project

This is a complete Android Studio project implementing the flow you asked for:

1. **Splash screen** — plays your video full-screen, with "Optimizer by Tabi" underneath.
2. **Optimization screen** — runs safe, on-device housekeeping (see note below) with a status log.
3. **Launch screen** — plays your video again, with "Launching Roblox…" underneath, then opens Roblox automatically. If Roblox isn't installed, it opens the Play Store listing instead.

Your logo (`1000003557.png`) has been turned into the app icon at every required density (`mipmap-mdpi` through `mipmap-xxxhdpi`), and your video is bundled at `app/src/main/res/raw/intro_video.mp4`.

## Important: what "optimization" actually means here

A normal Android app — one that isn't rooting the device or modifying Roblox's own APK — physically cannot reach into Roblox's process, change its render settings, or touch its memory. So this app doesn't pretend to. Per what you asked (no cheats, no memory injection), the optimization step does real, legitimate things only:

- clears this app's own cache
- asks the system to free up memory
- reads and displays free RAM
- reads and displays free storage
- checks whether you're on Wi-Fi or mobile data

That's the honest ceiling for any non-root third-party app. Most "game booster" apps on the Play Store do exactly this and little more — I didn't want to build you something that quietly does nothing while claiming otherwise.

## Building the APK — no PC needed

This project includes `.github/workflows/build-apk.yml`, a GitHub Actions workflow. Once this project is pushed to a GitHub repo, GitHub's own cloud servers (which already have the Android SDK installed) will compile the APK for you automatically — you never touch a build tool yourself.

1. Push/upload this project to a GitHub repository (any way you like — via the API, via the mobile web uploader, or from a computer if you ever get access to one).
2. Go to the repo's **Actions** tab → the "Build APK" workflow will already have run (or tap **Run workflow** to trigger it).
3. Once it finishes (green check, a couple of minutes), open that run → scroll to **Artifacts** → download `optimizer-by-tabi-debug-apk`. That's a zip containing `app-debug.apk`.
4. Unzip it on your phone (Files app or any zip app) and tap the `.apk` to install (you'll need to allow "install unknown apps" for whichever app you used to open it).

## Building the APK — with a PC (if you ever have access to one)

1. Install [Android Studio](https://developer.android.com/studio) (free).
2. Open Android Studio → **Open** → select this project folder.
3. Let Gradle sync (it'll fetch the Android build tools automatically the first time — this needs internet access).
4. **Build → Build Bundle(s) / APK(s) → Build APK(s)**.
5. Your APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

To make a signed release APK instead of a debug one, use **Build → Generate Signed Bundle / APK** and follow the prompts to create a keystore.

## One naming note

The app is named "Optimizer by Tabi" rather than putting "Roblox" in the app's own title/branding. Roblox Corporation owns that trademark, and naming or branding a third-party app as if it were official/affiliated is a common reason such apps get pulled from the Play Store. The app still targets and launches Roblox by package name (`com.roblox.client`) — that part works exactly as you described — this is just about the app's own name and listing if you ever publish it.
