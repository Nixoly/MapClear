package me.nixoly.nixmapclear.listeners;

import me.nixoly.nixmapclear.MapClear;
import me.nixoly.nixmapclear.commands.MapClearCommand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {

    private final MapClearCommand mapClearCommand;

    public JoinListener(MapClearCommand mapClearCommand) {
        this.mapClearCommand = mapClearCommand;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        boolean bypassAutoEnabled = MapClear.getInstance().getMainConfig().getBoolean("bypass_onjoin_enabled", true);

        if (bypassAutoEnabled && player.hasPermission("mapclear.bypass.onjoin")) {
            mapClearCommand.setBypassForPlayer(player, true);
        }
    }
}
