package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DeepVibrantTeal,
    onPrimary = Color.White,
    primaryContainer = SoftTealTint,
    onPrimaryContainer = DarkPine,
    secondary = MetallicGold,
    onSecondary = DarkPine,
    secondaryContainer = GoldHighlight,
    onSecondaryContainer = DarkPine,
    tertiary = LuminousCyan,
    onTertiary = DarkPine,
    background = CanvasMint,
    onBackground = DarkPine,
    surface = SurfaceWhite,
    onSurface = DarkPine,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = SlateTealMuted,
    outline = BorderTealGray,
    outlineVariant = BorderTealLight
)

private val DarkColorScheme = darkColorScheme(
    primary = LuminousCyan,
    onPrimary = DarkPine,
    primaryContainer = Color(0xFF16382D),
    onPrimaryContainer = CanvasMint,
    secondary = MetallicGold,
    onSecondary = DarkPine,
    secondaryContainer = Color(0xFF332B18),
    onSecondaryContainer = GoldHighlight,
    tertiary = DeepVibrantTeal,
    onTertiary = Color.White,
    background = Color(0xFF0F1418),
    onBackground = Color(0xFFE2E8F0),
    surface = Color(0xFF182026),
    onSurface = Color(0xFFE2E8F0),
    surfaceVariant = Color(0xFF222C35),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF26333C),
    outlineVariant = Color(0xFF1F2932)
)

@Composable
fun NoorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

