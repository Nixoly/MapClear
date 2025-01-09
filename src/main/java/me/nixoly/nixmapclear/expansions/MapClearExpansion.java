package me.nixoly.nixmapclear.expansions;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.nixoly.nixmapclear.MapClear;
import me.nixoly.nixmapclear.tasks.MapClearTimerTask;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.entity.Player;

public class MapClearExpansion extends PlaceholderExpansion {

    @Override
    public String getIdentifier() {
        return "mapclear";
    }

    @Override
    public String getAuthor() {
        return "Nixoly";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        if ("remaining".equalsIgnoreCase(identifier)) {
            MapClearTimerTask timerTask = MapClear.getInstance().getTimerTask();
            if (timerTask != null) {
                int remainingTime = timerTask.getRemainingTime();
                return DurationFormatUtils.formatDuration(remainingTime * 1000L, "HH:mm:ss", true);
            }
        }
        return "";
    }
}
