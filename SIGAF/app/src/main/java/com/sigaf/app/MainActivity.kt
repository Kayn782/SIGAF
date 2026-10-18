package com.sigaf.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.sigaf.app.presentation.navigation.SigafNavHost
import com.sigaf.app.presentation.theme.SigafTheme

/**
 * Única Activity de la app (Single Activity Architecture).
 * Toda la interfaz vive en Jetpack Compose y se navega con Navigation Compose.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // La UI se dibuja detrás de las barras del sistema

        setContent {
            SigafTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SigafNavHost(appContainer = applicationContext.sigafContainer)
                }
            }
        }
    }
}
