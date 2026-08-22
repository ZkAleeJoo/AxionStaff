# AxionStaff - Official Documentation

![Version](https://img.shields.io/badge/version-1.0.0-blue.svg) ![Java](https://img.shields.io/badge/Java-25%2B-red.svg) ![PaperMC](https://img.shields.io/badge/PaperMC-1.21--26.2%2B-green.svg) ![Languages](https://img.shields.io/badge/Languages-🇺🇸_EN_%7C_🇪🇸_ES-blue.svg)

AxionStaff is an advanced moderation plugin designed for Minecraft servers. Built natively for PaperMC and compatible with Folia's multi-threading architecture, it provides a comprehensive suite of moderation tools including sanction systems, staff mode, anti-xray detection, and cross-server synchronization via MySQL databases.

## Table of Contents
1. [Main Features](#1-main-features)
2. [Installation Guide](#2-installation-guide)
3. [Configuration Files](#3-configuration-files)
4. [Commands List](#4-commands-list)
5. [Permission Nodes](#5-permission-nodes)
6. [PlaceholderAPI Variables](#6-placeholderapi-variables)
7. [Modules and Systems Details](#7-modules-and-systems-details)
8. [Discord Configuration Guide](#8-discord-configuration-guide)

---

## 1. Main Features
* **Complete Staff Mode:** Customizable tool inventory (Freeze, Sanction, Vanish, Fly Mode, etc.).
* **Advanced Sanctions System:** Support for Bans, Mutes, Kicks, Warns, IP Bans, interactive History, and Sanctions GUI menu.
* **Multi-Server Synchronization (MySQL):** Share punishment history, active mutes, and Vanish states across the entire network (BungeeCord/Velocity).
* **Smart Anti-Xray System:** Configurable mining alerts based on time and session thresholds to avoid chat spam.
* **Client Tracker:** Detects if players join with modified clients (LunarClient, LabyMod, PvPLounge, etc.) by intercepting the "Brand" packet.
* **Discord Integration:** Sends sanction logs, server reports, and staff chat directly to Discord webhooks.
* **Alt Account Tracking:** Detects multiple accounts playing from the same IP address.
* **Death Revive:** Restores the exact inventory and experience of players who have recently died.
* **Multi-Language Support:** English and Spanish files integrated by default.

---

## 2. Installation Guide

### Prerequisites
1. **Java 25** or higher (Strictly required by the plugin's internal engine).
2. A server running **PaperMC** (API 1.13 to 1.21+) or compatible forks like Purpur and **Folia**.
3. (Optional) **MySQL/MariaDB** database to enable network synchronization.
4. (Optional) **PlaceholderAPI** to use variables in other plugins.

### Installation Steps
1. Compile the project using Maven (`mvn clean package`) or download the compiled `.jar` file.
2. Place the resulting file (e.g., `AxionStaff-1.0.0.jar`) in your server's `plugins/` folder.
3. Start the server for the first time to generate the default configuration files.
4. (Optional) Navigate to `plugins/AxionStaff/config.yml` to configure your MySQL database connection.
5. Use the `/axionstaff reload` command or restart the server to apply the changes.

---

## 3. Configuration Files

AxionStaff generates multiple files inside the `plugins/AxionStaff/` directory. Each file serves a specific purpose in the plugin's ecosystem.

### `config.yml`
This is the main configuration file where the plugin's general behavior is defined:
* **Modules:** Allows disabling entire systems under the `modules:` section (e.g., setting `anti-xray: false` if you use an external alternative).
* **Database:** Change `database.enabled` to `true` and configure your credentials to connect to MySQL. It is imperative to configure a unique `server-id` for each server in the network to prevent data conflicts.
* **Language:** In the `general.language` section, set `"es"` for Spanish or `"en"` for English.
* **Anti-Xray:** Define monitored blocks and alert thresholds per time window and per session.
* **Command Spy:** Configure sensitive commands that must be masked to protect user privacy (e.g., login passwords).

### `discord.yml`
Links your Staff actions to your Discord server. Supports multiple channels for different types of notifications (Reports, Sanctions, Staff Chat). To use it, enable the desired option and insert your Webhook URL.

### `lang/` Directory (`messages_en.yml` and `messages_es.yml`)
These files contain all text strings, prefixes, and messages sent by the plugin. They can be entirely customized (colors, formats, and translations).

---

## 4. Commands List

### Moderation Commands
| Command | Aliases | Description |
| :--- | :--- | :--- |
| `/axionstaff` | `/as` | Main command for plugin information and reloading. |
| `/staff` | `/s`, `/staffmode` | Toggles Staff Mode on or off. |
| `/ban <player> [time] [reason]` | - | Applies a temporary or permanent ban to a player. |
| `/mute <player> [time] [reason]` | - | Temporarily or permanently mutes a player's chat. |
| `/kick <player> [reason]` | - | Kicks a player from the server. |
| `/warn <player> [reason]` | - | Issues a formal warning to a player. |
| `/history <player>` | - | Opens a visual menu displaying sanction history. |
| `/sanction <player>` | - | Opens the advanced sanctions GUI menu. |
| `/silent <command>` | - | Executes a punishment (ban, mute) without announcing the message in public chat. |
| `/ban-ip <ip/player>` | - | Permanently bans an IP address. |
| `/tempban-ip <ip/player>` | - | Temporarily bans an IP address. |
| `/unban <player>` | - | Removes a player's ban. |
| `/unmute <player>` | - | Removes a player's chat mute. |
| `/unban-ip <ip>` | - | Removes an IP address ban. |

### Utility and Staff Commands
| Command | Aliases | Description |
| :--- | :--- | :--- |
| `/vanish` | `/v` | Enters hidden spectator mode (state persists between sessions). |
| `/freeze <player>` | `/ss` | Freezes a player for review. |
| `/unfreeze <player>` | `/uss` | Unfreezes a player. |
| `/sc <message>` | `/staffchat` | Sends a message to the private administration team chat. |
| `/cmdspy` | - | Monitors user-executed commands in real-time. |
| `/chat` | - | Global chat administration options (Mute/Clear). |
| `/alts <player>` | `/accs` | Shows accounts associated by IP address to the specified player. |
| `/report <player> <reason>` | `/reportar` | Allows users to report inappropriate behavior. |
| `/invsee <player>` | - | Inspects another player's inventory in real-time. |
| `/revive <player>` | - | Restores the player's recent death inventory and state. |
| `/xray` | - | Menu to review players who have generated Anti-Xray alerts. |
| `/fly [speed]` | - | Toggles flight mode or sets flight speed (0.1 - 1.0). |
| `/gamemode` | `/gm` | Interactive game mode selector via GUI. |

---

## 5. Permission Nodes

AxionStaff's permission system is granular, designed to maintain a strict hierarchy.

| Permission Node | Default Value | Description |
| :--- | :--- | :--- |
| `axionstaff.admin` | `op` | Full access to AxionStaff and all administrative commands. |
| `axionstaff.command.staff` | `op` | Allows the use of the `/staff` command to enter moderator mode. |
| `axionstaff.punish.ban` | `op` | Allows the use of `/ban` and `/tempban`. |
| `axionstaff.punish.mute` | `op` | Allows the use of `/mute` and `/tempmute`. |
| `axionstaff.punish.kick` | `op` | Allows the use of `/kick`. |
| `axionstaff.punish.warn` | `op` | Allows the use of `/warn`. |
| `axionstaff.punish.unban` | `op` | Allows the use of `/unban`. |
| `axionstaff.punish.unmute` | `op` | Allows the use of `/unmute`. |
| `axionstaff.punish.banip` | `op` | Allows the use of `/ban-ip` and `/tempban-ip`. |
| `axionstaff.punish.unbanip`| `op` | Allows the use of `/unban-ip`. |
| `axionstaff.history` | `op` | Allows viewing sanction history via `/history`. |
| `axionstaff.punish` | `op` | Allows opening the advanced menu using `/sanction`. |
| `axionstaff.sanctions.list`| `op` | Allows viewing active bans using `/sanction list`. |
| `axionstaff.punish.silent` | `op` | Allows executing silent punishments via `/silent`. |
| `axionstaff.punish.override`| `op` | Allows bypassing protection checks and sanctioning other Staff members. |
| `axionstaff.punish.protected`| `false` | Protects the player from being sanctioned by lower-ranking Staff members. |
| `axionstaff.vanish` | `op` | Allows the use of the `/vanish` command. |
| `axionstaff.see.vanish` | `op` | Allows viewing other Staff members who are in Vanish mode. |
| `axionstaff.vanish.join` | `false` | Automatically enables Vanish mode upon joining the server. |
| `axionstaff.staffchat` | `op` | Allows reading and writing in the Staff chat. |
| `axionstaff.cmdspy` | `op` | Allows using `/cmdspy` to audit user commands. |
| `axionstaff.cmdspy.raw` | `false` | (Critical) Allows viewing sensitive arguments and uncensored passwords in `/cmdspy` logs. |
| `axionstaff.cmdspy.raw.owners`| `op` | Grouping node intended exclusively for owners, includes access to raw cmdspy. |
| `axionstaff.chat.admin` | `op` | Allows muting and clearing the global chat. |
| `axionstaff.gamemode` | `op` | Allows using the `/gm` menu. |
| `axionstaff.alts` | `op` | Allows the use of the alternate account tracking system. |
| `axionstaff.alts.override` | `op` | Allows viewing alternate accounts of hierarchically protected players. |
| `axionstaff.alts.protected`| `false` | Hides the player's alternate accounts from lower-ranking Staff scrutiny. |
| `axionstaff.freeze` | `op` | Allows the use of `/freeze` and `/unfreeze`. |
| `axionstaff.report` | `true` | Allows players to execute `/report` (Granted to all by default). |
| `axionstaff.report.notify` | `op` | Allows Staff to receive real-time notifications about new reports. |
| `axionstaff.report.bypass` | `op` | Allows bypassing cooldowns when sending reports. |
| `axionstaff.invsee` | `op` | Allows the use of `/invsee`. |
| `axionstaff.revive` | `op` | Allows the use of `/revive` to restore inventories lost upon death. |
| `axionstaff.fly` | `op` | Allows the use of `/fly` and modification of its speed. |
| `axionstaff.client.notify` | `op` | Allows receiving notifications when modified client usage is detected. |
| `axionstaff.antixray.alert`| `op` | Allows receiving real-time Anti-Xray system notifications. |
| `axionstaff.antixray.bypass`| `false` | Prevents the Anti-Xray system from generating mining alerts for this player. |

---

## 6. PlaceholderAPI Variables

If PlaceholderAPI is installed on the server, AxionStaff exposes the following variables for integration into scoreboards, chats, and other compatible plugins.

* `%axionstaff_in_staff_mode%` - Returns the current Staff Mode state (true/false).
* `%axionstaff_vanished%` - Returns the current Vanish mode state (true/false).
* `%axionstaff_frozen%` - Returns whether the player is currently frozen (true/false).
* `%axionstaff_is_spy%` - Returns whether the player has CommandSpy enabled (true/false).
* `%axionstaff_warn_count%` - Returns the total number of warnings issued to the player.
* `%axionstaff_ban_count%` - Returns the total number of bans issued to the player.
* `%axionstaff_mute_count%` - Returns the total number of mutes issued to the player.
* `%axionstaff_kick_count%` - Returns the total number of kicks issued to the player.
* `%axionstaff_total_punishments%` - Returns the total sum of punishments applied to the player.
* `%axionstaff_playtime%` - Returns the player's formatted playtime (e.g., `12h 30m`).

*(Note: Text output for boolean variables can be configured in the corresponding section of `config.yml`).*

---

## 7. Modules and Systems Details

### Visual Freeze System (TextDisplay)
Unlike conventional freeze systems, the `/freeze` command summons a text entity (TextDisplay) directly into the target user's field of view. This blocks all command entry, camera movement, and physical interaction.
* If the offender attempts to disconnect while the system is active, the plugin will execute an **Automatic Security Ban** (set to 7 days duration by default) for evasion.

### Privacy and Security (CommandSpy)
AxionStaff actively protects confidential information. When CommandSpy is enabled, sensitive commands defined in `config.yml` (such as AuthMe passwords or authentication tokens) are censored using a mask (e.g., `******`). This allows the moderation team to audit command usage without compromising user credentials. Only high-ranking staff with the `axionstaff.cmdspy.raw` permission can view the original arguments.

### Compact Inventory Inspection (InvSee)
The InvSee command generates a unified interface that renders the primary inventory, off-hand, and armor pieces simultaneously. This compact layout eliminates the need for secondary windows and facilitates quick ScreenShares.

### Restoration System (Death Revive)
The plugin stores the exact inventory state and experience every time a player dies. If a death occurs due to a technical server failure or unauthorized third-party intervention, the `/revive` command opens an interactive panel allowing a complete rollback of the lost inventory with a single click.

---

## 8. Discord Configuration Guide

To configure `discord.yml` and connect AxionStaff to your Discord server, follow these steps to set up Webhooks:

1. **Open your Discord Server Settings**
   Go to **Server Settings** > **Integrations** > **Webhooks**.

2. **Create a New Webhook**
   Click on **New Webhook**. Name it (e.g., "AxionStaff") and select the channel where you want the notifications to appear (like `#staff-chat` or `#punishments`).

3. **Copy the Webhook URL**
   Click the **Copy Webhook URL** button.

4. **Paste it into `discord.yml`**
   Open `plugins/AxionStaff/discord.yml` on your Minecraft server.
   Locate the module you want to enable (for example, `sanctions` or `staff-chat`) and paste the URL in the `webhook-url` field:
   ```yaml
   sanctions:
     enabled: true
     webhook-url: "https://discord.com/api/webhooks/your_webhook_url_here"
   ```

5. **Reload the Plugin**
   Save the file and run `/axionstaff reload` in your game or server console. Your Discord integration is now active!