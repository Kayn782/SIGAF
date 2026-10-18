package com.sigaf.app.di

import android.content.Context
import com.sigaf.app.data.preference.OnboardingPreferencesDataSource
import com.sigaf.app.data.repository.AnalysisRepositoryImpl
import com.sigaf.app.data.repository.AuthRepositoryImpl
import com.sigaf.app.data.repository.OnboardingRepositoryImpl
import com.sigaf.app.data.repository.SecurityRepositoryImpl
import com.sigaf.app.domain.repository.AnalysisRepository
import com.sigaf.app.domain.repository.AuthRepository
import com.sigaf.app.domain.repository.OnboardingRepository
import com.sigaf.app.domain.repository.SecurityRepository
import com.sigaf.app.domain.usecase.CompleteWelcomeUseCase
import com.sigaf.app.domain.usecase.ObserveOnboardingStateUseCase
import com.sigaf.app.domain.usecase.SaveNotificationPermissionUseCase
import com.sigaf.app.domain.usecase.analysis.AnalyzeContentUseCase
import com.sigaf.app.domain.usecase.auth.LoginUseCase
import com.sigaf.app.domain.usecase.auth.RegisterUseCase
import com.sigaf.app.domain.usecase.auth.ResendCodeUseCase
import com.sigaf.app.domain.usecase.auth.VerifyCodeUseCase
import com.sigaf.app.domain.usecase.security.ClearHistoryUseCase
import com.sigaf.app.domain.usecase.security.MarkAlertReadUseCase
import com.sigaf.app.domain.usecase.security.ObserveAlertsUseCase
import com.sigaf.app.domain.usecase.security.ObserveHistoryUseCase

/**
 * Contenedor de dependencias (INYECCIÓN MANUAL).
 */
interface AppContainer {
    // Onboarding
    val observeOnboardingState: ObserveOnboardingStateUseCase
    val completeWelcome: CompleteWelcomeUseCase
    val saveNotificationPermission: SaveNotificationPermissionUseCase

    // Autenticación
    val loginUseCase: LoginUseCase
    val registerUseCase: RegisterUseCase
    val verifyCodeUseCase: VerifyCodeUseCase
    val resendCodeUseCase: ResendCodeUseCase

    // Análisis
    val analyzeContentUseCase: AnalyzeContentUseCase

    // Seguridad (Alertas + Historial)
    val observeAlerts: ObserveAlertsUseCase
    val observeHistory: ObserveHistoryUseCase
    val markAlertRead: MarkAlertReadUseCase
    val clearHistory: ClearHistoryUseCase
}

/** Implementación real del contenedor. */
class DefaultAppContainer(context: Context) : AppContainer {

    // --- Capa data -----------------------------------------------------
    private val preferences: OnboardingPreferencesDataSource by lazy {
        OnboardingPreferencesDataSource(context.applicationContext)
    }
    private val onboardingRepository: OnboardingRepository by lazy {
        OnboardingRepositoryImpl(preferences)
    }

    private val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl()
    }

    private val analysisRepository: AnalysisRepository by lazy {
        AnalysisRepositoryImpl()
    }

    private val securityRepository: SecurityRepository by lazy {
        SecurityRepositoryImpl()
    }

    // --- Casos de uso (capa domain) ------------------------------------
    // Onboarding
    override val observeOnboardingState: ObserveOnboardingStateUseCase by lazy {
        ObserveOnboardingStateUseCase(onboardingRepository)
    }
    override val completeWelcome: CompleteWelcomeUseCase by lazy {
        CompleteWelcomeUseCase(onboardingRepository)
    }
    override val saveNotificationPermission: SaveNotificationPermissionUseCase by lazy {
        SaveNotificationPermissionUseCase(onboardingRepository)
    }

    // Autenticación
    override val loginUseCase: LoginUseCase by lazy {
        LoginUseCase(authRepository)
    }
    override val registerUseCase: RegisterUseCase by lazy {
        RegisterUseCase(authRepository)
    }
    override val verifyCodeUseCase: VerifyCodeUseCase by lazy {
        VerifyCodeUseCase(authRepository)
    }
    override val resendCodeUseCase: ResendCodeUseCase by lazy {
        ResendCodeUseCase(authRepository)
    }

    // Análisis
    override val analyzeContentUseCase: AnalyzeContentUseCase by lazy {
        AnalyzeContentUseCase(analysisRepository)
    }

    // Seguridad (Alertas + Historial)
    override val observeAlerts: ObserveAlertsUseCase by lazy {
        ObserveAlertsUseCase(securityRepository)
    }
    override val observeHistory: ObserveHistoryUseCase by lazy {
        ObserveHistoryUseCase(securityRepository)
    }
    override val markAlertRead: MarkAlertReadUseCase by lazy {
        MarkAlertReadUseCase(securityRepository)
    }
    override val clearHistory: ClearHistoryUseCase by lazy {
        ClearHistoryUseCase(securityRepository)
    }
}
