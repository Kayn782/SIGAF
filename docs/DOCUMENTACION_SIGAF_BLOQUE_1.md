# SIGAF — Documentación del Bloque 1

**Proyecto:** SIGAF — Sistema Inteligente de Gestión y Análisis de Fraudes
**Bloque:** 1 — Estructura del proyecto Android (arquitectura limpia + MVVM) y pantalla de **Inicio**
**Fecha:** 17 de octubre de 2026
**Capa:** Cliente Android (Kotlin 2.2.10 + Jetpack Compose, AGP 9.4.1 / Gradle 9.6 / JDK 25)

---

## 1. Resumen: ¿qué se construyó en este bloque?

En este primer bloque se dejó **listo el esqueleto profesional del proyecto Android** y se implementó **completa la pantalla "Menú Principal (Inicio)"** con datos simulados, para que puedas verla funcionando en el emulador *antes* de que exista el backend.

| # | Entregable | Dónde vive |
|---|-----------|-----------|
| 1 | Estructura de paquetes con arquitectura limpia MVVM (`data`, `domain`, `ui`, `di`) | `app/src/main/java/com/sigaf/app/` |
| 2 | Configuración Gradle completa (Version Catalog) con TODO el stack obligatorio | `gradle/libs.versions.toml`, `app/build.gradle.kts` |
| 3 | Tema visual oscuro nativo + paleta de riesgos | `ui/theme/` |
| 4 | Pantalla de Inicio: anillo de Nivel de Protección, contadores, gráfica semanal, accesos rápidos, consejos y Academia SIGAF | `ui/screens/home/HomeScreen.kt` |
| 5 | Barra de navegación inferior con las 5 pestañas | `ui/components/SigafBottomBar.kt` + `ui/navigation/` |
| 6 | Navegación completa (NavHost) con destinos provisionales | `ui/SigafApp.kt`, `ui/navigation/SigafNavHost.kt` |
| 7 | Flujo MVVM real: `HomeViewModel` + `StateFlow` + repositorio con datos de ejemplo | `ui/screens/home/`, `data/repository/` |
| 8 | Inyección de dependencias manual (contenedor propio) | `di/AppContainer.kt` |
| 9 | Manifiesto con Principio de Mínimo Privilegio documentado | `app/src/main/AndroidManifest.xml` |
| 10 | Vista previa HTML del diseño (para verlo sin compilar) | `mockups/preview-inicio.html` |

> **Estado actual:** el Inicio está completo y funcional con datos de ejemplo. Las otras 4 pestañas muestran una pantalla provisional con el nombre del bloque en que se construirán.

---

## 2. Cómo abrir y ejecutar el proyecto

### Opción A — Android Studio (recomendada)

1. Abre **Android Studio** (en este equipo está en `E:\Aplicaciones Desarrollo\ANDROID`).
2. `File > Open…` → carpeta raíz del proyecto (donde está `settings.gradle.kts`).
3. Espera el **Gradle Sync**. La primera vez descarga dependencias (unos minutos).
4. Arranca el emulador que ya tienes creado (**`Medium_Phone`**, API 37) y pulsa **Run ▶**.

### Opción B — Línea de comandos

```powershell
$env:JAVA_HOME = "E:\Aplicaciones Desarrollo\ANDROID\jbr"   # JDK 25 que incluye Android Studio
.\gradlew.bat assembleDebug                                  # APK en app\build\outputs\apk\debug
```

Para instalarlo en el emulador encendido:

```powershell
$env:JAVA_HOME = "E:\Aplicaciones Desarrollo\ANDROID\jbr"
.\gradlew.bat installDebug
```

### Ver el diseño sin compilar nada

Abre `mockups/preview-inicio.html` en el navegador: es una réplica exacta (HTML/CSS) de la pantalla de Inicio, útil para revisar el diseño o mostrarlo en la exposición sin depender del emulador.

**Resultado esperado:** pantalla negra con "Hola, Andrés 👋", el anillo verde al **87%**, los tres contadores, la gráfica de barras, los accesos rápidos, el consejo, la tarjeta Academia SIGAF y la barra inferior de 5 pestañas.

### Compilación verificada ✅

El proyecto **se compiló y se instaló realmente** en el emulador `Medium_Phone_API_37.0` de este equipo:

```
> Task :app:assembleDebug
BUILD SUCCESSFUL in 34s
```

- APK generado: `app\build\outputs\apk\debug\app-debug.apk` (≈20,5 MB)
- Captura de la app ejecutándose: `mockups\captura-inicio.png`
- Comando usado: `$env:JAVA_HOME = "E:\Aplicaciones Desarrollo\ANDROID\jbr"; .\gradlew.bat installDebug`

---

## 3. Arquitectura general del sistema (3 capas)

