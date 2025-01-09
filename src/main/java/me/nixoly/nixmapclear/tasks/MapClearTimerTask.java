package me.nixoly.nixmapclear.tasks;

import me.nixoly.nixmapclear.managers.MapClearManager;
import org.bukkit.scheduler.BukkitRunnable;

public class MapClearTimerTask extends BukkitRunnable {

    private int clearInterval;
    private int remainingTime;

    public MapClearTimerTask(int interval) {
        this.clearInterval = Math.max(interval, 1);
        this.remainingTime = this.clearInterval;
    }

    @Override
    public void run() {
        if (remainingTime <= 0) {
            performMapClear();
            remainingTime = clearInterval;
        } else {
            remainingTime--;
        }
    }

    public void updateClearInterval(int newInterval) {
        this.clearInterval = Math.max(newInterval, 1);
        this.remainingTime = this.clearInterval; // Reset timer with new interval
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    private void performMapClear() {
        MapClearManager.clearMap();
    }
}
