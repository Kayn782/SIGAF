package com.sigaf.app.presentation.screens.splash

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sigaf.app.R
import com.sigaf.app.presentation.components.SigafLogo
import com.sigaf.app.presentation.components.SigafPrimaryButton
import com.sigaf.app.presentation.theme.CyanAccent
import com.sigaf.app.presentation.theme.TextPrimary
import com.sigaf.app.presentation.theme.TextSecondary
import com.sigaf.app.presentation.theme.TextTertiary

/**
 * PANTALLA 1 — Inicio / Entrada (Splash).
 *
 * - Logo central del escudo con la "S" de SIGAF (dibujado en Compose, con
 *   halo cian que respira).
 * - Títulos institucionales (SIGAF + nombre completo + línea de misión).
 * - Botón principal "INGRESAR AL SISTEMA >" con acento cian brillante.
 *
 * El [SplashViewModel] decide hacia qué ruta continúa el ingreso según el
 * onboarding ya persistido en DataStore.
 */
@Composable
fun SplashScreen(
    onEnterSystem: (String) -> Unit
) {
    val viewModel: SplashViewModel = viewModel(factory = SplashViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Entrada suave: el bloque central aparece con fundido y desplazamiento
    var started by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { started = true }
    val contentAlpha by animateFloatAsState(
        targetValue = if (started) 1f else 0f,
        animationSpec = tween(durationMillis = 750),
        label = "splash_alpha"
    )
    val contentOffset by animateDpAsState(
        targetValue = if (started) 0.dp else 28.dp,
        animationSpec = tween(durationMillis = 750),
        label = "splash_offset"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // --- Bloque central (logo + títulos institucionales) ---
        Column(
            modifier = Modifier
                .weight(1f)
                .graphicsLayer {
                    alpha = contentAlpha
                    translationY = contentOffset.toPx()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))

            SigafLogo(size = 136.dp, pulseEnabled = true)

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge,
                color = TextPrimary,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = stringResource(R.string.app_full_name),
                style = MaterialTheme.typography.titleMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = stringResource(R.string.splash_tagline),
                style = MaterialTheme.typography.labelMedium,
                color = CyanAccent.copy(alpha = 0.9f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 12.dp)
            )

            Spacer(modifier = Modifier.weight(1f))
        }

        // --- Pie: nota legal + botón de ingreso ---
        Text(
            text = stringResource(R.string.splash_legal),
            style = MaterialTheme.typography.labelSmall,
            color = TextTertiary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(14.dp))

        SigafPrimaryButton(
            text = stringResource(R.string.splash_enter),
            enabled = !uiState.loading,
            onClick = { onEnterSystem(uiState.nextRoute) }
        )

        Spacer(modifier = Modifier.height(6.dp))
    }
}
