package org.zkaleejoo.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.zkaleejoo.AxionStaff;

import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;

public class PlayerDeathListener implements Listener {

    private final AxionStaff plugin;

    public PlayerDeathListener(AxionStaff plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String cause = resolveDeathCause(event, player);
        plugin.getInventorySnapshotManager().saveDeathSnapshot(player, cause);
    }

    private String resolveDeathCause(PlayerDeathEvent event, Player player) {
        net.kyori.adventure.text.Component deathMessage = event.deathMessage();
        if (deathMessage != null) {
            String plainMessage = PlainTextComponentSerializer.plainText().serialize(deathMessage);
            if (!plainMessage.isBlank()) {
                return plainMessage;
            }
        }

        if (player.getLastDamageCause() != null) {
            return player.getLastDamageCause().getCause().name();
        }

        return "Unknown";
    }
}
