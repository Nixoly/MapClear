package me.nixoly.nixmapclear.commands;

import me.nixoly.nixmapclear.MapClear;
import me.nixoly.nixmapclear.files.GetFiles;
import me.nixoly.nixmapclear.managers.MapClearManager;
import me.nixoly.nixmapclear.tasks.MapClearTimerTask;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class MapClearCommand implements CommandExecutor {

    private final MapClearTimerTask timerTask;

    public MapClearCommand(MapClearTimerTask timerTask) {
        this.timerTask = timerTask;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            showHelp(sender);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "clear":
                if (!sender.hasPermission("mapclear.clear")) {
                    sender.sendMessage(translate("no_permission"));
                    return true;
                }
                MapClearManager.clearMap();
                return true;

            case "bypass":
                if (!(sender instanceof Player)) {
                    sender.sendMessage(translate("player_only"));
                    return true;
                }

                Player player = (Player) sender;
                if (!player.hasPermission("mapclear.bypass")) {
                    sender.sendMessage(translate("no_permission"));
                    return true;
                }

                toggleBypassForPlayer(player);
                return true;

            case "reload":
                if (!sender.hasPermission("mapclear.reload")) {
                    sender.sendMessage(translate("no_permission"));
                    return true;
                }

                GetFiles.reloadFiles(MapClear.getInstance(), timerTask);
                MapClearManager.initialize(MapClear.getInstance());
                sender.sendMessage(translate("plugin_reloaded"));
                return true;

            case "help":
            case "":
                showHelp(sender);
                return true;

            default:
                sender.sendMessage(translate("invalid_subcommand"));
                return true;
        }
    }

    public void setBypassForPlayer(Player player, boolean bypass) {
        MapClearManager.setBypassing(player, bypass);
        String messageKey = bypass ? "bypass_enabled" : "bypass_disabled";
        player.sendMessage(translate(messageKey));
    }

    public void toggleBypassForPlayer(Player player) {
        boolean isBypassing = MapClearManager.toggleBypass(player);
        player.sendMessage(translate(isBypassing ? "bypass_enabled" : "bypass_disabled"));
    }

    private void showHelp(CommandSender sender) {
        for (String line : getMessages().getStringList("help_message")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', line));
        }
    }

    private String translate(String key) {
        return ChatColor.translateAlternateColorCodes('&', getMessages().getString(key, "&cMessage not found: " + key));
    }

    private FileConfiguration getMessages() {
        return GetFiles.getMessages();
    }

    private FileConfiguration getConfig() {
        return GetFiles.getConfig();
    }
}