SIGAF es un sistema de **tres capas**; este documento cubre la primera.

```
┌─────────────────────────────────────────────────────────────────────┐
│  1. CLIENTE ANDROID (este bloque)                                   │
│     Kotlin · Jetpack Compose · MVVM · Clean Architecture            │
│     Room (historial) · DataStore (prefs) · Keystore (secretos)      │
│     WorkManager · NotificationListenerService (escucha pasiva)      │
└───────────────────────────────┬─────────────────────────────────────┘
                                │ HTTPS + JSON (+ JWT)
┌───────────────────────────────▼─────────────────────────────────────┐
│  2. BACKEND FastAPI (Python) — Bloque 2                             │
│     Autenticación, orquestación de análisis, persistencia           │
└───────────────────────────────┬─────────────────────────────────────┘
                                │ API interna
┌───────────────────────────────▼─────────────────────────────────────┐
│  3. SERVICIOS DE IA + FCM (externos) — Bloques posteriores          │
│     Modelos de clasificación de fraude · notificaciones push        │
└─────────────────────────────────────────────────────────────────────┘
```

### 3.1. Arquitectura interna del cliente Android (Clean Architecture + MVVM)

```
        ┌──────────────────────────── ui (Compose) ───────────────────────────┐
        │  HomeScreen ─── observa ──► HomeUiState ───► pinta píxeles          │
        │      │  eventos (onClick) hacia abajo                                │
        └──────┼──────────────────────────────────────────────────────────────┘
               ▼
        ┌───────────────────── ui (ViewModel) ────────────────────────────────┐
        │  HomeViewModel  ·  StateFlow<HomeUiState>  ·  viewModelScope         │
        │  mapea dominio → estado de UI, captura errores, cancela coroutines   │
        └──────┬──────────────────────────────────────────────────────────────┘
               ▼ depende SOLO de la interfaz (inversión de dependencias)
        ┌──────────────────────── domain (puro Kotlin) ───────────────────────┐
        │  DashboardRepository (interfaz)  ·  DashboardSummary / RiskLevel     │
        │  NO conoce Compose, Retrofit, Room ni JSON                           │
        └──────▲──────────────────────────────────────────────────────────────┘
               │ implementa
        ┌──────┴──────────────── data ────────────────────────────────────────┐
        │  FakeDashboardRepository (hoy)  ·  Retrofit/OkHttp (Bloque 2)       │
        │  DTOs + Mappers  ·  Room (Bloque 4)  ·  DataStore (Bloque 6)        │
        └─────────────────────────────────────────────────────────────────────┘
               ▲ construye y entrega
        ┌──────┴──────────────── di (inyección manual) ───────────────────────┐
        │  AppContainer  →  dice quién implementa DashboardRepository          │
        │  HomeViewModel.Factory  →  conecta el contenedor con el ViewModel    │
        └─────────────────────────────────────────────────────────────────────┘
```

**La regla de oro de la arquitectura limpia:** las flechas de dependencia **siempre apuntan hacia dentro**. `ui` conoce `domain`, `data` conoce `domain`, pero `domain` no conoce a nadie. Por eso se puede cambiar el backend o la base de datos sin reescribir la pantalla.

---

## 4. Estructura de paquetes

### 4.1. Árbol real creado en el Bloque 1

