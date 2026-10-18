package com.sigaf.app.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.sigaf.app.presentation.theme.CyanAccent
import com.sigaf.app.presentation.theme.NavyBackground
import com.sigaf.app.presentation.theme.NavyBackgroundBottom
import com.sigaf.app.presentation.theme.NavyBackgroundTop
import com.sigaf.app.presentation.theme.TextTertiary

/**
 * Fondo atmosférico de SIGAF.
 *
 * Compone tres capas para dar la sensación de "seguridad tecnológica"
 * basada en azules oscuros (sin negro plano):
 *
 *  1. Degradado vertical azul marino (superior → noche profunda).
 *  2. Halo radial cian muy tenue en la parte superior.
 *  3. Rejilla sutil apenas visible, como una malla de monitoreo.
 *
 * Sobre él se dibuja el contenido (el NavHost completo en [com.sigaf.app.presentation.SigafApp]).
 */
@Composable
fun SigafBackground(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(modifier = modifier.fillMaxSize()) {

        // 1. Degradado de fondo azul marino
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            NavyBackgroundTop,
                            NavyBackground,
                            NavyBackgroundBottom
                        )
                    )
                )
        )

        // 2 + 3. Halo cian y rejilla (dibujados en una sola pasada)
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Halo radial cian en la mitad superior
            val haloRadius = size.width * 1.1f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        CyanAccent.copy(alpha = 0.10f),
                        CyanAccent.copy(alpha = 0.0f)
                    ),
                    center = Offset(size.width / 2f, size.height * 0.16f),
                    radius = haloRadius
                ),
                center = Offset(size.width / 2f, size.height * 0.16f),
                radius = haloRadius
            )

            // Rejilla de monitoreo (muy tenue, no compite con el contenido)
            val lineColor = TextTertiary.copy(alpha = 0.10f)
            val step = size.width / 6f
            var x = step
            while (x < size.width) {
                drawLine(lineColor, start = Offset(x, 0f), end = Offset(x, size.height))
                x += step
            }
            val stepY = size.width / 6f
            var y = stepY
            while (y < size.height) {
                drawLine(lineColor, start = Offset(0f, y), end = Offset(size.width, y))
                y += stepY
            }
        }

        content()
    }
}
