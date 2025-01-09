package me.nixoly.nixmapclear.managers;

import me.nixoly.nixmapclear.files.GetFiles;
import me.nixoly.nixmapclear.listeners.BlockEventListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class MapClearManager {

    private static final Set<UUID> bypassPlayers = new HashSet<>();
    private static String mapClearedMessage;

    public static void initialize(JavaPlugin plugin) {
        FileConfiguration messagesConfig = GetFiles.getMessages();
        mapClearedMessage = ChatColor.translateAlternateColorCodes('&',
                messagesConfig.getString("map_cleared", "&aThe map has been successfully cleared!"));
    }

    public static void clearMap() {
        BlockEventListener.clearTrackedBlocks();

        if (mapClearedMessage != null && !mapClearedMessage.isEmpty()) {
            Bukkit.broadcastMessage(mapClearedMessage);
        }
    }

    public static boolean toggleBypass(Player player) {
        UUID uuid = player.getUniqueId();
        if (bypassPlayers.contains(uuid)) {
            bypassPlayers.remove(uuid);
            return false;
        } else {
            bypassPlayers.add(uuid);
            return true;
        }
    }

    public static boolean isBypassing(Player player) {
        return bypassPlayers.contains(player.getUniqueId());
    }

    public static void setBypassing(Player player, boolean bypass) {
        UUID uuid = player.getUniqueId();
        if (bypass) {
            bypassPlayers.add(uuid);
        } else {
            bypassPlayers.remove(uuid);
        }
    }
}
