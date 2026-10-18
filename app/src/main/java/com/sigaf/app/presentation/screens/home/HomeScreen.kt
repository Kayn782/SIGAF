package com.sigaf.app.presentation.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.NotificationImportant
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sigaf.app.presentation.components.SigafCard
import com.sigaf.app.presentation.components.SigafPrimaryButton
import com.sigaf.app.presentation.theme.CyanAccent
import com.sigaf.app.presentation.theme.TextPrimary
import com.sigaf.app.presentation.theme.TextSecondary

@Composable
fun HomeScreen(
    onNavigateToAnalyzer: () -> Unit = {},
    onNavigateToAlerts: () -> Unit = {},
    onNavigateToHistory: () -> Unit = {},
    onNavigateToProfile: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "MENÚ PRINCIPAL",
            style = MaterialTheme.typography.displayMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Bienvenido a SIGAF",
            style = MaterialTheme.typography.bodyLarge,
            color = CyanAccent,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        MenuCard(
            title = "Analizar",
            subtitle = "Detecta fraudes en mensajes, enlaces o remitentes",
            onClick = onNavigateToAnalyzer
        )

        Spacer(modifier = Modifier.height(14.dp))

        MenuCard(
            title = "Centro de Alertas",
            subtitle = "Revisa alertas y marcas como leídas",
            onClick = onNavigateToAlerts
        )

        Spacer(modifier = Modifier.height(14.dp))

        MenuCard(
            title = "Historial",
            subtitle = "Consulta tus análisis anteriores",
            onClick = onNavigateToHistory
        )

        Spacer(modifier = Modifier.height(14.dp))

        MenuCard(
            title = "Perfil",
            subtitle = "Configuración y datos de la cuenta",
            onClick = onNavigateToProfile
        )

        Spacer(modifier = Modifier.height(20.dp))

        SigafPrimaryButton(
            text = "ANALIZAR AHORA >",
            onClick = onNavigateToAnalyzer
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MenuCard(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    SigafCard(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp)
    ) {
        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }
    }
}