```
Proyecto predeterminado/
├─ settings.gradle.kts                   ← módulos + repositorios + resolución de JDK
├─ build.gradle.kts                      ← plugins declarados (sin aplicar)
├─ gradle.properties                     ← memoria, caché de configuración, estilo Kotlin
├─ local.properties                       ← ruta del SDK (no se sube a Git)
├─ gradlew / gradlew.bat / wrapper       ← compilar desde consola
├─ gradle/libs.versions.toml             ← VERSION CATALOG (todas las versiones en un sitio)
├─ mockups/preview-inicio.html           ← vista previa del diseño en el navegador
├─ docs/DOCUMENTACION_SIGAF_BLOQUE_1.md  ← este documento
└─ app/
   ├─ build.gradle.kts                   ← dependencias y configuración del módulo
   └─ src/main/
      ├─ AndroidManifest.xml             ← permisos + Activity (Mínimo Privilegio documentado)
      ├─ keepRules/rules.keep            ← reglas R8 (AGP 9; antes era proguard-rules.pro)
      ├─ res/values/strings.xml          ← TODOS los textos en español (localización)
      ├─ res/values/themes.xml           ← tema XML mínimo (fondo negro, barras transparentes)
      ├─ res/drawable/ic_sigaf_logo.xml  ← logo vectorial (escudo verde)
      └─ java/com/sigaf/app/
         ├─ SigafApplication.kt          ← raíz: crea el contenedor de dependencias
         ├─ MainActivity.kt              ← Compose + tema oscuro (sin XML de layout)
         │
         ├─ di/                          ← INYECCIÓN DE DEPENDENCIAS (manual)
         │  └─ AppContainer.kt           ← interfaz + implementación que enlaza domain ↔ data
         │
         ├─ domain/                      ← REGLAS DE NEGOCIO (Kotlin puro, sin Android)
         │  ├─ model/
         │  │  ├─ DashboardSummary.kt    ← modelo del panel: nivel, contadores, actividad, tip
         │  │  └─ RiskLevel.kt           ← BAJO · MEDIO · ALTO · CRITICO · INSUFICIENTE
         │  └─ repository/
         │     └─ DashboardRepository.kt ← contrato (puerto) que la UI consume
         │
         ├─ data/                        ← IMPLEMENTACIONES (red, BD, caché)
         │  ├─ repository/
         │  │  └─ FakeDashboardRepository.kt ← datos de ejemplo (se reemplaza en Bloque 2)
         │  ├─ mapper/
         │  │  └─ DashboardMapper.kt     ← DTO ⇄ dominio (blindaje ante cambios del backend)
         │  └─ remote/
         │     ├─ NetworkModule.kt       ← OkHttp + Retrofit + kotlinx.serialization
         │     ├─ api/DashboardApi.kt    ← endpoints Retrofit (Bloque 2)
         │     ├─ dto/DashboardDto.kt    ← espejo del JSON del backend
         │     └─ interceptor/AuthTokenInterceptor.kt ← adjunta el JWT (Bloque 2)
         │
         └─ ui/                          ← PRESENTACIÓN (Jetpack Compose)
            ├─ SigafApp.kt               ← Scaffold + barra inferior + NavHost
            ├─ theme/
            │  ├─ Color.kt               ← paleta completa (fondos, textos, RIESGOS)
            │  ├─ Type.kt                ← tipografía + formas redondeadas
            │  ├─ Theme.kt               ← colorScheme oscuro + SigafTheme()
            │  └─ RiskColor.kt           ← RiskLevel → Color
            ├─ navigation/
            │  ├─ Routes.kt              ← rutas centralizadas ("inicio", "analizar?tab={tab}"…)
            │  ├─ BottomNavItem.kt       ← las 5 pestañas (ruta + icono + etiqueta)
            │  └─ SigafNavHost.kt        ← grafo de navegación completo
            ├─ components/               ← REUTILIZABLES (sin lógica de negocio)
            │  ├─ ProtectionRing.kt      ← anillo circular animado (Canvas)  ← componente estrella
            │  ├─ StatCard.kt            ← tarjeta de contador
            │  ├─ WeeklyActivityChart.kt ← gráfica de barras de 7 días
            │  ├─ SectionHeader.kt       ← título de sección + acción
            │  └─ SigafBottomBar.kt      ← NavigationBar de Material 3
            ├─ screens/
            │  ├─ home/                  ← PANTALLA INICIO (bloque actual)
            │  │  ├─ HomeScreen.kt       ← composables de la pantalla
            │  │  ├─ HomeViewModel.kt    ← estado + llamada al repositorio + Factory
            │  │  └─ HomeUiState.kt      ← estado inmutable de la pantalla
            │  ├─ analyzer/AnalysisTab.kt ← pestañas Mensaje/Enlace/Remitente
            │  └─ common/PlaceholderScreen.kt ← destinos provisionales
            └─ util/Format.kt            ← formato de cifras en locale Colombia
```

### 4.2. Paquetes previstos para bloques siguientes

| Paquete | Se agrega en | Qué contendrá |
|---------|-------------|----------------|
| `data/local/room` | Bloque 4 | Entidades, DAOs y base de datos del historial |
| `data/local/datastore` | Bloque 6 | Preferencias (modo oscuro, umbral, análisis automático) |
| `security/` | Bloque 2 y 6 | `TokenManager`, `EncryptedSharedPreferences`, Keystore |
| `background/` | Bloque 5 | `SigafNotificationListener`, `AnalysisWorker`, `FcmService` |
| `ui/screens/alerts` \| `history` \| `profile` | Bloques 4 y 6 | Las demás pantallas |
| `util/` | Continuo | Result wrappers, validadores, extensiones |

---

## 5. Stack tecnológico: qué se usó y por qué

### 5.1. Versiones reales del proyecto

