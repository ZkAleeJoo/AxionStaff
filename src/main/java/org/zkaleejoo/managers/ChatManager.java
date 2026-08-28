package org.zkaleejoo.managers;

import org.bukkit.command.CommandSender;
import org.zkaleejoo.AxionStaff;
import org.zkaleejoo.utils.MessageUtils;

public class ChatManager {
    private final AxionStaff plugin;
    private boolean globalMute = false;

    public ChatManager(AxionStaff plugin) {
        this.plugin = plugin;
    }

    public boolean isGlobalMute() {
        return globalMute;
    }

    public void setGlobalMute(boolean status) {
        this.globalMute = status;
    }

    public void clearChat(CommandSender sender) {
        for (int i = 0; i < 100; i++) {
            MessageUtils.broadcastToPlayersOnly(" ");
        }
        
        String msg = plugin.getMainConfigManager().getMsgChatCleared()
                .replace("{player}", sender.getName());
        MessageUtils.broadcastToPlayersOnly(msg);
    }
}
