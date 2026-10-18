package com.sigaf.app.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * Tema global de SIGAF.
 * La app es SIEMPRE oscura (modo oscuro absoluto), por eso se fuerza el esquema oscuro.
 */
private val SigafDarkColorScheme = darkColorScheme(
    primary = SigafColors.Cyan,
    onPrimary = SigafColors.OnCyan,
    primaryContainer = SigafColors.SurfaceHigh,
    onPrimaryContainer = SigafColors.Cyan,
    secondary = SigafColors.CyanDark,
    onSecondary = SigafColors.OnCyan,
    tertiary = SigafColors.Safe,
    background = SigafColors.Background,
    onBackground = SigafColors.TextPrimary,
    surface = SigafColors.Surface,
    onSurface = SigafColors.TextPrimary,
    surfaceVariant = SigafColors.SurfaceVariant,
    onSurfaceVariant = SigafColors.TextSecondary,
    surfaceTint = Color.Transparent,
    outline = SigafColors.Border,
    outlineVariant = SigafColors.BorderSoft,
    error = SigafColors.Critical,
    onError = Color.White
)

private val SigafShapes = Shapes(
    extraSmall = RoundedCornerShape(8.dp),
    small = RoundedCornerShape(12.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(20.dp),
    extraLarge = RoundedCornerShape(28.dp)
)

@Composable
fun SigafTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SigafDarkColorScheme,
        typography = SigafTypography,
        shapes = SigafShapes,
        content = content
    )
}
