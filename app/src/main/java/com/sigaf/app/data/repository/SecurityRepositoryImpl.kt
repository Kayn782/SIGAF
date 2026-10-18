package com.sigaf.app.data.repository

import com.sigaf.app.domain.model.Alert
import com.sigaf.app.domain.model.AlertType
import com.sigaf.app.domain.model.HistoryEntry
import com.sigaf.app.domain.model.RiskLevel
import com.sigaf.app.domain.repository.SecurityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Implementación fake para MVP (sin backend ni Room).
 * Devuelve datos simulados y permite marcar alertas como leídas.
 */
class SecurityRepositoryImpl : SecurityRepository {

    private val now = System.currentTimeMillis()
    private val minute = 60_000L

    private val _alerts = MutableStateFlow(
        listOf(
            Alert(
                id = "a1",
                title = "Posible phishing bancario",
                description = "Un mensaje suplanta a tu banco y pide tu clave por enlace.",
                alertType = AlertType.PHISHING,
                riskLevel = RiskLevel.CRITICAL,
                source = "Mensaje",
                timestamp = now - 3 * minute
            ),
            Alert(
                id = "a2",
                title = "Enlace malicioso detectado",
                description = "URL con dominio sospechoso que imita a un sitio oficial.",
                alertType = AlertType.MALICIOUS_URL,
                riskLevel = RiskLevel.HIGH,
                source = "Enlace",
                timestamp = now - 25 * minute,
                isRead = true
            ),
            Alert(
                id = "a3",
                title = "Remitente desconocido sospechoso",
                description = "Número no registrado con patrón de estafa premio.",
                alertType = AlertType.SUSPICIOUS_SENDER,
                riskLevel = RiskLevel.MEDIUM,
                source = "Remitente",
                timestamp = now - 90 * minute
            ),
            Alert(
                id = "a4",
                title = "Intento de estafa verificado",
                description = "Confirmado fraude de transferencia urgente.",
                alertType = AlertType.SCAM,
                riskLevel = RiskLevel.CRITICAL,
                source = "Mensaje",
                timestamp = now - 5 * 60 * minute,
                isRead = true
            )
        )
    )

    private val _history = MutableStateFlow(
        listOf(
            HistoryEntry(
                id = "h1",
                content = "Urgente, transfiera a esta cuenta...",
                source = "Mensaje",
                score = 88,
                riskLevel = RiskLevel.CRITICAL,
                verdict = "Alto riesgo de fraude",
                timestamp = now - 10 * minute
            ),
            HistoryEntry(
                id = "h2",
                content = "https://premio-ganador-free.com",
                source = "Enlace",
                score = 80,
                riskLevel = RiskLevel.HIGH,
                verdict = "Sospechoso",
                timestamp = now - 40 * minute
            ),
            HistoryEntry(
                id = "h3",
                content = "+57 300 000 0000",
                source = "Remitente",
                score = 70,
                riskLevel = RiskLevel.HIGH,
                verdict = "Sospechoso",
                timestamp = now - 2 * 60 * minute
            ),
            HistoryEntry(
                id = "h4",
                content = "Hola, ¿cómo estás? Te llamo luego.",
                source = "Mensaje",
                score = 15,
                riskLevel = RiskLevel.LOW,
                verdict = "Seguridad aceptable",
                timestamp = now - 6 * 60 * minute
            )
        )
    )

    override fun observeAlerts(): Flow<List<Alert>> = _alerts

    override fun observeHistory(): Flow<List<HistoryEntry>> = _history.map { list ->
        list.sortedByDescending { it.timestamp }
    }

    override suspend fun markAlertRead(alertId: String) {
        _alerts.update { list ->
            list.map { if (it.id == alertId) it.copy(isRead = true) else it }
        }
    }

    override suspend fun clearHistory() {
        _history.update { emptyList() }
    }
}
