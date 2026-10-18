package com.sigaf.app.domain.repository

import com.sigaf.app.domain.model.AnalysisInput
import com.sigaf.app.domain.model.AnalysisResult

/**
 * Contrato para análisis de contenido sospechoso.
 */
interface AnalysisRepository {
    suspend fun analyze(input: AnalysisInput): AnalysisResult
}
