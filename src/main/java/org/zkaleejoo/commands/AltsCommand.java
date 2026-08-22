package org.zkaleejoo.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zkaleejoo.AxionStaff;
import org.zkaleejoo.utils.MessageUtils;

public class AltsCommand implements CommandExecutor {
    private final AxionStaff plugin;

    public AltsCommand(AxionStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = CommandContextUtil.requirePlayer(sender, plugin.getMainConfigManager());
        if (player == null) {
            return true;
        }

        if (!CommandContextUtil.requirePermission(player, "axionstaff.alts", plugin.getMainConfigManager())) {
            return true;
        }

        if (args.length < 1) {
            player.sendMessage(MessageUtils.getColoredMessage(
                    plugin.getMainConfigManager().getPrefix() + plugin.getMainConfigManager().getAltsUse()));
            return true;
        }

        String targetName = args[0];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target != null && target.hasPermission("axionstaff.alts.protected")
                && !player.hasPermission("axionstaff.alts.override")) {
            player.sendMessage(MessageUtils.getColoredMessage(plugin.getMainConfigManager().getPrefix()
                    + plugin.getMainConfigManager().getAltsProtectedMessage().replace("{target}", target.getName())));
            return true;
        }

        plugin.getGuiManager().openAltsMenu(player, targetName);
        return true;
    }
}
