package com.sigaf.app.presentation.screens.permissions

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sigaf.app.R
import com.sigaf.app.presentation.components.CheckBullet
import com.sigaf.app.presentation.components.SigafCard
import com.sigaf.app.presentation.components.SigafPrimaryButton
import com.sigaf.app.presentation.components.SigafSecondaryButton
import com.sigaf.app.presentation.theme.CyanAccent
import com.sigaf.app.presentation.theme.CyanContainerDark
import com.sigaf.app.presentation.theme.NavyBackground
import com.sigaf.app.presentation.theme.RiskSafe
import com.sigaf.app.presentation.theme.TextPrimary
import com.sigaf.app.presentation.theme.TextSecondary
import com.sigaf.app.presentation.theme.TextTertiary

/**
 * PANTALLA 3 — Permisos de notificaciones.
 *
 * - Fondo azul oscuro con icono de campana superior en contenedor redondeado.
 * - Título "¿Permitir lectura de notificaciones?" + texto explicativo.
 * - Tres viñetas con palomitas en contenedores oscuros (privacidad y control).
 * - Acciones: "Otorgar" (botón principal destacado en blanco/claro) y
 *   "Denegar" (botón secundario con borde sutil).
 * - Nota legal inferior: "Nunca leemos tus mensajes sin tu permiso."
 *
 * La zona central es desplazable y las acciones quedan fijas abajo, así
 * el botón "Otorgar" siempre está a la vista en pantallas pequeñas.
 */
@Composable
fun PermissionsScreen(
    onDecisionMade: () -> Unit
) {
    val viewModel: PermissionsViewModel = viewModel(factory = PermissionsViewModel.Factory)

    // Efecto de un solo disparo: navega UNA vez cuando la decisión se guarda
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                PermissionsViewModel.Event.DecisionSaved -> onDecisionMade()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp)
    ) {

        // ========== Zona central desplazable (icono, textos, viñetas) ==========
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Icono de campana en contenedor de bordes redondeados
            Box(
                modifier = Modifier
                    .size(78.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(CyanContainerDark)
                    .border(
                        width = 1.dp,
                        color = CyanAccent.copy(alpha = 0.35f),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.NotificationsActive,
                    contentDescription = stringResource(R.string.cd_permissions_icon),
                    tint = CyanAccent,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = stringResource(R.string.permissions_title),
                style = MaterialTheme.typography.headlineLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.permissions_body),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(26.dp))

            // Tres viñetas dentro de una tarjeta azul translúcida
            SigafCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 18.dp, vertical = 20.dp)
            ) {
                CheckBullet(
                    text = stringResource(R.string.permissions_bullet_private),
                    checkColor = RiskSafe
                )
                Spacer(modifier = Modifier.height(14.dp))
                CheckBullet(
                    text = stringResource(R.string.permissions_bullet_no_messages),
                    checkColor = CyanAccent
                )
                Spacer(modifier = Modifier.height(14.dp))
                CheckBullet(
                    text = stringResource(R.string.permissions_bullet_revoke),
                    checkColor = RiskSafe
                )
            }

            Spacer(modifier = Modifier.height(20.dp))
        }

        // ========== Acciones fijas + nota legal ==========
        SigafPrimaryButton(
            text = stringResource(R.string.permissions_grant),
            onClick = viewModel::onGrant,
            // Estilo botón principal destacado en blanco/claro
            containerColor = Color(0xFFF3F8FF),
            contentColor = NavyBackground
        )

        Spacer(modifier = Modifier.height(12.dp))

        SigafSecondaryButton(
            text = stringResource(R.string.permissions_deny),
            onClick = viewModel::onDeny
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Lock,
                contentDescription = null,
                tint = TextTertiary,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.permissions_legal),
                style = MaterialTheme.typography.labelSmall,
                color = TextTertiary,
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
    }
}
