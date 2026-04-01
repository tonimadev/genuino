package com.genuino.sdk.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

/**
 * Base entities for UI components.
 */
@Serializable
sealed class ComponentType {
    @Serializable
    @SerialName("Text")
    data class Text(
        val text: String,
        val style: String? = null,
        val color: String? = null
    ) : ComponentType()

    @Serializable
    @SerialName("Button")
    data class Button(
        val text: String,
        val actionId: String,
        val payload: Map<String, String> = emptyMap()
    ) : ComponentType()

    @Serializable
    @SerialName("Column")
    data class Column(
        val children: List<ComponentType>,
        val spacing: Int = 0
    ) : ComponentType()

    @Serializable
    @SerialName("Row")
    data class Row(
        val children: List<ComponentType>,
        val spacing: Int = 0
    ) : ComponentType()
    
    @Serializable
    @SerialName("Spacer")
    data class Spacer(val size: Int) : ComponentType()
}

/**
 * Root layout structure for Genuino.
 */
@Serializable
data class GenuinoLayout(
    val root: ComponentType
)
