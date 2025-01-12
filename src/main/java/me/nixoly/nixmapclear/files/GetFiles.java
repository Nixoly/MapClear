
package me.nixoly.nixmapclear.files;

import me.nixoly.nixmapclear.MapClear;
import me.nixoly.nixmapclear.tasks.MapClearTimerTask;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class GetFiles {

    private static FileConfiguration config;
    private static FileConfiguration messages;

    public static void initialize(MapClear plugin) {
        plugin.saveDefaultConfig();
        config = plugin.getConfig();

        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public static FileConfiguration getConfig() {
        return config;
    }

    public static FileConfiguration getMessages() {
        return messages;
    }

    public static void reloadFiles(MapClear plugin, MapClearTimerTask timerTask) {
        plugin.reloadConfig();
        config = plugin.getConfig();

        File messagesFile = new File(plugin.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            plugin.saveResource("messages.yml", false);
        }
        messages = YamlConfiguration.loadConfiguration(messagesFile);

        int newInterval = config.getInt("clear_interval", 9000);
        timerTask.updateClearInterval(newInterval);
    }
}