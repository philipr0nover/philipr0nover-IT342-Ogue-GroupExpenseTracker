package com.ogue.groupexpensetracker.mobile.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// 🎨 BRAND COLORS (MATCH WEB)
val GreenPrimary = Color(0xFF10B981)
val GreenDark = Color(0xFF059669)
val GreenLight = Color(0xFF34D399)

val BackgroundLight = Color(0xFFF9FAFB)
val SurfaceLight = Color(0xFFFFFFFF)
val TextPrimary = Color(0xFF111827)

// 🌙 DARK THEME
private val DarkColorScheme = darkColorScheme(
    primary = GreenPrimary,
    secondary = GreenDark,
    tertiary = GreenLight
)

// ☀️ LIGHT THEME
private val LightColorScheme = lightColorScheme(
    primary = GreenPrimary,
    secondary = GreenDark,
    tertiary = GreenLight,
    background = BackgroundLight,
    surface = SurfaceLight,
    onPrimary = Color.White,
    onBackground = TextPrimary,
    onSurface = TextPrimary
)

// 🔥 MAIN THEME (RENAMED)
@Composable
fun GroupExpenseMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context)
            else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}