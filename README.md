# Open Camera Visor - Backend Server 📹

Este repositorio contiene el código del servidor (Backend) diseñado para ejecutarse en un dispositivo dedicado (ej. Samsung Galaxy S10) como parte del ecosistema **Open Camera Visor**. 

El servidor se encarga de monitorear, registrar eventos de movimiento en formato `.mp4` y exponer una API RESTful ligera para transmitir estos videos bajo demanda a los clientes autorizados a través de una red privada virtual (Tailscale).

## 🚀 Características Principales

* **Detección y Grabación:** Captura eventos de movimiento y los almacena localmente en formato MP4.
* **API RESTful:** Proporciona endpoints estructurados para consultar el inventario de videos y solicitar transmisiones de medios.
* **Streaming Directo:** Transmite video en tiempo real compatible con reproductores modernos como ExoPlayer.
* **Red Privada Segura:** Diseñado para operar sobre **Tailscale**, eliminando la necesidad de abrir puertos en el router y garantizando que solo los dispositivos autenticados en la malla (Tailnet) puedan acceder al flujo de video.

## 📡 Endpoints de la API

El servidor opera por defecto en el puerto `8080`. Asegúrate de usar la IP asignada por Tailscale (ej. `100.x.x.x`) para realizar las peticiones.

### 1. Obtener lista de eventos
Devuelve un inventario en formato JSON con todos los clips de video grabados disponibles en el servidor.

* **URL:** `/videos`
* **Método:** `GET`
* **Respuesta Exitosa (200 OK):**
  ```json
  [
    "Motion_2026-03-10_02-53-45.mp4",
    "Motion_2026-03-10_05-12-10.mp4"
  ]