| Tecnología | Versión | Rol en SIGAF |
|-----------|---------|--------------|
| **Kotlin** | 2.2.10 | Lenguaje único del cliente |
| **Android Gradle Plugin (AGP)** | 9.4.1 | Compila el módulo Android |
| **Gradle** | 9.6 | Motor de construcción |
| **JDK** | 25 (el que trae Android Studio) | Ejecuta Gradle |
| **compileSdk / targetSdk** | 37 | Plataforma instalada en el equipo |
| **minSdk** | 26 (Android 8.0) | Keystore moderno, canales de notificación, WorkManager |
| **Jetpack Compose (BOM)** | 2026.02.01 | UI declarativa |
| **Material 3** | 1.4.0 (vía BOM) | Componentes visuales |
| **Navigation Compose** | 2.10.2 | Navegación entre las 5 pestañas |
| **Lifecycle (ViewModel/Compose)** | 2.11.0 | `ViewModel`, `StateFlow`, `collectAsStateWithLifecycle` |
| **Coroutines** | 1.10.2 | Concurrencia (`suspend`, `viewModelScope`) |
| **Room** | 2.8.5 | Historial y caché offline (Bloque 4) |
| **DataStore** | 1.2.1 | Preferencias del usuario (Bloque 6) |
| **Retrofit / OkHttp** | 2.12.0 / 4.12.0 | HTTP con interceptores (Bloque 2) |
| **kotlinx.serialization** | 1.11.0 | JSON ↔ objetos |
| **androidx.security.crypto** | 1.1.0 | Keystore / EncryptedSharedPreferences |
| **WorkManager** | 2.12.0 | Trabajo en segundo plano (Bloque 5) |

> Las dependencias que aún no tienen código (Room, DataStore, Retrofit, security-crypto, WorkManager) están **declaradas desde ya** en `libs.versions.toml`, para que los siguientes bloques no tengan que tocar la configuración de Gradle.

### 5.2. Nota sobre los iconos

`material-icons-extended` **dejó de publicarse** (su última versión es la 1.7.8), por eso se fija la versión a mano en el catálogo en lugar de dejarla a la BOM. Es la única dependencia que no sigue al BOM de Compose.

---

## 6. Toolchain 2026: qué cambió con AGP 9 (importante para tu exposición)

Tu Android Studio es de 2026 y trae **AGP 9**, que cambió varias cosas respecto a los tutoriales antiguos. El proyecto se ajustó a la manera nueva:

| Antes (AGP 8 y anteriores) | Ahora (AGP 9, como en este proyecto) |
|---------------------------|--------------------------------------|
| Se aplicaba el plugin `org.jetbrains.kotlin.android` | **Ya no se aplica**: AGP 9 trae el soporte de Kotlin **integrado**; solo se añade `kotlin.plugin.compose` y `kotlin.plugin.serialization` |
| `compileSdk = 34` | `compileSdk { version = release(37) }` (DSL nuevo) |
| `isMinifyEnabled = true`, `isShrinkResources = true` | `buildTypes { release { optimization { enable = true; packageScope = … } } }` |
| `proguard-rules.pro` en la raíz del módulo | carpeta `app/src/main/keepRules/*.keep` |
| `kotlinOptions { jvmTarget = "17" }` | `compileOptions { source/targetCompatibility = VERSION_11 }` (AGP alinea Kotlin solo) |
| Gradle 8.9 y JDK 17/21 | **Gradle 9.6 + JDK 25** |

Otros ajustes que hacen que el proyecto funcione en este equipo:

- `settings.gradle.kts` incluye el plugin `foojay-resolver-convention` (resuelve JDK automáticamente).
- `gradle.properties` activa la **caché de configuración** (`org.gradle.configuration-cache=true`).
- `local.properties` apunta al SDK instalado (`C:\Users\sierr\AppData\Local\Android\Sdk`).
- Se copiaron `gradlew`, `gradlew.bat` y `gradle-wrapper.jar` desde tu proyecto `sigaf4` para poder compilar también desde consola.

---

## 7. Reglas de negocio del proyecto: cómo se reflejan en el código

### 7.1. Principio de Mínimo Privilegio (permisos)
- El manifiesto solicita **solo** `INTERNET` y `POST_NOTIFICATIONS`.
- **No** se declara `READ_SMS` ni `RECEIVE_SMS`, y **no** se pide ser app de SMS predeterminada.
- El bloque del futuro `NotificationListenerService` está **comentado y documentado** en el manifiesto, marcado como *Bloque 5*, indicando que exige autorización explícita del usuario en *Ajustes > Acceso a notificaciones*.

### 7.2. Evaluación probabilística (nunca "garantía absoluta")
- El dominio modela **niveles de riesgo estimado**, no booleanos "fraude/no fraude" (`RiskLevel`).
- La UI dice literalmente *"Riesgo estimado: Bajo"* (`R.string.home_risk_estimated`), jamás "es seguro".
- Debajo del panel aparece el aviso permanente: *"Evaluación probabilística… No es una garantía absoluta."*

