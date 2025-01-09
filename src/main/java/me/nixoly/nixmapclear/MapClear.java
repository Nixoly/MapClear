package me.nixoly.nixmapclear;

import me.nixoly.nixmapclear.managers.MapClearManager;
import me.nixoly.nixmapclear.listeners.JoinListener;
import me.nixoly.nixmapclear.commands.MapClearCommand;
import me.nixoly.nixmapclear.commands.MapClearTabCompleter;
import me.nixoly.nixmapclear.expansions.MapClearExpansion;
import me.nixoly.nixmapclear.listeners.BlockEventListener;
import me.nixoly.nixmapclear.tasks.MapClearTimerTask;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import me.nixoly.nixmapclear.files.GetFiles;

import java.io.File;

public class MapClear extends JavaPlugin {

    private static MapClear instance;
    private MapClearTimerTask timerTask;
    private FileConfiguration messagesConfig;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();

        GetFiles.initialize(this);

        int defaultInterval = GetFiles.getConfig().getInt("clear_interval", 9000);
        timerTask = new MapClearTimerTask(defaultInterval);
        timerTask.runTaskTimer(this, 0L, 20L);

        MapClearManager.initialize(this);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new MapClearExpansion().register();
        }

        // MapClearCommand oluşturuluyor
        MapClearCommand mapClearCommand = new MapClearCommand(timerTask);

        if (getCommand("mapclear") != null) {
            getCommand("mapclear").setExecutor(mapClearCommand);
            getCommand("mapclear").setTabCompleter(new MapClearTabCompleter());
        } else {
            getLogger().warning("Command 'mapclear' not found. Ensure it is defined in plugin.yml.");
        }

        // BlockEventListener ve JoinListener kayıt ediliyor
        Bukkit.getPluginManager().registerEvents(new BlockEventListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinListener(mapClearCommand), this);

        getLogger().info("MapClear plugin has been successfully enabled!");
    }

    @Override
    public void onDisable() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        getLogger().info("MapClear plugin has been successfully disabled!");
    }

    public static MapClear getInstance() {
        return instance;
    }

    public MapClearTimerTask getTimerTask() {
        return timerTask;
    }

    private void saveMessagesFile() {
        File dataFolder = getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            getLogger().warning("Could not create plugin data folder!");
        }

        File messagesFile = new File(dataFolder, "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
    }

    private void loadMessagesConfig() {
        File messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false);
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getMessagesConfig() {
        if (messagesConfig == null) {
            loadMessagesConfig();
        }
        return messagesConfig;
    }

    public FileConfiguration getMainConfig() {
        if (config == null) {
            reloadConfig();
            config = getConfig();
        }
        return config;
    }

    public void reloadMessagesConfig() {
        loadMessagesConfig();
        getLogger().info("Messages configuration reloaded.");
    }

    @Override
    public void reloadConfig() {
        super.reloadConfig();
        config = getConfig();
        getLogger().info("Main configuration reloaded.");
    }
}
