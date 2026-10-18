package com.sigaf.app

import android.app.Application
import com.sigaf.app.di.AppContainer
import com.sigaf.app.di.DefaultAppContainer

/**
 * Clase Application de SIGAF.
 *
 * Es el punto más temprano del proceso y por eso aquí vive el contenedor de
 * dependencias: cualquier ViewModel puede pedirlo con
 * `this[APPLICATION_KEY] as SigafApplication` dentro de su Factory.
 *
 * Se registra en el manifiesto con android:name=".SigafApplication".
 */
class SigafApplication : Application() {

    /** Contenedor de dependencias de toda la app (creación perezosa). */
    val container: AppContainer by lazy { DefaultAppContainer(this) }
}