### 7.3. Contenido insuficiente → nunca falso positivo
- Existe el nivel **`RiskLevel.INSUFICIENTE` ("Información insuficiente")** con color gris neutro (no rojo), precisamente para notificaciones truncadas o agrupadas ("4 mensajes nuevos").
- El mapper `DashboardMapper` degrada cualquier valor de riesgo desconocido a `INSUFICIENTE`, nunca a `BAJO` (evita dar falsa tranquilidad).

### 7.4. Privacidad — Ley 1581 de 2012 (Habeas Data, Colombia)
- `android:allowBackup="false"` en el manifiesto: los datos del usuario no salen del dispositivo por copias de seguridad.
- El `HttpLoggingInterceptor` está en nivel **BÁSICO**: registra método y URL, **nunca** el cuerpo, que contiene datos personales. En el Bloque 2 pasará a la variante `debug` (`debugImplementation`) para no registrar nada en producción.
- Los textos de consentimiento informado, la eliminación definitiva del historial y la política de tratamiento de datos quedan asignados al **Bloque 6 (Perfil)** y al registro (Bloque 2).

---

## 8. Explicación archivo por archivo (conceptos clave para exponer)

### 8.1. `SigafApplication.kt`
```kotlin
class SigafApplication : Application() {
    val container: AppContainer by lazy { DefaultAppContainer() }
}
```
Es el punto más temprano del proceso, por eso aquí vive el **contenedor de dependencias**. `by lazy` significa que no se construye hasta que alguien lo pide. Se registra en el manifiesto con `android:name=".SigafApplication"`.

### 8.2. `MainActivity.kt`
- `enableEdgeToEdge()`: dibuja detrás de las barras del sistema (aspecto inmersivo moderno).
- `setContent { SigafTheme(forceDarkTheme = true) { SigafApp() } }`: **no hay XML de layout**. Compose es el motor de UI.
- La Activity no inyecta nada: el ViewModel obtiene sus dependencias del contenedor mediante su `Factory`.

### 8.3. `di/AppContainer.kt` (inyección manual)
```kotlin
interface AppContainer { val dashboardRepository: DashboardRepository }

class DefaultAppContainer : AppContainer {
    override val dashboardRepository: DashboardRepository by lazy { FakeDashboardRepository() }
}
```
**¿Por qué inyección manual y no Hilt?**
1. Hilt **no está en la lista de tecnologías obligatorias** del proyecto.
2. No exige KSP ni procesadores de anotaciones: menos piezas móviles y compilaciones más rápidas.
3. Es transparente para estudiar: se ve exactamente de dónde sale cada objeto.
4. La arquitectura no cambia: la UI sigue dependiendo de la **interfaz** `DashboardRepository`; este contenedor decide qué implementación se entrega. Si más adelante se adopta Hilt, solo se migran estas líneas.

**Ventaja clave:** para conectar el backend real (Bloque 2) se cambia **una sola línea** aquí y la pantalla de Inicio sigue igual.

### 8.4. `ui/theme/Color.kt`
Define la paleta exigida:
- Fondos: `#000000` (pantalla), `#0B0C0F` (superficies), `#15171C` (tarjetas), `#24272F` (bordes).
- Primario: verde corporativo `#22C55E`.
- **Riesgos:** `BAJO #22C55E` · `MEDIO #FACC15` · `ALTO #F97316` · `CRITICO #EF4444` · `INSUFICIENTE #94A3B8`.

### 8.5. `ui/theme/Theme.kt` y `RiskColor.kt`
- `SigafTheme()` arma el `MaterialTheme` con el `darkColorScheme`, la tipografía y las formas redondeadas.
- `RiskColor.kt` convierte dominio → color con `val RiskLevel.color: Color`. **Traduce sin acoplar**: el dominio no sabe de colores y el color no sabe de negocio.

### 8.6. `domain/model/RiskLevel.kt` y `DashboardSummary.kt`
- Kotlin puro. Sin `import androidx.*`. Eso es lo que hace que la capa sea *limpia* y **testeable con JUnit sin emulador**.
- `DashboardSummary` es lo que el panel necesita: nivel 0–100, contadores, actividad semanal y un consejo.

### 8.7. `domain/repository/DashboardRepository.kt`
Interfaz de una sola función `suspend fun getDashboardSummary(): DashboardSummary`. Es el **puerto**; las implementaciones son los *adaptadores* (patrón *Ports & Adapters / Hexagonal*).

### 8.8. `data/repository/FakeDashboardRepository.kt`
Devuelve los datos de ejemplo (87%, 247 análisis, 12 amenazas, 86 automáticos, barras L–D) tras `delay(600)` para que se aprecie el estado de carga. Se reemplaza en el Bloque 2 cambiando **una sola línea** de `AppContainer`.

