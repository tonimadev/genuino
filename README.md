# Genuino SDK 🚀

Genuino is an innovative SDK for **Kotlin Multiplatform (KMP)** that enables dynamic user interface (UI) generation using Large Language Models (LLM), such as Google Gemini.

With Genuino, you can describe interface components using natural language, and the SDK takes care of rendering them natively on Android and iOS using Compose Multiplatform.

## ✨ Key Features

- 🧠 **AI-Powered UI Generation:** Transform text prompts into functional components.
- 📱 **Multiplatform:** Full support for Android and iOS (and experimental for Desktop/Web).
- 🎨 **Flexible Theming:** Easily integrate with your `MaterialTheme`.
- ⚡ **Extensible:** Support for different LLM engines and custom action handlers.
- 🔄 **History Management:** Native Undo/Redo functions for the generated interfaces.

## 📦 Project Structure

* `genuino-sdk`: The core of the SDK containing the rendering logic, LLM integration, and contracts.
* `genuino-sample`: A sample application demonstrating how to integrate and use the SDK in practice.
- `iosApp`: The native entry point for the iOS application.

## 🚀 How to Use

### 1. Basic Configuration

To use Genuino in your Compose Multiplatform project, you need to configure the `GenuinoConfig`:

```kotlin
val config = GenuinoConfig(
    llmEngine = GeminiLlmEngine(apiKey = "YOUR_API_KEY"),
    themeProvider = YourThemeProvider(),
    actionHandler = YourActionHandler()
)
```

### 2. Rendering the Interface

Simply use the `GenuinoContainer` Composable passing a prompt (e.g., "Create a user profile card"):

```kotlin
GenuinoContainer(
    prompt = "Create a login form with email and password fields",
    config = config,
    modifier = Modifier.fillMaxSize()
)
```

## 🛠 Build Commands

### Android
To generate the debug APK:
```shell
./gradlew :genuino-sample:assembleDebug
```

### iOS
To compile the framework and run on the simulator (via Xcode):
1. Open the `iosApp` folder in Xcode.
2. Select the `iosApp` scheme and the desired simulator.
3. Click **Run**.

Alternatively, you can build via terminal:
```shell
./gradlew :genuino-sample:embedAndSignAppleFrameworkForXcode
```

---

## 🏗 Technologies Used

- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
- [Ktor](https://ktor.io/) (for network calls)
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)