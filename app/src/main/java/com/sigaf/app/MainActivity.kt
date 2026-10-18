package com.sigaf.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sigaf.app.presentation.SigafApp
import com.sigaf.app.presentation.theme.SigafTheme

/**
 * Punto de entrada único de SIGAF.
 *
 * No hay Fragment ni layout XML: Jetpack Compose dibuja TODA la UI.
 * La Activity no inyecta nada; el contenedor de dependencias vive en
 * [SigafApplication] y lo consumen los ViewModels desde sus Factories.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Dibuja la app detrás de las barras del sistema (pantalla completa)
        // para que el fondo azul marino se extienda de borde a borde.
        enableEdgeToEdge()

        setContent {
            // Modo oscuro nativo por defecto (requisito de diseño):
            // fondo azul marino profundo + acentos cian.
            SigafTheme {
                SigafApp()
            }
        }
    }
}
