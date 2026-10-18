package com.sigaf.app.presentation.components

import androidx.compose.ui.graphics.Color
import com.sigaf.app.domain.model.RiskLevel
import com.sigaf.app.presentation.theme.RiskCritical
import com.sigaf.app.presentation.theme.RiskHigh
import com.sigaf.app.presentation.theme.RiskInsufficient
import com.sigaf.app.presentation.theme.RiskSafe
import com.sigaf.app.presentation.theme.RiskWarning

/**
 * Color asociado a cada nivel de riesgo (paleta global de alertas).
 */
fun RiskLevel.color(): Color = when (this) {
    RiskLevel.CRITICAL -> RiskCritical
    RiskLevel.HIGH -> RiskHigh
    RiskLevel.MEDIUM -> RiskWarning
    RiskLevel.LOW -> RiskSafe
    RiskLevel.INSUFFICIENT -> RiskInsufficient
}

/**
 * Etiqueta legible de cada nivel de riesgo.
 */
fun RiskLevel.label(): String = when (this) {
    RiskLevel.CRITICAL -> "Crítico"
    RiskLevel.HIGH -> "Alto"
    RiskLevel.MEDIUM -> "Moderado"
    RiskLevel.LOW -> "Bajo"
    RiskLevel.INSUFFICIENT -> "Insuficiente"
}

/**
 * Tiempo relativo simple ("hace 3 min", "hace 2 h", "ayer").
 */
fun relativeTime(timestamp: Long): String {
    val diff = System.currentTimeMillis() - timestamp
    val minutes = diff / 60_000L
    val hours = minutes / 60
    val days = hours / 24
    return when {
        minutes < 1 -> "ahora"
        minutes < 60 -> "hace ${minutes} min"
        hours < 24 -> "hace $hours h"
        days < 2 -> "ayer"
        else -> "hace $days días"
    }
}
