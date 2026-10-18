package com.sigaf.app.domain.model

/**
 * Resultado de un análisis realizado por SIGAF.
 *
 * @param score puntaje de riesgo de 0 a 100 (más alto = más peligroso).
 * @param level nivel traducido del puntaje para el usuario.
 * @param summary veredicto corto que se muestra en la tarjeta de resultado.
 * @param recommendations consejos concretos que el usuario debe seguir.
 */
data class AnalysisResult(
    val id: String,
    val input: String,
    val type: AnalysisType,
    val score: Int,
    val level: RiskLevel,
    val summary: String,
    val date: String,
    val recommendations: List<String>
)
