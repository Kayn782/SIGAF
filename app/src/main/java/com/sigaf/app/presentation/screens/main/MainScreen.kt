package com.sigaf.app.presentation.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sigaf.app.presentation.navigation.Routes
import com.sigaf.app.presentation.navigation.bottom.SigafBottomNav
import com.sigaf.app.presentation.screens.alerts.AlertsScreen
import com.sigaf.app.presentation.screens.analyzer.AnalyzerScreen
import com.sigaf.app.presentation.screens.history.HistoryScreen
import com.sigaf.app.presentation.screens.home.HomeScreen
import com.sigaf.app.presentation.screens.profile.ProfileScreen

/**
 * Pantalla principal con barra inferior (Bottom Navigation).
 * Contiene: Inicio/Home, Analizar, Alertas, Historial, Perfil.
 */
@Composable
fun MainScreen(
    onLogout: () -> Unit = {}
) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            SigafBottomNav(navController = navController)
        },
        containerColor = androidx.compose.ui.graphics.Color.Transparent
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            composable(Routes.HOME) {
                HomeScreen(
                    onNavigateToAnalyzer = { navController.navigate(Routes.ANALYZER) },
                    onNavigateToAlerts = { navController.navigate(Routes.ALERTS) },
                    onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                    onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
                )
            }
            composable(Routes.ANALYZER) {
                AnalyzerScreen(onBack = null)
            }
            composable(Routes.ALERTS) {
                AlertsScreen(onBack = null)
            }
            composable(Routes.HISTORY) {
                HistoryScreen(onBack = null)
            }
            composable(Routes.PROFILE) {
                ProfileScreen(onBack = null)
            }
        }
    }
}
