package org.zkaleejoo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zkaleejoo.AxionStaff;

public class VanishCommand implements CommandExecutor {

    private final AxionStaff plugin;

    public VanishCommand(AxionStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = CommandContextUtil.requirePlayer(sender, plugin.getMainConfigManager());
        if (player == null) {
            return true;
        }

        if (!CommandContextUtil.requirePermission(player, "axionstaff.vanish", plugin.getMainConfigManager())) {
            return true;
        }

        plugin.getStaffManager().toggleVanish(player);
        return true;
    }
}
