package com.genuino.sample

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import com.genuino.sdk.contracts.GenuinoThemeProvider

class SampleThemeProvider(
    private val primary: Color,
    private val background: Color,
    private val onBackground: Color,
    private val typography: androidx.compose.material3.Typography
) : GenuinoThemeProvider {
    override fun getPrimaryColor(): Color = primary
    override fun getBackgroundColor(): Color = background
    override fun getTextColor(): Color = onBackground
    override fun getTextStyle(styleName: String?): TextStyle {
        return when (styleName) {
            "title" -> typography.titleLarge
            "body" -> typography.bodyMedium
            "label" -> typography.labelSmall
            else -> typography.bodyMedium
        }
    }
}
