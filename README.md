
# ZohoAndroidAutomation

Android project containing automation and test code for Zoho-related workflows.

## Summary

- **Project:** ZohoAndroidAutomation
- **Type:** Android app + automation/tests
- **Build:** Gradle (Kotlin DSL)

## Contents

- `app/` — Android application module and automation tests.
- `gradle/`, build files — project build configuration.

## Requirements

- JDK 11 or newer
- Android Studio (recommended)
- Android SDK and platform tools
- An emulator or physical device for instrumentation tests

## Quick start

1. Open the project in Android Studio: File → Open and select the project root.
2. Or from the command line (Windows):

```bash
./gradlew.bat assembleDebug
./gradlew.bat connectedAndroidTest
```

Replace `./gradlew.bat` with `./gradlew` on Unix-like systems.

## Running tests

- Unit tests:

```bash
./gradlew.bat test
```

- Instrumentation / UI tests (device or emulator required):

```bash
./gradlew.bat connectedAndroidTest
```

## Common tasks

- Clean build:

```bash
./gradlew.bat clean assembleDebug
```

- Generate APK:

```bash
./gradlew.bat assembleRelease
```

## Contributing

1. Fork the repository and create a branch for your change.
2. Make changes and add/adjust tests where appropriate.
3. Submit a pull request with a clear description of changes.

## Notes

- Keep dependencies up to date via the `libs.versions.toml` manifest in `gradle/`.
- If you add instrumentation tests that depend on device configuration, document required device/emulator settings in this README.

## License

This project does not include a license file. Add a `LICENSE` file to declare licensing terms.

## Contact

For questions about this repo, open an issue or contact the maintainer.

