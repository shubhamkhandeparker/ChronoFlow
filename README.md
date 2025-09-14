# ChronoFlow - An AI-Powered Timer & Stopwatch App

ChronoFlow is a sleek, modern timekeeping application for Android built with 100% Kotlin and Jetpack Compose. It features a fully functional stopwatch and timer, enhanced with intelligent, natural language voice commands.

## 📸 Screenshots

| Stopwatch | Timer | Voice Permission |
| :---: | :---: | :---: |
| [<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/9b7dd242-701a-4fb3-aad0-515841f87cdd" />
] | [<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/5bdb08ee-c63f-47b7-9c11-57077f8a297a" />
] | [<img width="1080" height="2400" alt="image" src="https://github.com/user-attachments/assets/16f274b6-f135-476b-9d4a-a9ad674a27ec" />
] |

## ✨ Features

* **Dual Mode:** Fully functional Stopwatch and Timer modes.
* **AI Voice Control:** Control the app with natural language.
    * Start, pause, and reset the stopwatch or timer.
    * Set timer durations using complex commands (e.g., "Set timer for 1 hour and 30 minutes").
* **Data Persistence:** The app remembers your last set timer duration using Jetpack DataStore.
* **System Integration:** Plays a sound and vibrates when the timer is finished.
* **Modern UI:** A clean, single-activity UI built entirely with Jetpack Compose and Material 3, including a polished `BottomAppBar` with a `FloatingActionButton`.

## 🏛️ Architecture

This app is built following modern Android architecture best practices and the official Guide to App Architecture.

* **MVVM (Model-View-ViewModel):** The UI layer is strictly separated from the business logic.
* **Repository Pattern:** A `SettingsRepository` abstracts the data source (DataStore).
* **Service Layer:** Logic is encapsulated in services like `VoiceRecognizerService` and `NotificationService`.
* **Dependency Injection:** Hilt is used to provide and manage dependencies throughout the app.

## 🛠️ Tech Stack & Libraries

* **Kotlin** + **Coroutines & Flow** for asynchronous operations.
* **Jetpack Compose** for the declarative UI.
* **Material 3** for UI components and theming.
* **Hilt** for Dependency Injection.
* **Accompanist** for easy runtime permission handling.
* **Jetpack DataStore** for simple, asynchronous data storage.
* **Android's `SpeechRecognizer` API** for voice-to-text.
