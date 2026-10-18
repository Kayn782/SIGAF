package com.sigaf.app.domain.model

/** Aviso del Centro de Alertas clasificado por su nivel de riesgo. */
data class AlertItem(
    val id: String,
    val title: String,
    val description: String,
    val level: RiskLevel,
    val time: String,
    val source: String
)
