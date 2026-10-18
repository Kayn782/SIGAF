package com.sigaf.app.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sigaf.app.presentation.theme.NavyCardBorder
import com.sigaf.app.presentation.theme.NavySurface
import com.sigaf.app.presentation.theme.RiskSafe
import com.sigaf.app.presentation.theme.TextSecondary

/**
 * Viñeta con palomita en contenedor oscuro (pantalla de permisos).
 *
 * Icono de check (verde o cian) dentro de una caja redondeada oscura con
 * borde sutil, seguido del texto explicativo.
 *
 * @param checkColor color de la palomita: verde corporativo (seguridad)
 *                   o cian (información).
 */
@Composable
fun CheckBullet(
    text: String,
    modifier: Modifier = Modifier,
    checkColor: Color = RiskSafe
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Contenedor oscuro de la palomita
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(NavySurface)
                .border(width = 1.dp, color = NavyCardBorder, shape = RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                tint = checkColor,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}
