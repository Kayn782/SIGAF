package com.sigaf.app.domain.model

/**
 * Pestañas disponibles en la pantalla Analizar.
 */
enum class AnalysisTab {
    MESSAGE,
    URL,
    SENDER
}

/**
 * Resultado de análisis simulado (MVP front-end).
 */
data class AnalysisInput(
    val text: String,
    val tab: AnalysisTab
)

data class AnalysisResult(
    val score: Int,
    val riskLevel: RiskLevel,
    val verdict: String,
    val recommendations: List<String>
)
