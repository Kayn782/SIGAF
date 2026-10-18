package com.sigaf.app.presentation.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Esquema oscuro nativo de SIGAF con fondo AZUL MARINO (nunca negro
 * plano) y acento cian eléctrico (#00E5FF) para elementos interactivos.
 */
private val SigafDarkColorScheme = darkColorScheme(
    primary = CyanAccent,
    onPrimary = CyanOnAccent,
    primaryContainer = CyanContainerDark,
    onPrimaryContainer = CyanAccent,
    secondary = CyanDeep,
    onSecondary = CyanOnAccent,
    secondaryContainer = CyanContainerDark,
    onSecondaryContainer = TextPrimary,
    tertiary = RiskSafe,
    onTertiary = CyanOnAccent,
    tertiaryContainer = NavyCard,
    onTertiaryContainer = RiskSafe,
    background = NavyBackground,
    onBackground = TextPrimary,
    surface = NavySurface,
    onSurface = TextPrimary,
    surfaceVariant = NavyCard,
    onSurfaceVariant = TextSecondary,
    surfaceTint = CyanAccent,
    outline = NavyOutlineSoft,
    outlineVariant = NavyCardBorder,
    error = RiskCritical,
    onError = Color.White,
    errorContainer = Color(0xFF3A0A18),
    onErrorContainer = Color(0xFFFFB3C1),
    // El scrim/overlay por defecto de Material3 ya es semitransparente;
    // no se personaliza para no romper los ripples y bottom sheets.
    inverseSurface = TextPrimary,
    inverseOnSurface = NavyBackground
)

/**
 * Tema visual de SIGAF.
 *
 * SIGAF nace en modo oscuro nativo (requisito de diseño). Cuando exista la
 * preferencia de apariencia en DataStore, aquí se decidirá entre
 * oscuro/claro/sistema; de momento siempre se aplica el esquema azul marino.
 *
 * @param content Raíz de la UI (siempre [com.sigaf.app.presentation.SigafApp]).
 */
@Composable
fun SigafTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SigafDarkColorScheme,
        typography = SigafTypography,
        shapes = SigafShapes,
        content = content
    )
}
