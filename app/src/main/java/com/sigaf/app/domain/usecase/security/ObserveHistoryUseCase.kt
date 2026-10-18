package com.sigaf.app.domain.usecase.security

import com.sigaf.app.domain.model.HistoryEntry
import com.sigaf.app.domain.repository.SecurityRepository
import kotlinx.coroutines.flow.Flow

class ObserveHistoryUseCase(
    private val repository: SecurityRepository
) {
    operator fun invoke(): Flow<List<HistoryEntry>> = repository.observeHistory()
}