### 8.9. `data/remote/*`
- `NetworkModule`: clase normal (sin anotaciones) que construye OkHttp (timeouts, interceptor de token, logging) + Retrofit + `asConverterFactory` de kotlinx.serialization.
- `AuthTokenInterceptor`: punto único donde se añadirá el JWT.
- `dto/DashboardDto.kt` + `mapper/DashboardMapper.kt`: demuestran por qué separamos DTO de dominio.

### 8.10. `ui/screens/home/HomeUiState.kt`
Un **único** `data class` inmutable con todo el estado de la pantalla (`isLoading`, `errorMessage`, datos…). Beneficio: es imposible representar estados imposibles y la UI se vuelve predecible. `protectionFraction` calcula `87 → 0.87f` para el anillo.

### 8.11. `ui/screens/home/HomeViewModel.kt`
- El repositorio llega **por constructor**: `class HomeViewModel(private val dashboardRepository: DashboardRepository)`.
- `Factory` (companion): `viewModelFactory { initializer { … } }` toma la `Application`, lee su contenedor y construye el ViewModel. Compose lo usa así: `viewModel(factory = HomeViewModel.Factory)`. Así el ViewModel **nunca** crea sus propias dependencias.
- `MutableStateFlow` privado + `StateFlow` público de **solo lectura** (encapsulamiento).
- `loadDashboard()` vive en `viewModelScope`: la corrutina muere con el ViewModel. Los errores (`IOException` de red vs. genérico) se convierten en claves de texto (`"network"`, `"generic"`) que la UI traduce con `stringResource` → localización correcta y errores tipados.
- `DashboardSummary.toUiState()` (función privada del archivo): **capa anticorrupción** entre dominio y UI.

### 8.12. `ui/components/ProtectionRing.kt` ⭐ (el más interesante de explicar)
1. `animateFloatAsState(...)`: el progreso se anima de 0 → 0.87 en 1,2 s con `FastOutSlowInEasing`.
2. `Canvas` + `drawArc`: pista gris completa y arco de progreso con `Brush.sweepGradient` (degradado circular).
3. `rotate(-90f)`: hace que el arco arranque a las 12 en punto (el degradado por defecto empieza a las 3).
4. `StrokeCap.Round`: puntas redondeadas.
5. El texto central muestra `(progreso * 100).roundToInt()`.
6. **Detalle técnico clave:** `MaterialTheme.colorScheme` se leyó **fuera** del bloque de dibujo, porque el `DrawScope` no es contexto `@Composable` (error clásico de Compose).
7. `semantics { contentDescription = … }`: accesibilidad para lectores de pantalla.

### 8.13. `ui/components/WeeklyActivityChart.kt`
- Barras con `fillMaxHeight(fracción)` dentro de un contenedor de altura fija (`132.dp`); la fracción es `total del día / máximo de la semana`.
- `Brush.verticalGradient` da profundidad a la barra.
- Cada día con `threats > 0` recibe un **segmento rojo** en la cima.

### 8.14. `ui/components/SigafBottomBar.kt`
- `NavigationBar` de Material 3 con `BottomNavItem.items` (las 5 pestañas en orden).
- `NavigationBarItem.selected = currentRoute == item.route` pinta la activa en verde corporativo.
- `BadgedBox`/`Badge` deja lista la insignia de alertas sin leer (`unreadAlerts`, hoy en 0).

### 8.15. `ui/navigation/*`
- `Routes.kt`: las rutas como constantes (evita errores de tipeo). `"analizar?tab={tab}"` es una ruta **con argumento**.
- `BottomNavItem.kt`: `sealed class` con `data object` por pestaña (ruta + icono + etiqueta + descripción accesible).
- `SigafNavHost.kt`: `NavHost` con `startDestination = Routes.HOME` y 6 destinos (5 pestañas + Academia).

### 8.16. `ui/SigafApp.kt`
- Un solo `Scaffold` con `bottomBar` a nivel de app → **la barra se mantiene fija en todas las pantallas** (requisito de diseño).
- Patrón de navegación inferior recomendado por Google:
  `popUpTo(findStartDestination().id) { saveState = true }` + `launchSingleTop = true` + `restoreState = true`
  → no se apilan pantallas, no se duplican destinos y cada pestaña recuerda su estado (scroll incluido).
- `currentDestination?.hierarchy?.any { it.route == item.route }` decide si se muestra la barra.

