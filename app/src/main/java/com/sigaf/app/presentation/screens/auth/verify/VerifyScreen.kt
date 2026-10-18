package com.sigaf.app.presentation.screens.auth.verify

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MarkEmailRead
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sigaf.app.presentation.components.SigafCard
import com.sigaf.app.presentation.components.SigafPrimaryButton
import com.sigaf.app.presentation.components.SigafTextLink
import com.sigaf.app.presentation.theme.CyanAccent
import com.sigaf.app.presentation.theme.NavyCardBorder
import com.sigaf.app.presentation.theme.TextPrimary
import com.sigaf.app.presentation.theme.TextSecondary
import com.sigaf.app.presentation.theme.TextTertiary

@Composable
fun VerifyScreen(
    onVerified: () -> Unit,
    onResend: () -> Unit,
    onBack: () -> Unit
) {
    val viewModel: VerifyViewModel = viewModel(factory = VerifyViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val focusManager = LocalFocusManager.current

    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                VerifyViewModel.Event.VerifySuccess -> onVerified()
                VerifyViewModel.Event.CodeResent -> Unit
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(RoundedCornerShape(18.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.MarkEmailRead,
                contentDescription = null,
                tint = CyanAccent,
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "VERIFICACIÓN DE CORREO",
            style = MaterialTheme.typography.displayMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Ingresa el código de 4 dígitos que te enviamos",
            style = MaterialTheme.typography.bodyLarge,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(26.dp))

        SigafCard(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                horizontal = 20.dp,
                vertical = 22.dp
            )
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                repeat(4) { index ->
                    val digit = uiState.codeDigits[index]
                    val focusRequester = androidx.compose.runtime.remember { FocusRequester() }

                    OutlinedTextField(
                        value = digit,
                        onValueChange = { input: String ->
                            val filtered = input.filter { it.isDigit() }.take(1)
                            viewModel.onDigitChange(index, filtered)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 6.dp)
                            .focusRequester(focusRequester),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        textStyle = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary,
                            focusedBorderColor = CyanAccent,
                            unfocusedBorderColor = NavyCardBorder,
                            cursorColor = CyanAccent
                        )
                    )
                }
            }

            uiState.errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodySmall,
                    color = androidx.compose.ui.graphics.Color(0xFFFF6B8B),
                    textAlign = TextAlign.Start
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            SigafPrimaryButton(
                text = if (uiState.isLoading) "VERIFICANDO..." else "VERIFICAR CÓDIGO",
                enabled = !uiState.isLoading && uiState.codeDigits.all { it.isNotBlank() },
                onClick = viewModel::onVerifyClick
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (uiState.canResend) {
            Text(
                text = "¿No recibiste el código?",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
            SigafTextLink(
                text = "REENVIAR CÓDIGO",
                onClick = {
                    viewModel.onResendClick()
                    onResend()
                }
            )
        } else {
            Text(
                text = "Reenviar código en 00:${uiState.timeLeftSeconds.toString().padStart(2, '0')}",
                style = MaterialTheme.typography.bodyMedium,
                color = TextTertiary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        SigafTextLink(
            text = "VOLVER",
            onClick = onBack
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}
