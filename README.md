# AxionStaff - Wiki Oficial

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg) ![Java](https://img.shields.io/badge/Java-25%2B-red.svg) ![PaperMC](https://img.shields.io/badge/Paper--Folia-1.21--26.2%2B-green.svg) ![Languages](https://img.shields.io/badge/Languages-🇺🇸_EN_%7C_🇪🇸_ES-blue.svg)

¡Bienvenido a la Wiki oficial de **AxionStaff**! 
AxionStaff es un plugin avanzado de moderación diseñado para servidores de Minecraft. Construido nativamente para PaperMC y compatible con la arquitectura multihilo de Folia, proporciona una suite completa de herramientas de moderación, incluyendo sistemas de sanciones, modo staff, detección anti-xray y sincronización entre servidores vía bases de datos MySQL.

---

## Tabla de Contenidos
1. [Características Principales](#1-características-principales) 
2. [Guía de Instalación](#2-guía-de-instalación)
3. [Archivos de Configuración](#3-archivos-de-configuración)
4. [Lista de Comandos](#4-lista-de-comandos)
5. [Nodos de Permisos](#5-nodos-de-permisos)
6. [Variables de PlaceholderAPI](#6-variables-de-placeholderapi)
7. [Detalles de Módulos y Sistemas](#7-detalles-de-módulos-y-sistemas)
8. [Guía de Integración con Discord](#8-guía-de-integración-con-discord)

---

## 1. Características Principales
* **Modo Staff Completo:** Inventario de herramientas personalizable (Freeze, Sancionar, Vanish, Modo Vuelo, etc.).
* **Sistema Avanzado de Sanciones:** Soporte para Bans, Mutes, Kicks, Warns, Bans de IP, Historial interactivo y menú GUI de sanciones.
* **Sincronización Multi-Servidor (MySQL):** Comparte el historial de castigos, mutes activos y estados de Vanish en toda tu red (BungeeCord/Velocity).
* **Sistema Anti-Xray Inteligente:** Alertas de minería configurables basadas en umbrales de tiempo y sesión para evitar el spam en el chat.
* **Client Tracker:** Detecta si los jugadores entran con clientes modificados (LunarClient, LabyMod, PvPLounge, etc.) interceptando el paquete "Brand".
* **Integración con Discord:** Envía registros de sanciones, reportes del servidor y chat del staff directamente a webhooks de Discord.
* **Rastreo de Cuentas Alternas (Alts):** Detecta múltiples cuentas jugando desde la misma dirección IP.
* **Revivir al Morir (Death Revive):** Restaura el inventario exacto y la experiencia de los jugadores que han muerto recientemente.
* **Soporte Multi-Idioma:** Archivos en Inglés y Español integrados por defecto.

---

## 2. Guía de Instalación

### Requisitos Previos
1. **Java 25** o superior (Estrictamente requerido por el motor interno del plugin).
2. Un servidor usando **PaperMC** (API 1.13 a 1.21+) o forks compatibles como Purpur y **Folia**.
3. *(Opcional)* Base de datos **MySQL/MariaDB** para habilitar la sincronización en la red (Network).
4. *(Opcional)* **PlaceholderAPI** para usar variables en otros plugins o scoreboards.

### Pasos de Instalación
1. Descarga el archivo `.jar` compilado más reciente desde la página oficial de Descarga.
2. Coloca el archivo (ej. `AxionStaff-1.0.0.jar`) en la carpeta `plugins/` de tu servidor.
3. Inicia el servidor por primera vez para generar los archivos de configuración por defecto.
4. *(Opcional)* Navega a `plugins/AxionStaff/config.yml` para configurar tu conexión a la base de datos MySQL (ideal si usas BungeeCord/Velocity).
5. Usa el comando `/axionstaff reload` o reinicia el servidor para aplicar los cambios.

> **Nota:** ¡No necesitas compilar nada! Solo arrastra el archivo `.jar` y el plugin estará listo para usarse en tu servidor.

---

## 3. Archivos de Configuración

AxionStaff genera múltiples archivos dentro del directorio `plugins/AxionStaff/`. Cada archivo tiene un propósito específico:

### `config.yml`
Este es el archivo principal donde se define el comportamiento general del plugin:
* **Módulos:** Permite desactivar sistemas enteros bajo la sección `modules:` (ej. poner `anti-xray: false` si usas una alternativa externa).
* **Base de Datos:** Cambia `database.enabled` a `true` y configura tus credenciales para conectar a MySQL. Es imperativo configurar un `server-id` único para cada servidor en la red para evitar conflictos de datos.
* **Idioma:** En la sección `general.language`, pon `"es"` para Español o `"en"` para Inglés.
* **Anti-Xray:** Define los bloques monitoreados y los umbrales de alerta por ventana de tiempo y por sesión.
* **Command Spy:** Configura comandos sensibles que deben ser ocultados para proteger la privacidad del usuario (ej. contraseñas de /login).

### `discord.yml`
Vincula las acciones de tu Staff a tu servidor de Discord. Soporta múltiples canales para diferentes tipos de notificaciones (Reportes, Sanciones, Chat del Staff). Para usarlo, habilita la opción deseada e inserta tu URL de Webhook.

### Directorio `lang/` (`messages_en.yml` y `messages_es.yml`)
Estos archivos contienen todas las cadenas de texto, prefijos y mensajes enviados por el plugin. Pueden ser totalmente personalizados (colores, formatos y traducciones).

---

## 4. Lista de Comandos

### Comandos de Moderación
| Comando | Alias | Descripción |
| :--- | :--- | :--- |
| `/axionstaff` | `/as` | Comando principal para información del plugin y recargas. |
| `/staff` | `/s`, `/staffmode` | Activa o desactiva el Modo Staff. |
| `/ban <jugador> [tiempo] [razón]` | - | Aplica un ban temporal o permanente a un jugador. |
| `/mute <jugador> [tiempo] [razón]` | - | Mutea el chat de un jugador temporal o permanentemente. |
| `/kick <jugador> [razón]` | - | Expulsa a un jugador del servidor. |
| `/warn <jugador> [razón]` | - | Emite una advertencia formal a un jugador. |
| `/history <jugador>` | - | Abre un menú visual mostrando el historial de sanciones. |
| `/sanction <jugador>` | - | Abre el menú GUI avanzado de sanciones. |
| `/silent <comando>` | - | Ejecuta un castigo (ban, mute) sin anunciar el mensaje en el chat público. |
| `/ban-ip <ip/jugador>` | - | Banea permanentemente una dirección IP. |
| `/tempban-ip <ip/jugador>` | - | Banea temporalmente una dirección IP. |
| `/unban <jugador>` | - | Elimina el ban de un jugador. |
| `/unmute <jugador>` | - | Elimina el mute de chat de un jugador. |
| `/unban-ip <ip>` | - | Elimina el ban de una dirección IP. |

### Comandos de Utilidad y Staff
| Comando | Alias | Descripción |
| :--- | :--- | :--- |
| `/vanish` | `/v` | Entra en modo espectador oculto (el estado persiste entre sesiones). |
| `/freeze <jugador>` | `/ss` | Congela a un jugador para revisión (SS). |
| `/unfreeze <jugador>` | `/uss` | Descongela a un jugador. |
| `/sc <mensaje>` | `/staffchat` | Envía un mensaje al chat privado del equipo de administración. |
| `/cmdspy` | - | Monitorea los comandos ejecutados por los usuarios en tiempo real. |
| `/chat` | - | Opciones de administración del chat global (Mute/Clear). |
| `/alts <jugador>` | `/accs` | Muestra las cuentas asociadas por dirección IP al jugador especificado. |
| `/report <jugador> <razón>` | `/reportar` | Permite a los usuarios reportar comportamientos inapropiados. |
| `/invsee <jugador>` | - | Inspecciona el inventario de otro jugador en tiempo real. |
| `/revive <jugador>` | - | Restaura el inventario y estado reciente de muerte del jugador. |
| `/xray` | - | Menú para revisar jugadores que han generado alertas de Anti-Xray. |
| `/fly [velocidad]` | - | Alterna el modo vuelo o establece la velocidad de vuelo (0.1 - 1.0). |
| `/gamemode` | `/gm` | Selector de modo de juego interactivo vía GUI. |

---

## 5. Nodos de Permisos

El sistema de permisos de AxionStaff es detallado, diseñado para mantener una jerarquía estricta.

| Nodo de Permiso | Valor por Defecto | Descripción |
| :--- | :--- | :--- |
| `axionstaff.admin` | `op` | Acceso total a AxionStaff y a todos los comandos administrativos. |
| `axionstaff.command.staff` | `op` | Permite el uso del comando `/staff` para entrar al modo moderador. |
| `axionstaff.punish.ban` | `op` | Permite el uso de `/ban` y `/tempban`. |
| `axionstaff.punish.mute` | `op` | Permite el uso de `/mute` y `/tempmute`. |
| `axionstaff.punish.kick` | `op` | Permite el uso de `/kick`. |
| `axionstaff.punish.warn` | `op` | Permite el uso de `/warn`. |
| `axionstaff.punish.unban` | `op` | Permite el uso de `/unban`. |
| `axionstaff.punish.unmute` | `op` | Permite el uso de `/unmute`. |
| `axionstaff.punish.banip` | `op` | Permite el uso de `/ban-ip` y `/tempban-ip`. |
| `axionstaff.punish.unbanip`| `op` | Permite el uso de `/unban-ip`. |
| `axionstaff.history` | `op` | Permite ver el historial de sanciones vía `/history`. |
| `axionstaff.punish` | `op` | Permite abrir el menú avanzado usando `/sanction`. |
| `axionstaff.sanctions.list`| `op` | Permite ver los bans activos usando `/sanction list`. |
| `axionstaff.punish.silent` | `op` | Permite ejecutar castigos silenciosos vía `/silent`. |
| `axionstaff.punish.override`| `op` | Permite saltar las comprobaciones de protección y sancionar a otros miembros del Staff. |
| `axionstaff.punish.protected`| `false` | Protege al jugador de ser sancionado por miembros del Staff de menor rango. |
| `axionstaff.vanish` | `op` | Permite el uso del comando `/vanish`. |
| `axionstaff.see.vanish` | `op` | Permite ver a otros miembros del Staff que están en modo Vanish. |
| `axionstaff.vanish.join` | `false` | Activa automáticamente el modo Vanish al entrar al servidor. |
| `axionstaff.staffchat` | `op` | Permite leer y escribir en el chat del Staff. |
| `axionstaff.cmdspy` | `op` | Permite usar `/cmdspy` para auditar comandos de usuarios. |
| `axionstaff.cmdspy.raw` | `false` | *(Crítico)* Permite ver argumentos sensibles y contraseñas sin censura en los registros de `/cmdspy`. |
| `axionstaff.cmdspy.raw.owners`| `op` | Nodo de agrupación destinado exclusivamente para owners, incluye acceso al cmdspy sin censura. |
| `axionstaff.chat.admin` | `op` | Permite mutear y limpiar el chat global. |
| `axionstaff.gamemode` | `op` | Permite usar el menú `/gm`. |
| `axionstaff.alts` | `op` | Permite el uso del sistema de rastreo de cuentas alternas. |
| `axionstaff.alts.override` | `op` | Permite ver las cuentas alternas de jugadores protegidos jerárquicamente. |
| `axionstaff.alts.protected`| `false` | Oculta las cuentas alternas del jugador al escrutinio del Staff de menor rango. |
| `axionstaff.freeze` | `op` | Permite el uso de `/freeze` y `/unfreeze`. |
| `axionstaff.report` | `true` | Permite a los jugadores ejecutar `/report` (Otorgado a todos por defecto). |
| `axionstaff.report.notify` | `op` | Permite al Staff recibir notificaciones en tiempo real sobre nuevos reportes. |
| `axionstaff.report.bypass` | `op` | Permite saltar los tiempos de espera (cooldowns) al enviar reportes. |
| `axionstaff.invsee` | `op` | Permite el uso de `/invsee`. |
| `axionstaff.revive` | `op` | Permite el uso de `/revive` para restaurar inventarios perdidos al morir. |
| `axionstaff.fly` | `op` | Permite el uso de `/fly` y la modificación de su velocidad. |
| `axionstaff.client.notify` | `op` | Permite recibir notificaciones cuando se detecta el uso de un cliente modificado. |
| `axionstaff.antixray.alert`| `op` | Permite recibir notificaciones del sistema Anti-Xray en tiempo real. |
| `axionstaff.antixray.bypass`| `false` | Evita que el sistema Anti-Xray genere alertas de minería para este jugador. |

---

## 6. Variables de PlaceholderAPI

Si PlaceholderAPI está instalado en el servidor, AxionStaff expone las siguientes variables para su integración en scoreboards, chats y otros plugins compatibles.

* `%axionstaff_in_staff_mode%` - Devuelve el estado actual del Modo Staff (true/false).
* `%axionstaff_vanished%` - Devuelve el estado actual del modo Vanish (true/false).
* `%axionstaff_frozen%` - Devuelve si el jugador está actualmente congelado (true/false).
* `%axionstaff_is_spy%` - Devuelve si el jugador tiene CommandSpy activado (true/false).
* `%axionstaff_warn_count%` - Devuelve el número total de advertencias emitidas al jugador.
* `%axionstaff_ban_count%` - Devuelve el número total de bans emitidos al jugador.
* `%axionstaff_mute_count%` - Devuelve el número total de mutes emitidos al jugador.
* `%axionstaff_kick_count%` - Devuelve el número total de kicks emitidos al jugador.
* `%axionstaff_total_punishments%` - Devuelve la suma total de castigos aplicados al jugador.
* `%axionstaff_playtime%` - Devuelve el tiempo de juego formateado del jugador (ej. `12h 30m`).

*(Nota: El texto de salida para las variables booleanas (true/false) puede ser configurado en la sección correspondiente de `config.yml`).*

---

## 7. Detalles de Módulos y Sistemas

### Sistema Visual de Freeze (TextDisplay)
A diferencia de los sistemas de freeze convencionales, el comando `/freeze` invoca una entidad de texto (TextDisplay) directamente en el campo de visión del usuario objetivo. Esto bloquea toda entrada de comandos, movimiento de cámara e interacción física.
* Si el infractor intenta desconectarse mientras el sistema está activo, el plugin ejecutará un **Ban de Seguridad Automático** (establecido en 7 días de duración por defecto) por evasión.

### Privacidad y Seguridad (CommandSpy)
AxionStaff protege activamente la información confidencial. Cuando CommandSpy está activado, los comandos sensibles definidos en `config.yml` (como contraseñas de AuthMe o tokens de autenticación) son censurados usando una máscara (ej. `******`). Esto permite al equipo de moderación auditar el uso de comandos sin comprometer las credenciales de los usuarios. Solo el staff de alto rango con el permiso `axionstaff.cmdspy.raw` puede ver los argumentos originales.

### Inspección de Inventario Compacta (InvSee)
El comando InvSee genera una interfaz unificada que renderiza el inventario principal, la mano secundaria y las piezas de armadura simultáneamente. Este diseño compacto elimina la necesidad de ventanas secundarias y facilita las revisiones rápidas (ScreenShares).

### Sistema de Restauración (Death Revive)
El plugin almacena el estado exacto del inventario y la experiencia cada vez que un jugador muere. Si ocurre una muerte debido a una falla técnica del servidor o intervención no autorizada de terceros, el comando `/revive` abre un panel interactivo permitiendo una restauración completa del inventario perdido con un solo clic.

---

## 8. Guía de Integración con Discord

Para configurar `discord.yml` y conectar AxionStaff a tu servidor de Discord, sigue estos pasos para configurar los Webhooks:

1. **Abre los Ajustes de tu Servidor de Discord**
   Ve a **Ajustes del Servidor** > **Integraciones** > **Webhooks**.

2. **Crea un Nuevo Webhook**
   Haz clic en **Nuevo Webhook**. Ponle nombre (ej. "AxionStaff") y selecciona el canal donde quieres que aparezcan las notificaciones (como `#chat-staff` o `#sanciones`).

3. **Copia la URL del Webhook**
   Haz clic en el botón **Copiar URL de Webhook**.

4. **Pégalo en `discord.yml`**
   Abre `plugins/AxionStaff/discord.yml` en tu servidor de Minecraft.
   Localiza el módulo que quieres habilitar (por ejemplo, `sanctions` o `staff-chat`) y pega la URL en el campo `webhook-url`:
   ```yaml
   sanctions:
     enabled: true
     webhook-url: "https://discord.com/api/webhooks/tu_url_de_webhook_aqui"
   ```

5. **Recarga el Plugin**
   Guarda el archivo y ejecuta `/axionstaff reload` en tu juego o en la consola del servidor. ¡Tu integración con Discord ahora está activa!

---
> ¿Necesitas soporte adicional? ¡Únete a nuestro **Servidor de Discord** https://discord.gg/ym4x6jmSNh