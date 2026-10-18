package com.sigaf.app.presentation.components

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sigaf.app.R
import com.sigaf.app.presentation.theme.CyanAccent

/**
 * Logo de SIGAF dibujado 100% en Jetpack Compose (sin imágenes):
 * escudo de seguridad con la "S" de SIGAF y un halo cian que respira.
 *
 * @param size lado cuadrático del logo.
 * @param pulseEnabled anima el halo (el Splash lo usa como "latido" de seguridad).
 */
@Composable
fun SigafLogo(
    size: Dp = 136.dp,
    pulseEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    val pulse by rememberInfiniteTransition(label = "logo_pulse").animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2_400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "halo_alpha"
    )
    val haloAlpha = if (pulseEnabled) 0.16f + 0.14f * pulse else 0.20f
    val cdLogo = stringResource(R.string.cd_logo)

    Box(
        modifier = modifier
            .size(size)
            .semantics { contentDescription = cdLogo },
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val w = this.size.width
            val h = this.size.height
            val centerX = w / 2f

            // Halo cian alrededor del escudo
            val haloRadius = w * 0.62f
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        CyanAccent.copy(alpha = haloAlpha),
                        CyanAccent.copy(alpha = 0f)
                    ),
                    center = Offset(centerX, h * 0.5f),
                    radius = haloRadius
                ),
                center = Offset(centerX, h * 0.5f),
                radius = haloRadius
            )

            // Escudo (path con curvas suaves)
            val shield = Path().apply {
                moveTo(centerX, h * 0.04f)
                lineTo(w * 0.93f, h * 0.17f)
                cubicTo(w * 0.93f, h * 0.55f, w * 0.86f, h * 0.78f, centerX, h * 0.97f)
                cubicTo(w * 0.14f, h * 0.78f, w * 0.07f, h * 0.55f, w * 0.07f, h * 0.17f)
                close()
            }

            // Relleno azul marino del escudo
            drawPath(
                path = shield,
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0B1E4E), Color(0xFF061032)),
                    startY = 0f,
                    endY = h
                )
            )
            // Contorno cian
            drawPath(
                path = shield,
                color = CyanAccent,
                style = Stroke(width = w * 0.022f)
            )
        }

        // "S" de SIGAF, ligeramente por debajo del centro óptico del escudo
        Text(
            text = "S",
            color = CyanAccent,
            fontWeight = FontWeight.ExtraBold,
            fontSize = (size.value * 0.40f).sp,
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = (size.value * 0.03f).dp)
        )
    }
}
