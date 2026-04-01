# Genuino SDK 🚀

Genuino é um SDK inovador para **Kotlin Multiplatform (KMP)** que permite a geração dinâmica de interfaces de usuário (UI) utilizando modelos de linguagem de grande escala (LLM), como o Google Gemini.

Com o Genuino, você pode descrever componentes de interface através de linguagem natural e o SDK se encarrega de renderizá-los nativamente em Android e iOS usando Compose Multiplatform.

## ✨ Principais Funcionalidades

- 🧠 **Geração de UI via AI:** Transforme prompts de texto em componentes funcionais.
- 📱 **Multiplataforma:** Suporte total para Android e iOS (e experimental para Desktop/Web).
- 🎨 **Tematização Flexível:** Integre facilmente com o seu `MaterialTheme`.
- ⚡ **Extensível:** Suporte a diferentes engines de LLM e handlers de ação personalizados.
- 🔄 **Gerenciamento de Histórico:** Funções nativas de Undo/Redo para as interfaces geradas.

## 📦 Estrutura do Projeto

* `genuino-sdk`: O núcleo do SDK contendo a lógica de renderização, integração com LLM e contratos.
* `genuino-sample`: Aplicativo de exemplo demonstrando como integrar e usar o SDK na prática.
- `iosApp`: O ponto de entrada nativo para a aplicação iOS.

## 🚀 Como Usar

### 1. Configuração Básica

Para utilizar o Genuino em seu projeto Compose Multiplatform, você precisa configurar o `GenuinoConfig`:

```kotlin
val config = GenuinoConfig(
    llmEngine = GeminiLlmEngine(apiKey = "SUA_API_KEY"),
    themeProvider = SeuThemeProvider(),
    actionHandler = SeuActionHandler()
)
```

### 2. Renderizando a Interface

Basta utilizar o Composable `GenuinoContainer` passando um prompt (ex: "Crie um card de perfil de usuário"):

```kotlin
GenuinoContainer(
    prompt = "Crie um formulário de login com campos de e-mail e senha",
    config = config,
    modifier = Modifier.fillMaxSize()
)
```

## 🛠 Comandos de Build

### Android
Para gerar o APK de debug:
```shell
./gradlew :genuino-sample:assembleDebug
```

### iOS
Para compilar o framework e rodar no simulador (via Xcode):
1. Abra a pasta `iosApp` no Xcode.
2. Selecione o esquema `iosApp` e o simulador desejado.
3. Clique em **Run**.

Alternativamente, você pode buildar via terminal:
```shell
./gradlew :genuino-sample:embedAndSignAppleFrameworkForXcode
```

---

## 🏗 Tecnologias Utilizadas

- [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)
- [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
- [Ktor](https://ktor.io/) (para chamadas de rede)
- [Kotlinx Serialization](https://github.com/Kotlin/kotlinx.serialization)