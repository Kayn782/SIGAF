package com.sigaf.app.domain.usecase.analysis

import com.sigaf.app.domain.model.AnalysisInput
import com.sigaf.app.domain.model.AnalysisResult
import com.sigaf.app.domain.repository.AnalysisRepository

class AnalyzeContentUseCase(
    private val repository: AnalysisRepository
) {
    suspend operator fun invoke(input: AnalysisInput): AnalysisResult = repository.analyze(input)
}
