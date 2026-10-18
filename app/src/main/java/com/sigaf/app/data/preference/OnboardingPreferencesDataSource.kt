package com.sigaf.app.data.preference

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.sigaf.app.domain.model.NotificationPermissionState
import com.sigaf.app.domain.model.OnboardingState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// DataStore singleton a nivel de proceso: el delegate exige UNA sola
// instancia por aplicación (si no, lanza IllegalStateException).
private val Context.sigafDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "sigaf_onboarding_preferences"
)

/**
 * Fuente de datos del onboarding (almacenamiento local seguro en preferences).
 *
 * DataStore reemplaza a SharedPreferences: es asíncrono (Flow + Coroutines),
 * no bloquea la UI y tolera errores de escritura sin corromper el archivo.
 * Aquí solo se persisten banderas de onboarding; los datos sensibles del
 * usuario se guardarán cifrados con Keystore (Bloque 2).
 */
class OnboardingPreferencesDataSource(private val context: Context) {

    /** Estado del onboarding observado en tiempo real. */
    val onboardingState: Flow<OnboardingState> = context.sigafDataStore.data.map { prefs ->
        OnboardingState(
            welcomeSeen = prefs[KEY_WELCOME_SEEN] ?: false,
            notificationPermission = prefs[KEY_NOTIFICATION_PERMISSION]
                ?.let { stored ->
                    // Defensivo: si el valor guardado no es un enum válido,
                    // se asume que aún no se preguntó.
                    runCatching { NotificationPermissionState.valueOf(stored) }
                        .getOrDefault(NotificationPermissionState.NOT_ASKED)
                }
                ?: NotificationPermissionState.NOT_ASKED
        )
    }

    /** Persiste que la pantalla de bienvenida ya fue vista. */
    suspend fun setWelcomeSeen(seen: Boolean) {
        context.sigafDataStore.edit { prefs ->
            prefs[KEY_WELCOME_SEEN] = seen
        }
    }

    /** Persiste la decisión del usuario sobre el permiso de notificaciones. */
    suspend fun setNotificationPermission(granted: Boolean) {
        context.sigafDataStore.edit { prefs ->
            prefs[KEY_NOTIFICATION_PERMISSION] =
                if (granted) NotificationPermissionState.GRANTED.name
                else NotificationPermissionState.DENIED.name
        }
    }

    private companion object {
        val KEY_WELCOME_SEEN = booleanPreferencesKey("welcome_seen")
        val KEY_NOTIFICATION_PERMISSION = stringPreferencesKey("notification_permission")
    }
}
