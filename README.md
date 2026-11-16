# ChamplainEats 🍽️

**Kotlin · Android · Jetpack Compose · SQLite · Material Design**

Browse food and explore restaurants through a native Android interface. The main activity uses Compose navigation to connect restaurant discovery, food details, order placement and order-history screens.

[Portfolio](https://github.com/ArushiNaik) · [Browse source](https://github.com/ArushiNaik/ChamplainEats)

## What to explore

- Home, Restaurants, Orders and Profile navigation.
- Restaurant details and food items.
- Order placement and order-detail screens.
- Local SQLite persistence.

## A guide to the code

- `app/src/main/java/com/champlain/fastfood/MainActivity.kt` — navigation entry point
- `app/src/main/java/com/champlain/fastfood/database` — local persistence
- `app/build.gradle.kts` — SDK and dependencies

## Run locally

Open the root in Android Studio, install Android SDK 34 and let Gradle sync. Create an emulator or connect a device running API 26+. Select the `app` run configuration and press Run.

Compile and target SDK: 34. Minimum SDK: 26. Use the JDK required by the checked-in Android Gradle plugin. With the Android SDK configured, a command-line debug build is:

```bash
./gradlew assembleDebug
```

Windows: `gradlew.bat assembleDebug`.

## Project notes

This is a development/portfolio app, not a live delivery service. No payment or fulfillment integration is claimed. Android builds were not rerun for this documentation update.

## Context

An Android project in [Arushi Naik’s portfolio](https://github.com/ArushiNaik).
