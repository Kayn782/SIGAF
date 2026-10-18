package com.sigaf.app.presentation.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.sigaf.app.R
import com.sigaf.app.presentation.screens.common.PlaceholderScreen
import com.sigaf.app.presentation.screens.permissions.PermissionsScreen
import com.sigaf.app.presentation.screens.splash.SplashScreen
import com.sigaf.app.presentation.screens.welcome.WelcomeScreen
import com.sigaf.app.presentation.screens.analyzer.AnalyzerScreen
import com.sigaf.app.presentation.screens.alerts.AlertsScreen
import com.sigaf.app.presentation.screens.history.HistoryScreen
import com.sigaf.app.presentation.screens.home.HomeScreen
import com.sigaf.app.presentation.screens.profile.ProfileScreen
import com.sigaf.app.presentation.screens.main.MainScreen

/**
 * Grafo de navegación de SIGAF.
 *
 * Flujo inicial implementado:
 *  1. SPLASH      → "INGRESAR AL SISTEMA >"
 *  2. WELCOME     → "COMENZAR >"  (o "YA TENGO UNA CUENTA" → LOGIN)
 *  3. PERMISSIONS → "Otorgar" / "Denegar" → LOGIN
 *
 * Las pantallas de autenticación y el menú principal están registradas como
 * provisionales ([PlaceholderScreen]) hasta el siguiente bloque.
 *
 * Transiciones: fundido suave (300 ms) para que el cambio de pantalla se
 * sienta continuo sobre el mismo fondo azul marino.
 */
@Composable
fun SigafNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier,
        enterTransition = { fadeIn(animationSpec = tween(300)) },
        exitTransition = { fadeOut(animationSpec = tween(220)) },
        popEnterTransition = { fadeIn(animationSpec = tween(300)) },
        popExitTransition = { fadeOut(animationSpec = tween(220)) }
    ) {
        // 1. INICIO / SPLASH — logo, títulos institucionales e ingreso
        composable(Routes.SPLASH) {
            SplashScreen(
                onEnterSystem = { destination ->
                    navController.navigate(destination) {
                        // El splash nunca debe volver con "atrás"
                        popUpTo(Routes.SPLASH) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 2. BIENVENIDA — onboarding con Detecta / Analiza / Previene
        composable(Routes.WELCOME) {
            WelcomeScreen(
                onStart = {
                    // Se mantiene el backstack: "atrás" desde permisos
                    // regresa a la bienvenida (flujo lineal predecible).
                    navController.navigate(Routes.PERMISSIONS) {
                        launchSingleTop = true
                    }
                },
                onLogin = {
                    navController.navigate(Routes.LOGIN) {
                        launchSingleTop = true
                    }
                }
            )
        }

        // 3. PERMISOS — lectura de notificaciones (Otorgar / Denegar)
        composable(Routes.PERMISSIONS) {
            PermissionsScreen(
                onDecisionMade = {
                    // Onboarding completo: la bienvenida queda fuera del
                    // backstack y el destino es el inicio de sesión.
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.WELCOME) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 4. INICIO DE SESIÓN
        composable(Routes.LOGIN) {
            com.sigaf.app.presentation.screens.auth.login.LoginScreen(
                onNavigateToRegister = { navController.navigate(Routes.REGISTER) },
                onLoginSuccess = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            )
        }

        // 5. REGISTRO
        composable(Routes.REGISTER) {
            com.sigaf.app.presentation.screens.auth.register.RegisterScreen(
                onBack = { navController.navigateUp() },
                onRegisterSuccess = { navController.navigate(Routes.VERIFY) }
            )
        }

        // 6. VERIFICACIÓN DE CORREO
        composable(Routes.VERIFY) {
            com.sigaf.app.presentation.screens.auth.verify.VerifyScreen(
                onBack = { navController.navigateUp() },
                onVerified = {
                    navController.navigate(Routes.MAIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                        popUpTo(Routes.VERIFY) { inclusive = true }
                        launchSingleTop = true
                    }
                },
                onResend = { }
            )
        }

        // 7. MENÚ PRINCIPAL CON BOTTOM NAV
        composable(Routes.MAIN) {
            MainScreen()
        }

        // HOME/ANALYZER/ALERTS/HISTORY/PROFILE aún accesibles desde el grafo si se navega directamente
        composable(Routes.HOME) {
            HomeScreen(
                onNavigateToAnalyzer = { navController.navigate(Routes.ANALYZER) },
                onNavigateToAlerts = { navController.navigate(Routes.ALERTS) },
                onNavigateToHistory = { navController.navigate(Routes.HISTORY) },
                onNavigateToProfile = { navController.navigate(Routes.PROFILE) }
            )
        }

        // 8. ANALIZAR (Bloque 3)
        composable(Routes.ANALYZER) {
            AnalyzerScreen(
                onBack = { navController.navigateUp() }
            )
        }

        // 9. ALERTAS (Bloque 4 — Centro de Alertas)
        composable(Routes.ALERTS) {
            AlertsScreen(
                onBack = { navController.navigateUp() }
            )
        }

        // 10. HISTORIAL (Bloque 4)
        composable(Routes.HISTORY) {
            HistoryScreen(
                onBack = { navController.navigateUp() }
            )
        }

        // 11. PERFIL
        composable(Routes.PROFILE) {
            ProfileScreen(
                onBack = { navController.navigateUp() }
            )
        }
    }
}

