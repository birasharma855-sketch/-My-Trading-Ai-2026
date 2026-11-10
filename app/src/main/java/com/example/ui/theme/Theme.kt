package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TradeProLightColorScheme = lightColorScheme(
    primary = TradeProBluePrimary,
    onPrimary = Color.White,
    primaryContainer = TradeProBlueContainer,
    onPrimaryContainer = TradeProBlueDark,
    secondary = BullGreen,
    onSecondary = Color.White,
    secondaryContainer = BullGreenContainer,
    onSecondaryContainer = BullGreenDark,
    tertiary = AiPurpleGlow,
    onTertiary = Color.White,
    background = NeutralBg,
    onBackground = NeutralDark,
    surface = NeutralSurface,
    onSurface = NeutralDark,
    surfaceVariant = Color(0xFFF1F5F9),
    onSurfaceVariant = NeutralMedium,
    outline = NeutralBorder,
    outlineVariant = Color(0xFFE2E8F0),
    error = BearRed,
    onError = Color.White,
    errorContainer = BearRedContainer,
    onErrorContainer = BearRedDark
)

private val TradeProDarkColorScheme = darkColorScheme(
    primary = TradeProBlueLight,
    onPrimary = Color.White,
    primaryContainer = TradeProBlueDark,
    onPrimaryContainer = Color(0xFFDBEAFE),
    secondary = BullGreen,
    onSecondary = Color.White,
    secondaryContainer = BullGreenDark,
    onSecondaryContainer = Color(0xFFA7F3D0),
    tertiary = AiPurpleGlow,
    onTertiary = Color.White,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF334155),
    onSurfaceVariant = Color(0xFFCBD5E1),
    outline = Color(0xFF475569),
    error = BearRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = false, // Default to crisp professional white theme per user request
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) TradeProDarkColorScheme else TradeProLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
