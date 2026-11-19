# PorAquí

PorAquí es una aplicación Android orientada a facilitar el descubrimiento de negocios y servicios locales en El Salvador, con énfasis en pequeños y medianos emprendimientos.

Su objetivo es acercar a las personas con comercios cercanos y ofrecer a los propietarios una vía para publicar información sobre su negocio, como categoría, ubicación, horario de apertura y emoji representativo.

## Índice

1. [Propósito y funciones principales](#1-propósito-y-funciones-principales)
2. [Tecnologías utilizadas](#2-tecnologías-utilizadas)
3. [Requisitos e instalación](#3-requisitos-e-instalación)
4. [Configuración y ejecución](#4-configuración-y-ejecución)
5. [Solución de problemas](#5-solución-de-problemas)

## 1. Propósito y funciones principales

### Problema que resuelve

PorAquí facilita la búsqueda de establecimientos locales y ayuda a que nuevos clientes conozcan emprendimientos cercanos. Al reunir negocios por categoría y mostrarlos en una vista de exploración y en un mapa, reduce la dificultad de encontrar servicios disponibles en una zona determinada.

### Público objetivo

- Personas interesadas en descubrir comercios y servicios locales en El Salvador.
- Usuarios que desean buscar negocios por nombre, categoría o ubicación.
- Emprendedores y propietarios que quieran registrar un negocio y compartir su ubicación.

### Funcionalidades implementadas

- Pantalla de inicio con datos del usuario y categorías disponibles.
- Inicio de sesión y registro de cuentas mediante Firebase Authentication.
- Registro de perfiles de usuario en la colección `usuarios` de Cloud Firestore.
- Registro de negocios en la colección `negocios` de Cloud Firestore.
- Visualización de negocios desde Firestore combinados con un conjunto de datos de demostración.
- Búsqueda de negocios por nombre, categoría o ubicación.
- Exploración y filtrado de negocios por categoría.
- Navegación entre Inicio, Explorar, Mapa y Perfil.
- Visualización de negocios en un mapa mediante Leaflet y WebView, usando OpenStreetMap como capa de teselas.
- Selector interactivo de ubicación para el registro de negocios mediante Leaflet y WebView.
- Perfil de usuario con nombre, correo electrónico y cierre de sesión.
- Pantalla de bienvenida y pantalla inicial de presentación.

### Funcionalidades pendientes o futuras

Las siguientes opciones aparecen en la interfaz, pero actualmente muestran información o mensajes de demostración y no cuentan con una implementación persistente verificable:

- Negocios guardados.
- Historial de visitas.
- Notificaciones.
- Configuración avanzada de la cuenta.
- Estadísticas reales de reseñas y visitas en el perfil.

La aplicación no incluye configuración ni integración directa del SDK de Google Maps. El mapa actual utiliza Leaflet cargado en `WebView`, OpenStreetMap y archivos HTML locales.

## 2. Tecnologías utilizadas

| Tecnología | Uso en el proyecto | Evidencia verificable |
|---|---|---|
| Kotlin | Lógica de la aplicación y creación de Activities y ViewModels | `app/src/main/java/com/example/poraqui/` |
| Android Studio | Entorno recomendado para abrir, sincronizar y ejecutar el proyecto | Proyecto Android con Gradle |
| XML Views | Interfaces gráficas tradicionales basadas en layouts XML | `app/src/main/res/layout/` |
| ViewBinding | Acceso seguro a las vistas de los layouts | `buildFeatures { viewBinding = true }` |
| MVVM | Separación de interfaz, estado y lógica mediante ViewModels, LiveData y repositorios | `ui/`, `viewmodel/` y `data/repository/` |
| Firebase Authentication | Inicio de sesión, registro y cierre de sesión | Dependencia `firebase-auth` |
| Cloud Firestore | Perfil de usuarios y registro y consulta de negocios | Dependencia `firebase-firestore` y colección `negocios` |
| Gradle Kotlin DSL | Automatización de compilación y configuración del módulo Android | `build.gradle.kts` y `settings.gradle.kts` |
| AndroidX y Material Components | Componentes de interfaz y compatibilidad | Dependencias en `app/build.gradle.kts` |
| OkHttp | Biblioteca HTTP incluida como dependencia | `okhttp:4.12.0` |
| Leaflet 1.9.4 | Representación de mapas dentro de WebViews | `app/src/main/assets/leaflet_map.html` y `leaflet_picker.html` |
| OpenStreetMap | Fuente de las teselas del mapa | URL de teselas en `leaflet_map.html` |

### Arquitectura del proyecto

La aplicación sigue una organización cercana a MVVM:

```text
app/src/main/java/com/example/poraqui/
├── MainActivity.kt
├── data/
│   ├── model/
│   └── repository/
├── ui/
│   ├── auth/
│   ├── business/
│   ├── explore/
│   ├── home/
│   ├── map/
│   ├── profile/
│   └── splash/
└── viewmodel/
```

### Estado de las integraciones remotas

- Firebase está declarado en el proyecto y se utiliza en las funcionalidades de autenticación y Firestore.
- Para iniciar sesión, registrarse o guardar y consultar negocios se necesita una configuración válida de Firebase.
- Si Firebase no está configurado, la aplicación puede fallar al sincronizar Gradle o al realizar operaciones remotas.
- Los datos estáticos de demostración permiten mostrar algunos negocios de forma local si la consulta a Firestore falla.

## 3. Requisitos e instalación

### Requisitos de software

- Android Studio compatible con Gradle 9.6.0 y Android Gradle Plugin 9.4.1.
- JDK compatible con el código configurado para Java 11.
- SDK de Android con API 36 instalada.
- Un emulador Android o un dispositivo físico con Android 7.0 (API 24) o superior.
- Git para clonar el repositorio.
- Conexión a Internet para la sincronización de dependencias y los servicios de Firebase.
- Conexión a Internet para cargar Leaflet desde CDN y las teselas de OpenStreetMap.

Versiones verificadas en el proyecto:

| Componente | Versión |
|---|---|
| Android Gradle Plugin | 9.4.1 |
| Gradle Wrapper | 9.6.0 |
| SDK mínimo | API 24 |
| SDK objetivo | API 36 |
| SDK de compilación | API 36 |
| Java source/target | 11 |
| Versión de la aplicación | 1.0 |

### Clonar el repositorio

Desde una terminal:

```bash
git clone https://github.com/vc242100091-droid/PorAqui.git
cd PorAqui
```

### Abrir en Android Studio

1. Abra Android Studio.
2. Seleccione **Open**.
3. Busque la carpeta `PorAqui` clonada y ábrala.
4. Espere a que Android Studio detecte el proyecto Gradle.
5. Permita la descarga o instalación de las herramientas que Android Studio indique.

No seleccione `app/` como proyecto principal: abra la carpeta raíz que contiene `settings.gradle.kts`.

### Sincronizar y compilar

En Android Studio:

1. Ejecute **File > Sync Project with Gradle Files**.
2. Espere a que finalice la descarga de dependencias.
3. Seleccione el módulo `app`.
4. Ejecute **Build > Make Project** o use el botón de compilación.

También puede compilar desde la raíz del proyecto:

**Windows:**

```bat
gradlew.bat assembleDebug
```

**Linux o macOS:**

```bash
./gradlew assembleDebug
```

El APK de depuración se genera normalmente en:

```text
app/build/outputs/apk/debug/
```

## 4. Configuración y ejecución

### Configurar Firebase

> **Advertencia de seguridad:** el archivo `google-services.json` contiene información de configuración de Firebase y **no debe publicarse en GitHub**. El proyecto ya excluye este archivo mediante la configuración `.gitignore`; no se debe eliminar esa exclusión ni subir el archivo al repositorio.

Antes de probar autenticación, registros o Firestore:

1. Acceda a Firebase Console.
2. Cree un proyecto o seleccione el proyecto configurado para PorAquí.
3. Registre una aplicación Android cuyo nombre de paquete coincida con:

```text
com.example.poraqui
```

4. Registre el nombre de paquete en la configuración de Firebase.
5. Descargue el archivo `google-services.json` generado para la aplicación Android.
6. Coloque el archivo descargado directamente en:

```text
app/google-services.json
```

La ubicación requerida por el complemento de Google Services es el módulo de la aplicación, no la raíz del proyecto.

No copie claves privadas, contraseñas ni credenciales dentro del README o del código fuente. El archivo `google-services.json` debe permanecer únicamente en el entorno local o manejarse mediante las políticas de secretos del equipo.

### Configurar Authentication

En Firebase Console:

1. Abra **Authentication**.
2. Seleccione **Sign-in method**.
3. Habilite **Email/Password**.

Sin este proveedor habilitado, el registro y el inicio de sesión pueden mostrar un error de configuración.

### Configurar Firestore

En Firebase Console:

1. Abra **Firestore Database**.
2. Cree una base de datos si aún no existe.
3. Configure reglas de seguridad apropiadas para el entorno de prueba o producción.

La aplicación espera datos en las siguientes colecciones:

- `usuarios`: contiene perfiles creados durante el registro.
- `negocios`: contiene la información y las coordenadas de los negocios registrados.

No se deben colocar datos sensibles en los documentos públicos de Firestore sin aplicar reglas de seguridad adecuadas.

### Sincronizar Gradle

Después de colocar `app/google-services.json`:

1. En Android Studio seleccione **File > Sync Project with Gradle Files**.
2. Compruebe que no existan errores de descarga de dependencias.
3. Confirme que el módulo `app` esté seleccionado.

### Seleccionar un dispositivo

Puede usar:

- Un emulador creado en **Device Manager**.
- Un teléfono Android conectado por USB con Depuración USB habilitada.

El dispositivo debe cumplir al menos API 24. Para utilizar las funciones de inicio de sesión y registro necesita una conexión a Internet.

### Ejecutar la aplicación

1. Seleccione el módulo o configuración **app**.
2. Seleccione un dispositivo disponible.
3. Pulse **Run** o presione **Shift + F10**.
4. Espere a que Android Studio instale y abra PorAquí.

Desde la terminal:

```bash
./gradlew installDebug
```

> El uso de `./gradlew installDebug` requiere un dispositivo o emulador disponible.

### Comprobación del inicio correcto

Cuando la aplicación inicia correctamente:

1. Debe aparecer la pantalla inicial o Splash.
2. Después debe abrirse la pantalla de bienvenida.
3. Puede continuar a iniciar sesión o crear una cuenta.
4. Tras autenticarse, debe abrirse la pantalla principal con categorías y negocios.
5. Las pestañas inferiores deben permitir abrir Inicio, Explorar, Mapa y Perfil.

Sin Firebase configurado, la interfaz puede abrirse, pero el registro, inicio de sesión y operaciones con Firestore no funcionarán correctamente. Los datos de demostración pueden continuar visibles si la consulta remota falla.

## 5. Solución de problemas

### No se encuentra `google-services.json`

Asegúrese de que el archivo esté exactamente en:

```text
app/google-services.json
```

Luego sincronice Gradle nuevamente. No publique ni confirme el archivo en GitHub.

### Error de nombre de paquete

Firebase debe registrar el nombre de paquete:

```text
com.example.poraqui
```

Si el proyecto Firebase usa otro nombre de paquete, descargue una configuración correspondiente o use el proyecto autorizado por el propietario del repositorio.

### Error de Firebase Authentication

Habilite **Email/Password** en Firebase Console. Compruebe además que el dispositivo tenga Internet y que el correo tenga un formato válido.

### Firestore no muestra negocios registrados

Verifique:

- Que `google-services.json` sea válido.
- Que el proyecto tenga Firestore habilitado.
- Que exista la colección `negocios`.
- Que las reglas permitan lectura y escritura a la cuenta utilizada.
- Que los documentos tengan campos compatibles, como `name`, `category`, `location`, `latitude` y `longitude`.

Si la consulta falla, la aplicación continúa con la lista estática de negocios demostrativos definida en `BusinessRepository.kt`.

### El mapa no muestra capas ni marcadores

Compruebe:

- Conexión a Internet.
- Acceso a `https://unpkg.com/leaflet@1.9.4/`.
- Acceso a `https://{s}.tile.openstreetmap.org/`.
- Que JavaScript esté habilitado en el `WebView`.
- Que los negocios tengan valores válidos de `latitude` y `longitude`.

### Error de SDK o versión de Gradle

Instale o active el SDK API 36 desde el **SDK Manager** de Android Studio. Permita que Android Studio sincronice el wrapper de Gradle y descargue las dependencias declaradas.

### Dependencias o compilación desactualizadas

Ejecute:

```bash
./gradlew clean assembleDebug
```

En Windows:

```bat
gradlew.bat clean assembleDebug
```

No modifique manualmente las versiones ni las dependencias salvo que el equipo del proyecto lo autorice.

## Aplicación Android

- ID de aplicación: `com.example.poraqui`
- Versión: `1.0` (`versionCode` 1)
- SDK mínimo: API 24
- SDK objetivo y de compilación: API 36
