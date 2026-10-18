package com.sigaf.app.presentation.theme

import androidx.compose.ui.graphics.Color

/**
 * Paleta oficial de SIGAF — Modo oscuro absoluto.
 *
 * Reglas de marca:
 *  - Fondo general negro profundo.
 *  - Tarjetas grises oscuros translúcidos con borde sutil.
 *  - Cian (#00E5FF) para acciones principales y elementos activos.
 *  - Rojo/Fucsia = crítico, Ámbar = advertencia, Verde = seguro.
 */
object SigafColors {

    // Fondos y contenedores
    val Black = Color(0xFF000000)
    val Background = Color(0xFF0A0D14)
    val Surface = Color(0xFF11151E)
    val SurfaceHigh = Color(0xFF171D29)
    val SurfaceVariant = Color(0xFF1D2432)
    val Border = Color(0xFF242C3C)
    val BorderSoft = Color(0xFF1B2230)

    // Acento principal (cian / turquesa)
    val Cyan = Color(0xFF00E5FF)
    val CyanDark = Color(0xFF00B8D4)
    val OnCyan = Color(0xFF00141A)

    // Paleta de riesgo
    val Critical = Color(0xFFFF2E63) // Crítico / Alto -> rojo fucsia
    val High = Color(0xFFFF6B8B)     // Alto (variante rosa)
    val Warning = Color(0xFFFFB300)  // Moderado / advertencia -> ámbar
    val Low = Color(0xFF4DDFA0)      // Bajo -> verde claro
    val Safe = Color(0xFF1FD17A)     // Seguro -> verde corporativo

    // Texto
    val White = Color(0xFFFFFFFF)
    val TextPrimary = Color(0xFFF4F6FA)
    val TextSecondary = Color(0xFF8B94A7)
    val TextTertiary = Color(0xFF5A6376)
}
