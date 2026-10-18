package com.sigaf.app.domain.repository

import com.sigaf.app.domain.model.AlertItem
import com.sigaf.app.domain.model.AnalysisResult
import com.sigaf.app.domain.model.AnalysisType
import com.sigaf.app.domain.model.BlockedItem
import com.sigaf.app.domain.model.SecurityTip
import com.sigaf.app.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

/** Contrato del motor de análisis y gestión de riesgos de SIGAF. */
interface AnalysisRepository {

    fun observeAnalyses(): Flow<List<AnalysisResult>>

    fun observeAlerts(): Flow<List<AlertItem>>

    fun observeBlocked(): Flow<List<BlockedItem>>

    fun observeTips(): Flow<List<SecurityTip>>

    fun observeProfile(): Flow<UserProfile>

    fun analysisById(id: String): Flow<AnalysisResult?>

    /** Ejecuta el análisis y guarda el resultado en el historial. */
    suspend fun analyze(input: String, type: AnalysisType): AnalysisResult

    suspend fun markSafe(id: String)

    suspend fun deleteAnalysis(id: String)

    suspend fun addBlocked(value: String, type: com.sigaf.app.domain.model.BlockType)

    suspend fun removeBlocked(id: String)
}
