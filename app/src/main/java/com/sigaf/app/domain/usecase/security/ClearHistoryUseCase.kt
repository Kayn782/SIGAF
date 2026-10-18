package com.sigaf.app.domain.usecase.security

import com.sigaf.app.domain.repository.SecurityRepository

class ClearHistoryUseCase(
    private val repository: SecurityRepository
) {
    suspend operator fun invoke() = repository.clearHistory()
}