### 8.17. `ui/screens/home/HomeScreen.kt`
Estructura de la pantalla (una `LazyColumn` con 8 ítems para no renderizar lo invisible):
1. **GreetingHeader** — saludo personalizado + avatar con la inicial.
2. **ProtectionCard** — `ProtectionRing` + chip de riesgo coloreado + descripción.
3. **Contadores** — tres `StatCard`: análisis (verde), amenazas (rojo), automáticos (azul).
4. **Gráfica semanal** — en tarjeta con `SectionHeader`.
5. **Accesos rápidos** — Mensaje / Enlace / Remitente → navegan a `Analizar` con la pestaña preseleccionada.
6. **Consejo del día** — tarjeta con icono de bombilla.
7. **Academia SIGAF** — tarjeta con degradado violeta y botón.
8. **Aviso probabilístico** — texto legal pequeño y centrado.

La pantalla es **"tonta"**: solo dibuja `HomeUiState`. Toda decisión está en el ViewModel o en el dominio. Maneja tres estados: **cargando**, **error con reintento** y **éxito**.

---

## 9. El flujo de datos completo (resumen para exponer)

```
FakeDashboardRepository.getDashboardSummary()          (data — suspende 600 ms)
        │ devuelve DashboardSummary (modelo de dominio)
        ▼
HomeViewModel.loadDashboard()  →  summary.toUiState()
        │ publica en MutableStateFlow
        ▼
uiState: StateFlow<HomeUiState>                        (público, solo lectura)
        │
collectAsStateWithLifecycle()                          (ui — recolecta solo con pantalla visible)
        ▼
HomeScreen dibuja: anillo 87%, contadores, gráfica, tarjetas
```

Y el cableado de dependencias ocurre una sola vez:

```
SigafApplication.container (DefaultAppContainer)
        │ entrega DashboardRepository
        ▼
HomeViewModel.Factory  →  HomeViewModel(repository)
        ▼
viewModel(factory = HomeViewModel.Factory)  en HomeScreen
```

Si algún día el backend cambia, **solo cambia la primera línea**: el resto del camino es idéntico. Eso es el valor de la arquitectura.

---

## 10. Decisiones de diseño UI/UX aplicadas

| Requisito | Implementación |
|-----------|----------------|
| Modo oscuro nativo por defecto | `SigafTheme(forceDarkTheme = true)` + fondo `#000000` |
| Tarjetas contenedoras con bordes redondeados | `SigafShapes` (12/16/20/24 dp) en `Type.kt` |
| Tipografía moderna y limpia | `SigafTypography` (sin serifas, pesos Bold/SemiBold, tracking leve) |
| Indicador circular de protección | `ProtectionRing` en `Canvas`, animado |
| Contadores de análisis/amenazas | Tres `StatCard` con color por semántica |
| Gráficos de actividad semanal | `WeeklyActivityChart` con segmento rojo de amenazas |
| Tarjetas de consejos / Academia SIGAF | Ítem 6 y 7 de la `LazyColumn` |
| Navegación inferior fija con 5 pestañas | `Scaffold(bottomBar = SigafBottomBar)` |
| Pestañas superiores en Analizar | Ruta con argumento `tab` + `AnalysisTab.TEXT/URL/SENDER` |
| Filtros de historial por riesgo | Preparado: `RiskLevel` es la clave de filtro (Bloque 4) |

---

## 11. Próximos bloques (hoja de ruta)

- **Bloque 2 — Red y sesión:** endpoints Retrofit reales, `TokenManager` con Keystore/`EncryptedSharedPreferences`, interceptores de auth y refresh, login/registro con consentimiento Ley 1581, sustituir `FakeDashboardRepository` por `RemoteDashboardRepository`.
- **Bloque 3 — Pantalla Analizar:** pestañas Mensaje/Texto, Enlace/URL y Remitente/Correo; POST `/analyze`; resultado con explicación y recomendaciones preventivas.
- **Bloque 4 — Historial y Alertas:** Room (entidades + DAO + `Flow`), lista cronológica con filtros por `RiskLevel`, borrado permanente (Habeas Data), centro de alertas.
- **Bloque 5 — Segundo plano:** `NotificationListenerService` con filtrado y detección de "información insuficiente", `WorkManager`, FCM, permiso `POST_NOTIFICATIONS` en tiempo de ejecución.
- **Bloque 6 — Perfil y datos:** DataStore (apariencia, análisis automático, umbral de notificaciones), política de tratamiento de datos, cierre de sesión seguro.

---

## 12. Guía de estudio: preguntas probables de tu exposición

**1. ¿Por qué Clean Architecture y no todo el código en la pantalla?**
Porque el sistema tiene tres capas externas (Android, FastAPI, IA) que van a evolucionar por separado. Aislar el dominio permite cambiar el backend, la base de datos o la UI sin romper lo demás, y hacer pruebas unitarias puras sin emulador.

**2. ¿Qué es MVVM y cómo se ve aquí?**
*Model* (dominio + data), *View* (`HomeScreen`, declarativa y sin lógica), *ViewModel* (`HomeViewModel`, sostiene el estado y sobrevive a rotaciones). La View observa `StateFlow`; nunca al contrario.

