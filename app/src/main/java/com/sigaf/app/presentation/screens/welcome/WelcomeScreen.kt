package com.sigaf.app.presentation.screens.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Radar
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sigaf.app.R
import com.sigaf.app.presentation.components.FeatureCard
import com.sigaf.app.presentation.components.SigafPrimaryButton
import com.sigaf.app.presentation.components.SigafTextLink
import com.sigaf.app.presentation.theme.CyanAccent
import com.sigaf.app.presentation.theme.TextPrimary
import com.sigaf.app.presentation.theme.TextSecondary

/**
 * PANTALLA 2 — Onboarding / Bienvenida.
 *
 * - Título "BIENVENIDO A SIGAF" y subtítulo "Tu seguridad digital empieza aquí".
 * - Tres tarjetas horizontales con iconos interactivos de bordes
 *   redondeados: Detecta / Analiza / Previene (al tocarlas se activan
 *   con borde cian).
 * - Botón principal "COMENZAR >" y enlace inferior "YA TENGO UNA CUENTA".
 *
 * Al pulsar COMENZAR, [WelcomeViewModel] persiste la bienvenida en
 * DataStore y, vía SharedFlow, la pantalla navega a los permisos.
 */
@Composable
fun WelcomeScreen(
    onStart: () -> Unit,
    onLogin: () -> Unit
) {
    val viewModel: WelcomeViewModel = viewModel(factory = WelcomeViewModel.Factory)

    // Efectos de un solo disparo (navegación) → no se repiten al recomponer
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                WelcomeViewModel.Event.ProceedToPermissions -> onStart()
            }
        }
    }

    // Selección visual de la tarjeta tocada (-1 = ninguna)
    var selectedFeature by rememberSaveable { mutableIntStateOf(-1) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = stringResource(R.string.welcome_title),
            style = MaterialTheme.typography.displayMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = stringResource(R.string.welcome_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = CyanAccent,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(32.dp))

        // --- Tres tarjetas horizontales: Detecta / Analiza / Previene ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FeatureCard(
                icon = Icons.Filled.Radar,
                title = stringResource(R.string.feature_detect_title),
                description = stringResource(R.string.feature_detect_desc),
                selected = selectedFeature == 0,
                onClick = { selectedFeature = 0 },
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                icon = Icons.Filled.Timeline,
                title = stringResource(R.string.feature_analyze_title),
                description = stringResource(R.string.feature_analyze_desc),
                selected = selectedFeature == 1,
                onClick = { selectedFeature = 1 },
                modifier = Modifier.weight(1f)
            )
            FeatureCard(
                icon = Icons.Filled.VerifiedUser,
                title = stringResource(R.string.feature_prevent_title),
                description = stringResource(R.string.feature_prevent_desc),
                selected = selectedFeature == 2,
                onClick = { selectedFeature = 2 },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // --- Acciones ---
        SigafPrimaryButton(
            text = stringResource(R.string.welcome_start),
            onClick = viewModel::onStartClicked
        )

        Spacer(modifier = Modifier.height(6.dp))

        SigafTextLink(
            text = stringResource(R.string.welcome_login),
            onClick = onLogin
        )

        Spacer(modifier = Modifier.height(4.dp))
    }
}
