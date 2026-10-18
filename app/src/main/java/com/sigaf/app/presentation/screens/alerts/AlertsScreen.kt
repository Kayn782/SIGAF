package com.sigaf.app.presentation.screens.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sigaf.app.domain.model.Alert
import com.sigaf.app.presentation.components.SigafCard
import com.sigaf.app.presentation.components.SigafTopHeader
import com.sigaf.app.presentation.components.color
import com.sigaf.app.presentation.components.label
import com.sigaf.app.presentation.components.relativeTime
import com.sigaf.app.presentation.theme.CyanAccent
import com.sigaf.app.presentation.theme.NavyCardBorder
import com.sigaf.app.presentation.theme.TextPrimary
import com.sigaf.app.presentation.theme.TextSecondary
import com.sigaf.app.presentation.theme.TextTertiary

@Composable
fun AlertsScreen(
    onBack: (() -> Unit)? = null,
    viewModel: AlertsViewModel = viewModel(factory = AlertsViewModel.Factory)
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        SigafTopHeader(
            title = "CENTRO DE ALERTAS",
            onBack = onBack,
            subtitle = "Avisos de actividad sospechosa detectada"
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Contador de no leídas
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.NotificationsActive,
                contentDescription = null,
                tint = CyanAccent,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "${uiState.unreadCount} sin leer · ${uiState.alerts.size} en total",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (uiState.alerts.isEmpty()) {
            EmptyAlerts()
        } else {
            uiState.alerts.forEach { alert ->
                AlertItem(
                    alert = alert,
                    onClick = { viewModel.onAlertClick(alert) }
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@Composable
private fun AlertItem(alert: Alert, onClick: () -> Unit) {
    val riskColor = alert.riskLevel.color()

    SigafCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        borderColor = if (alert.isRead) NavyCardBorder else riskColor.copy(alpha = 0.7f),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(
            horizontal = 18.dp,
            vertical = 16.dp
        )
    ) {
        Row(verticalAlignment = Alignment.Top) {
            // Punto indicador de estado (no leída = cian brillante)
            Box(
                modifier = Modifier
                    .padding(top = 6.dp)
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (alert.isRead) TextTertiary else CyanAccent)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = alert.title,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = if (alert.isRead) FontWeight.Normal else FontWeight.Bold
                        ),
                        color = TextPrimary,
                        modifier = Modifier.weight(1f)
                    )

                    // Chip de nivel de riesgo
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(riskColor.copy(alpha = 0.15f))
                            .border(1.dp, riskColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = alert.riskLevel.label(),
                            style = MaterialTheme.typography.labelSmall,
                            color = riskColor
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = alert.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = alert.source,
                        style = MaterialTheme.typography.labelSmall,
                        color = CyanAccent
                    )
                    Text(
                        text = "  ·  ${relativeTime(alert.timestamp)}",
                        style = MaterialTheme.typography.labelSmall,
                        color = TextTertiary
                    )
                }
            }
        }
    }
}

@Composable
private fun EmptyAlerts() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 60.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.NotificationsActive,
            contentDescription = null,
            tint = TextTertiary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Sin alertas por ahora",
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Te avisaremos aquí cuando detectemos algo sospechoso.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )
    }
}
