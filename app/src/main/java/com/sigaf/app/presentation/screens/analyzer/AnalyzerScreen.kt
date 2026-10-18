package com.sigaf.app.presentation.screens.analyzer

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sigaf.app.domain.model.AnalysisTab
import com.sigaf.app.presentation.components.SigafCard
import com.sigaf.app.presentation.components.SigafPrimaryButton
import com.sigaf.app.presentation.theme.CyanAccent
import com.sigaf.app.presentation.theme.NavyCardBorder
import com.sigaf.app.presentation.theme.TextPrimary
import com.sigaf.app.presentation.theme.TextSecondary
import com.sigaf.app.presentation.theme.TextTertiary

@Composable
fun AnalyzerScreen(
    onBack: (() -> Unit)? = null
) {
    val viewModel: AnalyzerViewModel = viewModel(factory = AnalyzerViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "ANALIZAR CONTENIDO",
            style = MaterialTheme.typography.displayMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Detecta fraudes en mensajes, enlaces o remitentes",
            style = MaterialTheme.typography.bodyLarge,
            color = CyanAccent,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Tabs superiores
        SigafCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 12.dp,
                vertical = 12.dp
            )
        ) {
            TabRow(
                selectedTabIndex = when (uiState.selectedTab) {
                    AnalysisTab.MESSAGE -> 0
                    AnalysisTab.URL -> 1
                    AnalysisTab.SENDER -> 2
                },
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                contentColor = CyanAccent,
                indicator = { tabPositions ->
                    val idx = when (uiState.selectedTab) {
                        AnalysisTab.MESSAGE -> 0
                        AnalysisTab.URL -> 1
                        AnalysisTab.SENDER -> 2
                    }
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = CyanAccent,
                        height = 2.dp
                    )
                }
            ) {
                Tab(
                    selected = uiState.selectedTab == AnalysisTab.MESSAGE,
                    onClick = { viewModel.onTabSelected(AnalysisTab.MESSAGE) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Message,
                                contentDescription = null,
                                tint = if (uiState.selectedTab == AnalysisTab.MESSAGE) CyanAccent else TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.padding(start = 6.dp))
                            Text("Mensaje", color = if (uiState.selectedTab == AnalysisTab.MESSAGE) TextPrimary else TextSecondary)
                        }
                    }
                )
                Tab(
                    selected = uiState.selectedTab == AnalysisTab.URL,
                    onClick = { viewModel.onTabSelected(AnalysisTab.URL) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Link,
                                contentDescription = null,
                                tint = if (uiState.selectedTab == AnalysisTab.URL) CyanAccent else TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.padding(start = 6.dp))
                            Text("Enlace", color = if (uiState.selectedTab == AnalysisTab.URL) TextPrimary else TextSecondary)
                        }
                    }
                )
                Tab(
                    selected = uiState.selectedTab == AnalysisTab.SENDER,
                    onClick = { viewModel.onTabSelected(AnalysisTab.SENDER) },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Filled.Person,
                                contentDescription = null,
                                tint = if (uiState.selectedTab == AnalysisTab.SENDER) CyanAccent else TextSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.padding(start = 6.dp))
                            Text("Remitente", color = if (uiState.selectedTab == AnalysisTab.SENDER) TextPrimary else TextSecondary)
                        }
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        SigafCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 20.dp,
                vertical = 22.dp
            )
        ) {
            Text(
                text = when (uiState.selectedTab) {
                    AnalysisTab.MESSAGE -> "Pega o escribe el mensaje sospechoso"
                    AnalysisTab.URL -> "Pega la URL sospechosa"
                    AnalysisTab.SENDER -> "Ingresa número o nombre del remitente"
                },
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = uiState.inputText,
                onValueChange = viewModel::onInputChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        when (uiState.selectedTab) {
                            AnalysisTab.MESSAGE -> "Ej.: 'Urgente, transferir dinero...'"
                            AnalysisTab.URL -> "Ej.: https://ejemplo-sospechoso.com"
                            AnalysisTab.SENDER -> "Ej.: +57 300 000 0000"
                        },
                        color = TextTertiary
                    )
                },
                minLines = 3,
                maxLines = 6,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = TextPrimary,
                    unfocusedTextColor = TextPrimary,
                    focusedBorderColor = CyanAccent,
                    unfocusedBorderColor = NavyCardBorder,
                    focusedLabelColor = CyanAccent,
                    unfocusedLabelColor = TextSecondary,
                    cursorColor = CyanAccent
                )
            )

            uiState.errorMessage?.let { msg ->
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = msg,
                    style = MaterialTheme.typography.bodySmall,
                    color = androidx.compose.ui.graphics.Color(0xFFFF6B8B)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SigafPrimaryButton(
                text = if (uiState.isAnalyzing) "ANALIZANDO..." else "ANALIZAR AHORA >",
                enabled = !uiState.isAnalyzing,
                onClick = viewModel::onAnalyzeClick
            )
        }

        uiState.result?.let { res ->
            Spacer(modifier = Modifier.height(20.dp))
            SigafCard(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(
                    horizontal = 20.dp,
                    vertical = 22.dp
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "RESULTADO DEL ANÁLISIS",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "${res.score}%",
                        style = MaterialTheme.typography.titleLarge,
                        color = res.riskLevel.color()
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = res.verdict,
                    style = MaterialTheme.typography.bodyLarge,
                    color = res.riskLevel.color()
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Recomendaciones:",
                    style = MaterialTheme.typography.titleSmall,
                    color = TextPrimary
                )

                res.recommendations.forEach { rec ->
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "• $rec",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextSecondary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
