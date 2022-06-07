package fun.LSDog.CMIAFK;

import com.Zrips.CMI.events.CMIAfkEnterEvent;
import com.Zrips.CMI.events.CMIAfkKickEvent;
import com.Zrips.CMI.events.CMIAfkLeaveEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventAFK implements Listener {


    @EventHandler
    public static void afkEnterEvent(CMIAfkEnterEvent e) {
        if (e.isCancelled()) return;
        new AFKPlayer(e.getPlayer()).startAFK();
    }

    @EventHandler
    public static void afkLeaveEvent(CMIAfkLeaveEvent e) {
        AFKPlayer afkPlayer = AFKPlayer.getAFKPlayer(e.getPlayer());
        if (afkPlayer != null) afkPlayer.leaveAfk();
    }

    @EventHandler
    public static void afkKickEvent(CMIAfkKickEvent e) {
        AFKPlayer afkPlayer = AFKPlayer.getAFKPlayer(e.getPlayer());
        if (afkPlayer != null) afkPlayer.leaveAfk();
    }

    @EventHandler
    public static void quitEvent(PlayerQuitEvent e) {
        AFKPlayer afkPlayer = AFKPlayer.getAFKPlayer(e.getPlayer());
        if (afkPlayer != null) afkPlayer.leaveAfk();
    }


}
