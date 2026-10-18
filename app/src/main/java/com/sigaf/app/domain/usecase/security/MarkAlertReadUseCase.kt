package com.sigaf.app.domain.usecase.security

import com.sigaf.app.domain.repository.SecurityRepository

class MarkAlertReadUseCase(
    private val repository: SecurityRepository
) {
    suspend operator fun invoke(alertId: String) = repository.markAlertRead(alertId)
}
