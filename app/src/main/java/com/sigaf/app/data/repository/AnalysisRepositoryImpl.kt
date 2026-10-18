package com.sigaf.app.data.repository

import com.sigaf.app.domain.model.AnalysisInput
import com.sigaf.app.domain.model.AnalysisResult
import com.sigaf.app.domain.model.AnalysisTab
import com.sigaf.app.domain.model.RiskLevel
import com.sigaf.app.domain.repository.AnalysisRepository
import kotlinx.coroutines.delay

/**
 * Implementación fake para MVP (sin backend).
 */
class AnalysisRepositoryImpl : AnalysisRepository {

    override suspend fun analyze(input: AnalysisInput): AnalysisResult {
        delay(800)

        val text = input.text.lowercase()
        val baseScore = when (input.tab) {
            AnalysisTab.MESSAGE -> if (text.contains("transferir") || text.contains("clave") || text.contains("urgente")) 85 else 15
            AnalysisTab.URL -> if (text.contains("http") && (text.contains("gratis") || text.contains("premio") || text.count { it == '.' } >= 3)) 80 else 10
            AnalysisTab.SENDER -> if (text.contains("+") || text.length < 6 || text.contains("desconocido")) 70 else 20
        }

        val risk = when {
            baseScore >= 75 -> RiskLevel.CRITICAL
            baseScore >= 50 -> RiskLevel.HIGH
            baseScore >= 25 -> RiskLevel.MEDIUM
            else -> RiskLevel.LOW
        }

        val verdict = when (risk) {
            RiskLevel.CRITICAL -> "Alto riesgo de fraude"
            RiskLevel.HIGH -> "Sospechoso"
            RiskLevel.MEDIUM -> "Requiere precaución"
            RiskLevel.LOW -> "Seguridad aceptable"
            else -> "Sin datos suficientes"
        }

        val recommendations = when (risk) {
            RiskLevel.CRITICAL -> listOf("NO compartas información sensible", "Verifica por canal oficial", "Reporta el intento")
            RiskLevel.HIGH -> listOf("Verifica la fuente antes de actuar", "No hagas clic si hay presión", "Consulta con soporte oficial")
            RiskLevel.MEDIUM -> listOf("Revisa con cuidado los datos", "Confirma por otro canal", "Desconfía de urgencias")
            else -> listOf("Parece seguro", "Mantén buenas prácticas de seguridad")
        }

        return AnalysisResult(
            score = baseScore,
            riskLevel = risk,
            verdict = verdict,
            recommendations = recommendations
        )
    }
}
