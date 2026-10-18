package com.sigaf.app.domain.model

/**
 * Tipo de evento/amenaza que dispara una alerta.
 */
enum class AlertType {
    PHISHING,      // Suplantación de identidad
    SCAM,          // Estafa / fraude económico
    MALICIOUS_URL, // Enlace malicioso
    SUSPICIOUS_SENDER, // Remitente sospechoso
    UNKNOWN
}

/**
 * Alerta mostrada en el Centro de Alertas.
 */
data class Alert(
    val id: String,
    val title: String,
    val description: String,
    val alertType: AlertType,
    val riskLevel: RiskLevel,
    val source: String,          // Origen: "Mensaje", "Enlace", "Remitente"
    val timestamp: Long,         // Época (ms)
    val isRead: Boolean = false
)

/**
 * Entrada del historial de análisis realizados por el usuario.
 */
data class HistoryEntry(
    val id: String,
    val content: String,         // Texto/URL/remitente analizado
    val source: String,          // Pestaña de origen
    val score: Int,
    val riskLevel: RiskLevel,
    val verdict: String,
    val timestamp: Long
)
