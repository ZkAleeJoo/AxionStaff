package org.zkaleejoo.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.zkaleejoo.AxionStaff;

public class XrayCommand implements CommandExecutor {

    private final AxionStaff plugin;

    public XrayCommand(AxionStaff plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,
            String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMainConfigManager().getMsgConsole());
            return true;
        }

        if (!CommandContextUtil.requirePermission(player, "AxionStaff.antixray.alert", plugin.getMainConfigManager())) {
            return true;
        }

        if (!plugin.getMainConfigManager().isAntiXrayEnabled() || plugin.getAntiXrayListener() == null) {
            player.sendMessage(org.zkaleejoo.utils.MessageUtils.getColoredMessage(
                    plugin.getMainConfigManager().getPrefix() + plugin.getMainConfigManager().getAntiXrayMenuDisabledMessage()));
            return true;
        }

        plugin.getGuiManager().openXrayMenu(player);
        return true;
    }
}

