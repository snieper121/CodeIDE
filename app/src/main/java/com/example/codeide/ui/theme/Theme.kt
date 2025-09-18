package com.example.codeide.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Black,
    secondary = Black,
    tertiary = Black,
    background = Color(0xFF1C1B1F), // Стандартный тёмный фон Material 3
    surface = Color(0xFF1C1B1F)
)

@Composable
fun CodeIDETheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Мы принудительно используем тёмную тему
    val colorScheme = DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}