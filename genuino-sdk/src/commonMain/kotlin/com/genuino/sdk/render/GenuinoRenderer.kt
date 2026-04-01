package com.genuino.sdk.render

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.genuino.sdk.contracts.GenuinoActionHandler
import com.genuino.sdk.contracts.GenuinoThemeProvider
import com.genuino.sdk.model.ComponentType

/**
 * The core rendering engine of Genuino.
 * It maps [ComponentType] entities to Compose Multiplatform UI components.
 * 
 * @param component The component to render.
 * @param themeProvider Provider for colors and styles.
 * @param actionHandler Handler for events emitted by components.
 */
@Composable
fun GenuinoRenderer(
    component: ComponentType,
    themeProvider: GenuinoThemeProvider,
    actionHandler: GenuinoActionHandler
) {
    when (component) {
        is ComponentType.Text -> {
            Text(
                text = component.text,
                style = themeProvider.getTextStyle(component.style),
                color = component.color?.let { parseColor(it) } ?: themeProvider.getTextColor()
            )
        }
        is ComponentType.Button -> {
            Button(
                onClick = { actionHandler.onAction(component.actionId, component.payload) },
                colors = ButtonDefaults.buttonColors(
                    containerColor = themeProvider.getPrimaryColor()
                )
            ) {
                Text(text = component.text)
            }
        }
        is ComponentType.Column -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(component.spacing.dp)
            ) {
                component.children.forEach { child ->
                    GenuinoRenderer(child, themeProvider, actionHandler)
                }
            }
        }
        is ComponentType.Row -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(component.spacing.dp)
            ) {
                component.children.forEach { child ->
                    GenuinoRenderer(child, themeProvider, actionHandler)
                }
            }
        }
        is ComponentType.Spacer -> {
            Spacer(modifier = Modifier.size(component.size.dp))
        }
    }
}

// Simple color parser for Hex strings
private fun parseColor(colorString: String): androidx.compose.ui.graphics.Color {
    return try {
        androidx.compose.ui.graphics.Color(
            colorString.removePrefix("#").toLong(16) or 0x00000000FF000000
        )
    } catch (e: Exception) {
        androidx.compose.ui.graphics.Color.Unspecified
    }
}
