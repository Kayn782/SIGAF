package com.sigaf.app

import android.app.Application
import android.content.Context
import com.sigaf.app.di.AppContainer

/**
 * Application de SIGAF.
 * Aquí se crea el [AppContainer] (Inyección de Dependencias manual) que vive
 * durante todo el ciclo de vida de la aplicación.
 */
class SigafApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}

/** Extensión para acceder al contenedor desde cualquier Activity/Composable. */
val Context.sigafContainer: AppContainer
    get() = (applicationContext as SigafApplication).appContainer
