package com.sigaf.app.presentation.theme

import androidx.compose.ui.graphics.Color

// =====================================================================
// PALETA SIGAF — Atmosfera "seguridad tecnológica" en azules.
//
// Regla de diseño: el fondo NUNCA es negro plano; toda la app respira
// tonos de azul marino / azul noche profundo. Los contenedores son
// azules oscuros translúcidos con borde sutil y elegante.
// =====================================================================

// --- Fondos generales (azul marino muy oscuro) -----------------------
val NavyBackgroundTop = Color(0xFF071633)      // Parte superior del degradado
val NavyBackground = Color(0xFF050D26)         // Fondo principal (pantallas)
val NavyBackgroundBottom = Color(0xFF02061A)   // Parte inferior (casi noche)

// --- Superficies y tarjetas (azul oscuro translúcido) ----------------
val NavySurface = Color(0xFF0A1330)            // Superficie elevada (barras, sheets)
val NavyCard = Color(0xFF0C1739)               // Fondo base de tarjetas
val NavyCardGradientTop = Color(0xFF0E1C46)    // Degradado superior de tarjetas
val NavyCardGradientBottom = Color(0xFF0A1132) // Degradado inferior de tarjetas
val NavyCardBorder = Color(0xFF1D2F66)         // Borde sutil y elegante
val NavyOutlineSoft = Color(0xFF16244F)        // Divisores / tracks

// --- Acento principal: azul cian brillante eléctrico -----------------
val CyanAccent = Color(0xFF00E5FF)             // #00E5FF — acciones principales
val CyanDeep = Color(0xFF00B8D4)               // Cian profundo (pressed/gradiente)
val CyanOnAccent = Color(0xFF03171E)           // Texto sobre botones cian
val CyanContainerDark = Color(0xFF063039)      // Contenedores/chips cian oscuros

// --- Texto sobre fondo azul ------------------------------------------
val TextPrimary = Color(0xFFE8F1FF)            // Blanco azulado (alto contraste)
val TextSecondary = Color(0xFF94A7D6)          // Azul grisáceo de apoyo
val TextTertiary = Color(0xFF5E71A6)           // Captions / deshabilitado

// =====================================================================
// PALETA DE ALERTAS / RIESGO
//   Crítico / Alto  → Rojo / Fucsia de advertencia
//   Moderado        → Ámbar / Amarillo
//   Seguro / Bajo   → Verde corporativo / Verde cian
// =====================================================================
val RiskCritical = Color(0xFFFF2E63)           // Crítico → fucsia de advertencia
val RiskHigh = Color(0xFFFF4D6D)               // Alto    → rojo
val RiskWarning = Color(0xFFFFB300)            // Moderado → ámbar
val RiskLow = Color(0xFF4DDFA0)                // Bajo    → verde cian
val RiskSafe = Color(0xFF1FD17A)               // Seguro  → verde corporativo
val RiskInsufficient = Color(0xFF7C8BC4)       // Información insuficiente → azul gris
