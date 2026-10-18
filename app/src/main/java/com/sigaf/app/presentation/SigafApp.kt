package com.sigaf.app.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.sigaf.app.presentation.components.SigafBackground
import com.sigaf.app.presentation.navigation.SigafNavHost

/**
 * Raíz de la UI de SIGAF.
 *
 * Envuelve TODO el grafo de navegación en [SigafBackground] para que el
 * fondo azul marino (con halo cian y rejilla) sea único e infinito:
 * las transiciones entre pantallas nunca exponen otro color de fondo.
 */
@Composable
fun SigafApp() {
    val navController = rememberNavController()

    SigafBackground {
        SigafNavHost(navController = navController)
    }
}
