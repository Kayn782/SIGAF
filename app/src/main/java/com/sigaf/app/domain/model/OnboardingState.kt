package com.sigaf.app.domain.model

/**
 * Estado del flujo de bienvenida, persistido en DataStore.
 *
 * @param welcomeSeen true cuando el usuario ya pasó por la pantalla
 *                    "BIENVENIDO A SIGAF".
 * @param notificationPermission decisión del usuario sobre la lectura de
 *                    notificaciones (pantalla de permisos).
 */
data class OnboardingState(
    val welcomeSeen: Boolean = false,
    val notificationPermission: NotificationPermissionState =
        NotificationPermissionState.NOT_ASKED
) {
    /** El flujo de bienvenida quedó completo cuando ya se preguntó por notificaciones. */
    val isOnboardingCompleted: Boolean
        get() = notificationPermission != NotificationPermissionState.NOT_ASKED
}

/** Decisión del usuario sobre el permiso de lectura de notificaciones. */
enum class NotificationPermissionState {
    /** Todavía no se mostró la pantalla de permisos. */
    NOT_ASKED,

    /** El usuario otorgó el permiso. */
    GRANTED,

    /** El usuario denegó el permiso. */
    DENIED
}