**3. ¿Por qué `StateFlow` y no variables sueltas?**
Es un flujo **con estado actual**, observable, seguro ante concurrencia y con ciclo de vida. Compose se suscribe con `collectAsStateWithLifecycle()`, que deja de recolectar cuando la pantalla no está visible → ahorro de batería.

**4. ¿Para qué sirve el `ProtectionRing` por dentro?**
`Canvas` + `drawArc` para los arcos, `Brush.sweepGradient` para el degradado, `rotate(-90f)` para empezar arriba y `animateFloatAsState` para animar. Cero librerías externas de gráficos.

**5. ¿Cómo se cumple el Principio de Mínimo Privilegio?**
Solo se piden `INTERNET` y `POST_NOTIFICATIONS`. No hay permisos de SMS. La escucha pasiva será un `NotificationListenerService` autorizado explícitamente por el usuario desde los ajustes del sistema.

**6. ¿Cómo evitan falsos positivos con notificaciones agrupadas?**
Con `RiskLevel.INSUFICIENTE`: la app muestra "Información insuficiente" y sugiere el análisis manual, en lugar de acusar o absolver sin datos.

**7. ¿Cómo garantizan que no se promete "seguridad absoluta"?**
El dominio solo maneja probabilidades por nivel, la UI usa el texto "Riesgo estimado" y hay un aviso legal permanente en el panel.

**8. ¿Dónde se guardan los datos del usuario?**
Hoy: en memoria (datos de ejemplo). En los bloques siguientes: Room para el historial, DataStore para preferencias y `EncryptedSharedPreferences`/Keystore para tokens. `allowBackup=false` impide que salgan del dispositivo.

**9. ¿Cómo se inyectan las dependencias si no hay Hilt?**
Con un contenedor propio (`AppContainer`) creado en `SigafApplication` y un `Factory` de ViewModel. Se cumple lo importante de la inyección —depender de interfaces y tener un único punto de construcción— sin añadir procesadores de anotaciones. Si en el Bloque 2 conviene Hilt, solo se migra el contenedor.

**10. ¿Qué pasa si el backend cambia un nombre de campo JSON?**
Se ajusta solo el `@SerialName` en el DTO y/o el mapper. La `HomeScreen` no se toca: esa es la utilidad del patrón DTO + mapper.

**11. ¿Por qué la aplicación no pide ser la app de SMS predeterminada?**
Por el Principio de Mínimo Privilegio y porque Google Play restringe ese permiso. La lectura pasiva de notificaciones con `NotificationListenerService` permite analizar SMS/WhatsApp/Gmail sin acceder a bases de datos internas ni reemplazar la app de mensajería.

**12. ¿Qué cambiaría si mañana el backend se cae?**
La UI mostraría el estado de error con botón "Reintentar", porque los errores se capturan en el ViewModel (`errorMessage = "network"`). No se rompe nada ni se inventan datos.

---

## 13. Glosario rápido

- **DTO:** objeto que refleja el JSON de la API; no se usa directamente en la UI.
- **Puerto / Adaptador:** interfaz del dominio (puerto) e implementación concreta (adaptador).
- **StateFlow:** flujo observable que siempre tiene un valor actual.
- **coroutine / suspend:** concurrencia cooperativa; `suspend` no bloquea el hilo.
- **viewModelScope:** alcance de corrutinas atado a la vida del ViewModel.
- **Composable:** función que describe UI; se re-ejecuta cuando cambia el estado que lee.
- **LazyColumn:** lista que solo compone los ítems visibles.
- **Inyección de dependencias:** entregar a un objeto las piezas que necesita en lugar de que las construya él mismo.
- **AGP (Android Gradle Plugin):** el plugin que compila proyectos Android dentro de Gradle.
- **Gradle:** el sistema de construcción (descarga dependencias, compila, empaqueta el APK).
- **compileSdk / targetSdk / minSdk:** contra qué API se compila / para cuál se optimiza / cuál es la mínima soportada.
- **KSP:** procesador de anotaciones de Kotlin (lo usan Room y Hilt); en este bloque 1 no se necesita.
- **JWT:** token firmado que identifica al usuario en cada petición.
- **Habeas Data (Ley 1581 de 2012):** normativa colombiana de protección de datos personales; exige consentimiento informado y derecho a eliminar la información.

---

**Fin del Bloque 1.** Al terminar de leer este documento deberías poder: explicar la arquitectura de tres capas, justificar cada decisión técnica (incluido el porqué de la inyección manual y del toolchain 2026) y defender por qué SIGAF es seguro y respetuoso de la privacidad **por diseño**, no por promesas.
