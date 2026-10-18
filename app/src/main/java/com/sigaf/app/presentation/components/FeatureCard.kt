package com.sigaf.app.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sigaf.app.presentation.theme.CyanAccent
import com.sigaf.app.presentation.theme.NavyCardBorder
import com.sigaf.app.presentation.theme.NavyCardGradientBottom
import com.sigaf.app.presentation.theme.NavyCardGradientTop
import com.sigaf.app.presentation.theme.NavySurface
import com.sigaf.app.presentation.theme.TextPrimary
import com.sigaf.app.presentation.theme.TextSecondary

/**
 * Tarjeta horizontal de la pantalla de bienvenida
 * ("Detecta", "Analiza", "Previene").
 *
 * El icono vive en un contenedor de bordes redondeados y la tarjeta es
 * interactiva: al tocarla se pinta su borde de cian (selección activa)
 * con una transición de color suave.
 *
 * @param icon icono representativo de la capacidad.
 * @param title nombre corto de la capacidad.
 * @param description promesa de valor ("Amenazas a tiempo", …).
 * @param selected estado de selección visual.
 */
@Composable
fun FeatureCard(
    icon: ImageVector,
    title: String,
    description: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = if (selected) CyanAccent else NavyCardBorder,
        animationSpec = tween(durationMillis = 250),
        label = "feature_border"
    )
    val iconContainerColor by animateColorAsState(
        targetValue = if (selected) CyanAccent.copy(alpha = 0.14f) else NavySurface,
        animationSpec = tween(durationMillis = 250),
        label = "feature_icon_bg"
    )
    val iconTint by animateColorAsState(
        targetValue = if (selected) CyanAccent else TextSecondary,
        animationSpec = tween(durationMillis = 250),
        label = "feature_icon_tint"
    )

    val shape = RoundedCornerShape(18.dp)

    Column(
        modifier = modifier
            .clip(shape)
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        NavyCardGradientTop.copy(alpha = 0.92f),
                        NavyCardGradientBottom.copy(alpha = 0.92f)
                    )
                )
            )
            .border(width = 1.5.dp, color = borderColor, shape = shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Contenedor del icono (bordes redondeados)
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(iconContainerColor)
                .border(width = 1.dp, color = borderColor.copy(alpha = if (selected) 0.9f else 0.6f), shape = RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null, // el texto de la tarjeta ya lo describe
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
