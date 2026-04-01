package com.genuino.sdk

import androidx.compose.runtime.*
import com.genuino.sdk.contracts.*
import com.genuino.sdk.engine.GeminiLlmEngine
import com.genuino.sdk.model.ComponentType
import com.genuino.sdk.model.GenuinoLayout
import com.genuino.sdk.render.GenuinoRenderer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.serialization.json.Json

/**
 * Configuration for the Genuino SDK.
 */
data class GenuinoConfig(
    val llmEngine: LlmEngine,
    val themeProvider: GenuinoThemeProvider,
    val actionHandler: GenuinoActionHandler,
    val historyStorage: GenuinoHistoryStorage = DefaultHistoryStorage()
)

/**
 * Main entry point for Genuino SDK.
 */
class Genuino private constructor(val config: GenuinoConfig) {

    private val _uiState = MutableStateFlow<ComponentType?>(null)
    val uiState: StateFlow<ComponentType?> = _uiState.asStateFlow()

    private val json = Json { ignoreUnknownKeys = true }

    /**
     * Triggers UI generation based on a prompt.
     */
    suspend fun generate(prompt: String) {
        try {
            val jsonResponse = config.llmEngine.generateUi(prompt)
            val layout = json.decodeFromString<GenuinoLayout>(jsonResponse)
            _uiState.value = layout.root
            config.historyStorage.push(jsonResponse)
        } catch (e: Exception) {
            e.printStackTrace()
            // Handle error state if necessary
        }
    }

    /**
     * Reverts to the previous UI state.
     */
    fun undo() {
        config.historyStorage.undo()?.let { jsonStr ->
            val layout = json.decodeFromString<GenuinoLayout>(jsonStr)
            _uiState.value = layout.root
        }
    }

    /**
     * Restores the next UI state in the history.
     */
    fun redo() {
        config.historyStorage.redo()?.let { jsonStr ->
            val layout = json.decodeFromString<GenuinoLayout>(jsonStr)
            _uiState.value = layout.root
        }
    }

    class Builder {
        private var llmEngine: LlmEngine? = null
        private var themeProvider: GenuinoThemeProvider? = null
        private var actionHandler: GenuinoActionHandler? = null

        fun setLlmEngine(engine: LlmEngine) = apply { this.llmEngine = engine }
        fun setTheme(theme: GenuinoThemeProvider) = apply { this.themeProvider = theme }
        fun setActionHandler(handler: GenuinoActionHandler) = apply { this.actionHandler = handler }

        fun build(): Genuino {
            val config = GenuinoConfig(
                llmEngine = llmEngine ?: throw IllegalStateException("LlmEngine is required"),
                themeProvider = themeProvider ?: throw IllegalStateException("ThemeProvider is required"),
                actionHandler = actionHandler ?: throw IllegalStateException("ActionHandler is required")
            )
            return Genuino(config)
        }
    }
}

/**
 * Composable container that renders the generated UI based on a prompt and configuration.
 *
 * @param prompt The prompt to generate the UI from.
 * @param config The configuration for the Genuino SDK.
 * @param modifier Modifier to be applied to the layout.
 */
@Composable
fun GenuinoContainer(
    prompt: String,
    config: GenuinoConfig,
    modifier: androidx.compose.ui.Modifier = androidx.compose.ui.Modifier
) {
    val scope = rememberCoroutineScope()
    val genUi = remember(config) { Genuino.Builder().apply {
        setLlmEngine(config.llmEngine)
        setTheme(config.themeProvider)
        setActionHandler(config.actionHandler)
    }.build() }
    
    val uiState by genUi.uiState.collectAsState()

    LaunchedEffect(prompt) {
        if (prompt.isNotBlank()) {
            genUi.generate(prompt)
        }
    }

    androidx.compose.foundation.layout.Box(modifier = modifier) {
        uiState?.let { component ->
            GenuinoRenderer(
                component = component,
                themeProvider = genUi.config.themeProvider,
                actionHandler = genUi.config.actionHandler
            )
        } ?: androidx.compose.material3.Text("Gerando interface...")
    }
}

internal class DefaultHistoryStorage : GenuinoHistoryStorage {
    private val history = mutableListOf<String>()
    private var currentIndex = -1

    override fun push(layout: String) {
        if (currentIndex < history.size - 1) {
            history.subList(currentIndex + 1, history.size).clear()
        }
        history.add(layout)
        currentIndex = history.size - 1
    }

    override fun undo(): String? {
        if (canUndo()) {
            currentIndex--
            return history[currentIndex]
        }
        return null
    }

    override fun redo(): String? {
        if (canRedo()) {
            currentIndex++
            return history[currentIndex]
        }
        return null
    }

    override fun canUndo(): Boolean = currentIndex > 0
    override fun canRedo(): Boolean = currentIndex < history.size - 1
}
