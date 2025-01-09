package me.nixoly.nixmapclear.listeners;

import me.nixoly.nixmapclear.files.GetFiles;
import me.nixoly.nixmapclear.managers.MapClearManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BlockEventListener implements Listener {

    private static final Set<Location> trackedBlocks = new HashSet<>();
    private final List<String> disabledWorlds;

    public BlockEventListener() {
        FileConfiguration config = GetFiles.getConfig();
        this.disabledWorlds = config.getStringList("disabled_worlds");
    }

    public static void trackBlock(Location location) {
        trackedBlocks.add(location);
    }

    public static void untrackBlock(Location location) {
        trackedBlocks.remove(location);
    }

    public static boolean isTracked(Location location) {
        return trackedBlocks.contains(location);
    }

    public static void clearTrackedBlocks() {
        trackedBlocks.removeIf(location -> {
            Material blockType = location.getBlock().getType();
            if (blockType.name().endsWith("_SHULKER_BOX") || blockType == Material.SHULKER_BOX) {
                return false;
            }
            location.getBlock().setType(Material.AIR);
            return true;
        });
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();

        if (disabledWorlds.contains(player.getWorld().getName())) {
            return;
        }

        if (MapClearManager.isBypassing(player)) {
            return;
        }

        if (!event.getBlock().getType().name().endsWith("_SHULKER_BOX") && event.getBlock().getType() != Material.SHULKER_BOX) {
            trackBlock(event.getBlock().getLocation());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Location location = event.getBlock().getLocation();

        if (disabledWorlds.contains(location.getWorld().getName())) {
            return;
        }

        if (isTracked(location)) {
            untrackBlock(location);
        }
    }
}
