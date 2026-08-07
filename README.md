# MentorHakka

MentorHakka is a native Android app built during a **48-hour hackathon at Plaksha University**. It acts as a personal mentor: you write a daily journal entry and state a goal ("aim"), and the app uses Google's Gemini generative model to turn that into personalized mentorship and career-style guidance — what went well, what to fix, and what to do tomorrow.

## Features

- **Journal Entry** — capture a free-text entry describing your day, thoughts, or challenges.
- **Aim** — state the goal you're currently working toward, used as context for the AI.
- **Get Guidance** — sends the journal entry + aim to the Gemini model, which returns mentorship advice, mistakes to correct, and next-day action items.
- **History** — browse past entries alongside the guidance the app generated for them, with timestamps.
- **Analytics** — a simple breakdown of how many past responses indicate an achieved vs. unachieved goal (keyword-based on the "achieved" string in stored responses).

## Tech Stack

- **Kotlin** on native **Android**, UI built entirely with **Jetpack Compose** (Material 3).
- **Jetpack Navigation Compose** for screen navigation (Home / History / Analytics).
- **ViewModel** (`ChatViewModel`) to hold chat/journal history in memory for the session.
- **Google AI Client SDK** (`com.google.ai.client.generativeai:generativeai`) calling the **Gemini** model (`gemini-1.0-pro-001`) to generate guidance.
- Min SDK 24, target/compile SDK 34.

## Getting Started

1. Clone the repository and open it in **Android Studio**.
2. Let Gradle sync (Gradle Kotlin DSL, `build.gradle.kts`).
3. Supply a Gemini API key: the `GenerativeModel` instance in `MainActivity.kt` currently takes an empty API key string — replace it with your own key from [Google AI Studio](https://aistudio.google.com/), ideally injected via a Gradle secrets/BuildConfig mechanism (e.g. the [secrets-gradle-plugin](https://github.com/google/secrets-gradle-plugin)) rather than hard-coding it.
4. Run the app on an emulator or physical device (minimum Android 7.0 / API 24).

## Project Structure

- `app/src/main/java/com/example/mentorhakka/MainActivity.kt` — app entry point, navigation graph, and all three screens (Home, History, Analytics).
- `app/src/main/java/com/example/mentorhakka/ChatViewModel.kt` — in-memory store of journal responses and their dates.
- `app/src/main/java/com/example/mentorhakka/ui/theme/` — Compose theme (colors, typography).

## Notes

- This was built as a hackathon project, so history and analytics are held in memory for the current app session (via `ChatViewModel`) rather than persisted to disk or a backend.
- Screenshots of the Home, History, and Analytics screens are included in the repo root (`homescreen.png`, `historyscreen.png`, `analytics.png`).

## Contributing

Contributions, issues, and pull requests are welcome.
