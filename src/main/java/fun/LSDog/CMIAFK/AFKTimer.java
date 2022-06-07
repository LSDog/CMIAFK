package fun.LSDog.CMIAFK;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * 这是个1秒间隔的全局定时器
 * 总而言之这玩意的误差只在每个玩家第一次间隔
 */
public class AFKTimer {

    public static final long DURING = 1000;
    private static BukkitTask timer;

    public static void startTimer() {
        timer = new BukkitRunnable() {
            @Override
            public void run() {
                long now = CMIAFKUtils.now();
                long secStamp = (now / 1000L) * 1000L;
                AFKPlayer.getAllAFKPlayer().forEach(afkPlayer -> {
                    if (!afkPlayer.isAvailable()) return;
                    if (!afkPlayer.getPlayer().isOnline()) {
                        if (afkPlayer.isAvailable()) {
                            afkPlayer.leaveAfk();
                        }
                        return;
                    }
                    afkPlayer.updateHologramLocation();
                    if (now - afkPlayer.getIntervalStartStamp() >= afkPlayer.getInfo().getInterval()) {
                        afkPlayer.setIntervalStartStamp(secStamp);
                        afkPlayer.executeAction();
                    }
                });
            }
        }.runTaskTimerAsynchronously(CMIAFK.instant, 0, DURING);
    }

    public static void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
