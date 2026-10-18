package com.sigaf.app.domain.repository

import com.sigaf.app.domain.model.Alert
import com.sigaf.app.domain.model.HistoryEntry
import kotlinx.coroutines.flow.Flow

/**
 * Contrato del repositorio de alertas y historial.
 * En el MVP devuelve datos simulados; se sustituirá por backend/Room.
 */
interface SecurityRepository {
    fun observeAlerts(): Flow<List<Alert>>
    fun observeHistory(): Flow<List<HistoryEntry>>

    suspend fun markAlertRead(alertId: String)
    suspend fun clearHistory()
}
