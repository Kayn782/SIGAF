package com.sigaf.app.presentation.navigation.bottom

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.sigaf.app.presentation.navigation.Routes

/**
 * Ítems de la barra de navegación inferior de SIGAF.
 */
sealed class BottomNavItem(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    data object Analyzer : BottomNavItem(
        route = Routes.ANALYZER,
        title = "Analizar",
        icon = Icons.Filled.Analytics
    )

    data object Alerts : BottomNavItem(
        route = Routes.ALERTS,
        title = "Alertas",
        icon = Icons.Filled.Notifications
    )

    data object History : BottomNavItem(
        route = Routes.HISTORY,
        title = "Historial",
        icon = Icons.Filled.History
    )

    data object Profile : BottomNavItem(
        route = Routes.PROFILE,
        title = "Perfil",
        icon = Icons.Filled.Person
    )

    companion object {
        val items = listOf(Analyzer, Alerts, History, Profile)
    }
}
