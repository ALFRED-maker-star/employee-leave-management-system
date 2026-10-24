package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = CorporateBlue,
    onPrimary = Color.White,
    primaryContainer = CorporateBlueLight,
    onPrimaryContainer = CorporateBlueHover,
    secondary = TextSecondary,
    onSecondary = Color.White,
    secondaryContainer = CorporateSurfaceVariant,
    onSecondaryContainer = TextPrimary,
    tertiary = StatusPending,
    onTertiary = Color.White,
    background = CorporateBackground,
    onBackground = TextPrimary,
    surface = CorporateSurface,
    onSurface = TextPrimary,
    surfaceVariant = CorporateSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    outline = CorporateBorder,
    outlineVariant = CorporateBorderStrong,
    error = StatusRejected,
    onError = Color.White,
    errorContainer = StatusRejectedBg,
    onErrorContainer = StatusRejectedText
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF60A5FA),
    onPrimary = Color(0xFF0F172A),
    primaryContainer = Color(0xFF1E3A8A),
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = Color(0xFF94A3B8),
    onSecondary = Color(0xFF0F172A),
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569),
    error = Color(0xFFF87171),
    onError = Color(0xFF450A0A)
)

@Composable
fun LeaveEaseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Explicit corporate styling honoring the palette requested
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
