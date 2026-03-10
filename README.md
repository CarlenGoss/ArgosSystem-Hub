# Open Camera Visor - Android Client 📱

Esta es la aplicación cliente (Frontend) del ecosistema **Open Camera Visor**, desarrollada nativamente en Android con Kotlin. Actúa como el centro de control para visualizar, administrar y reproducir transmisiones de seguridad provenientes de servidores remotos a través de una red privada (Tailscale).

## ✨ Características Destacadas

* **Interfaz Material Design 3:** Diseño moderno, limpio y profesional, optimizado para aprovechar toda la pantalla (Edge-to-Edge) y separar dinámicamente el reproductor de los controles.
* **Escáner QR Integrado:** Agrega nuevas cámaras instantáneamente escaneando un código QR que contiene la IP y el puerto del servidor.
* **Reproductor Nativo (ExoPlayer):** Transmisión fluida de archivos de video y flujos remotos directamente en la aplicación.
* **Gestión Dinámica de Eventos:** Consume automáticamente una API REST en segundo plano (usando Corrutinas) para listar los videos de movimiento grabados y reproducirlos con un solo toque.
* **Persistencia Local:** Guarda la configuración y el inventario de cámaras de forma segura en el dispositivo usando `SharedPreferences`.

## 🛠️ Tecnologías y Librerías Utilizadas

* **Lenguaje:** Kotlin
* **Arquitectura de UI:** ViewBinding & Navigation Component (Single-Activity Architecture)
* **Multimedia:** [Media3 / ExoPlayer](https://developer.android.com/media/media3)
* **Escaneo QR:** [ZXing (Zebra Crossing) Android Embedded](https://github.com/journeyapps/zxing-android-embedded)
* **Asincronía:** Kotlin Coroutines (`lifecycleScope`, `Dispatchers.IO`)
* **Red:** Soporte para tráfico HTTP en redes privadas virtuales (Tailscale).

## 🚀 Instalación y Uso

1. Clona este repositorio: `git clone https://github.com/TU-USUARIO/OpenCameraVisor-Android.git`
2. Abre el proyecto en **Android Studio**.
3. Sincroniza el proyecto con los archivos de Gradle.
4. Compila y ejecuta en un dispositivo físico (recomendado para usar el hardware de la cámara al escanear el QR).

> **Nota importante:** Para visualizar los videos, asegúrate de que el dispositivo Android esté conectado a tu red de **Tailscale** y tenga acceso a la IP del servidor.

## 🔗 Ecosistema Backend

Esta aplicación requiere de un servidor activo para obtener la lista de eventos y las transmisiones de video. Puedes encontrar el repositorio del servidor aquí:
* [Open Camera Visor - Backend Server](enlace-a-tu-repo-del-servidor-aqui)

---
*Desarrollado como solución integral de monitoreo remoto y administración de cámaras.*
