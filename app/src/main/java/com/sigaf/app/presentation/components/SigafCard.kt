package com.sigaf.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sigaf.app.presentation.theme.NavyCardBorder
import com.sigaf.app.presentation.theme.NavyCardGradientBottom
import com.sigaf.app.presentation.theme.NavyCardGradientTop

/**
 * Tarjeta base de SIGAF: fondo azul oscuro translúcido con degradado,
 * borde sutil y elegante y esquinas redondeadas.
 *
 * @param onClick si se pasa, la tarjeta completa es clicable (ripple acotado).
 * @param borderColor borde exterior (por defecto el sutil de las tarjetas;
 *                    una selección activa lo pinta de cian).
 * @param gradientAlpha opacidad del degradado (menos de 1.0 deja ver
 *                      el fondo azul marino por detrás: "translúcido").
 */
@Composable
fun SigafCard(
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    borderColor: Color = NavyCardBorder,
    gradientAlpha: Float = 0.92f,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(20.dp)
    val interactionSource = remember { MutableInteractionSource() }

    var cardModifier = modifier
        .clip(shape)
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    NavyCardGradientTop.copy(alpha = gradientAlpha),
                    NavyCardGradientBottom.copy(alpha = gradientAlpha)
                )
            )
        )
        .border(width = 1.dp, color = borderColor, shape = shape)

    if (onClick != null) {
        cardModifier = cardModifier.clickable(
            interactionSource = interactionSource,
            indication = ripple(),
            onClick = onClick
        )
    }

    Column(
        modifier = cardModifier.padding(contentPadding),
        content = content
    )
}
