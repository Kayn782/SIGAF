# SIGAF — Sistema Inteligente de Gestión y Análisis de Fraudes

Aplicación móvil Android nativa (Kotlin + Jetpack Compose) para la detección y gestión
de fraudes, con arquitectura limpia MVVM y backend FastAPI/Python + servicios de IA.

## Estado actual: Bloque 1 completado ✅

- Estructura de paquetes con arquitectura limpia (`data`, `domain`, `ui`, `di`).
- Tema oscuro nativo por defecto + paleta de riesgos (Bajo/Medio/Alto/Crítico).
- Pantalla **Inicio** completa: anillo de Nivel de Protección (87%), contadores,
  gráfica semanal, accesos rápidos, consejo del día y tarjeta Academia SIGAF.
- **Compilado, instalado y verificado** en el emulador (AGP 9.4.1 / Gradle 9.6 / Kotlin 2.2.10 / JDK 25).
  Captura real: `mockups/captura-inicio.png`.
- Barra de navegación inferior fija con 5 pestañas: Inicio, Analizar, Alertas,
  Historial y Perfil.
- Navegación funcional a todos los destinos (los 4 restantes son provisionales).

## Documentación para estudio

📄 **[`docs/DOCUMENTACION_SIGAF_BLOQUE_1.md`](docs/DOCUMENTACION_SIGAF_BLOQUE_1.md)**
Explica la arquitectura, la estructura de paquetes, cada archivo, las reglas de
negocio y trae una guía de preguntas para la exposición.

## Cómo ejecutar

**Opción A — Android Studio (recomendada)**

1. `File > Open…` y elegir la carpeta raíz del proyecto (donde está `settings.gradle.kts`).
2. Esperar el **Gradle Sync** (AGP 9.4.1 / Gradle 9.6 / Kotlin 2.2.10 / JDK 25, el que trae Android Studio).
3. Pulsar **Run ▶** en el emulador **Medium_Phone** (API 37, minSdk 26).

**Opción B — Consola**

```powershell
$env:JAVA_HOME = "E:\Aplicaciones Desarrollo\ANDROID\jbr"
.\gradlew.bat assembleDebug     # APK en app\build\outputs\apk\debug
.\gradlew.bat installDebug      # instala en el emulador encendido
```

**Ver el diseño sin compilar:** abre `mockups/preview-inicio.html` en el navegador.

## Estructura de capas

```
ui (Compose) ──► domain (reglas de negocio) ◄── data (red, BD, DTOs)
      ▲                                              ▲
      └────────── di (inyección manual) ─────────────┘
```

La inyección de dependencias es **manual** (`di/AppContainer.kt`): Hilt no estaba en la
lista de tecnologías obligatorias y así se evitan procesadores de anotaciones (KSP).
La UI depende de interfaces del dominio, no de implementaciones.

## Hoja de ruta

| Bloque | Contenido |
|-------|-----------|
| 2 | Red real: Retrofit, JWT, login/registro, consentimiento Ley 1581 |
| 3 | Pantalla Analizar (Mensaje / Enlace / Remitente) |
| 4 | Historial con Room y centro de Alertas |
| 5 | Segundo plano: NotificationListenerService, WorkManager, FCM |
| 6 | Perfil: DataStore, privacidad, cierre de sesión |
