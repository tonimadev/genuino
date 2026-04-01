package com.genuino.sdk.contracts

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * Interface for providing themes to the SDK components.
 * The host app should implement this to map its Design System to the SDK.
 */
interface GenuinoThemeProvider {
    fun getPrimaryColor(): Color
    fun getBackgroundColor(): Color
    fun getTextColor(): Color
    fun getTextStyle(styleName: String?): TextStyle
}

/**
 * Interface for handling actions emitted by generated UI components.
 */
interface GenuinoActionHandler {
    /**
     * Called when a component (like a button) is interacted with.
     * @param actionId The identifier for the action.
     * @param payload Additional data associated with the action.
     */
    fun onAction(actionId: String, payload: Map<String, String>)
}

/**
 * Interface for LLM engine implementations.
 */
interface LlmEngine {
    /**
     * Sends a prompt to the LLM and expects a JSON string representing the UI.
     */
    suspend fun generateUi(prompt: String): String
}

/**
 * Abstraction for UI history storage to support Undo/Redo.
 */
interface GenuinoHistoryStorage {
    fun push(layout: String)
    fun undo(): String?
    fun redo(): String?
    fun canUndo(): Boolean
    fun canRedo(): Boolean
}
