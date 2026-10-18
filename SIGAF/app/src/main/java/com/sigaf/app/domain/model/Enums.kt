package com.sigaf.app.domain.model

/**
 * Nivel de riesgo de un análisis o de una alerta.
 * Ordenado de más grave a más seguro (se usa para ordenar listas).
 */
enum class RiskLevel(val label: String) {
    CRITICAL("Crítico"),
    HIGH("Alto"),
    MEDIUM("Moderado"),
    LOW("Bajo"),
    SAFE("Seguro")
}

/** Tipo de contenido que el usuario puede analizar en la pestaña "Analizar". */
enum class AnalysisType(val label: String) {
    MESSAGE("Mensaje"),
    LINK("Enlace"),
    SENDER("Remitente")
}

/** Tipo de elemento bloqueado en el Centro de Seguridad. */
enum class BlockType(val label: String) {
    SENDER("Remitente"),
    LINK("Enlace")
}
