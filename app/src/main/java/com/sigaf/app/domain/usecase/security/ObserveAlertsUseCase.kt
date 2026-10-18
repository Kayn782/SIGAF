package com.sigaf.app.domain.usecase.security

import com.sigaf.app.domain.model.Alert
import com.sigaf.app.domain.repository.SecurityRepository
import kotlinx.coroutines.flow.Flow

class ObserveAlertsUseCase(
    private val repository: SecurityRepository
) {
    operator fun invoke(): Flow<List<Alert>> = repository.observeAlerts()
}
