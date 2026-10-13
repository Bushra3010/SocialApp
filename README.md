# Circle — Social Media Demo App

A native Android demo of a social media app, built with Kotlin and Jetpack Compose. It uses dummy data only and runs fully offline.

## Download

The ready-to-install APK is in [`release/Circle-demo.apk`](release/Circle-demo.apk). It needs Android 8.0 or newer. Allow installs from unknown sources when the phone asks.

## Features

- Login screen with demo credentials already filled in
- Home feed with stories, posts, double-tap to like, comments, share and save
- Explore: search people and browse a grid of posts
- Create a post with a photo, caption and location
- Activity: likes, comments, follows and mentions
- Profile with a posts grid and a Saved tab, plus other users' profiles with Follow and Message
- Direct messages with simulated replies
- Full-screen story viewer

Everything resets when the app restarts.

## Build

Requires JDK 17+ and the Android SDK (API 35).

```bash
./gradlew assembleRelease
# output: app/build/outputs/apk/release/app-release.apk
```

The release build is signed with the local debug key so it installs directly. It is not set up for Play Store publishing.
