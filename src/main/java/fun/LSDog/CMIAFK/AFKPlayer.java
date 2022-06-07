package fun.LSDog.CMIAFK;

import com.Zrips.CMI.CMI;
import com.Zrips.CMI.Modules.Holograms.CMIHologram;
import net.Zrips.CMILib.Container.CMILocation;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AFKPlayer {

    private static final Map<UUID, AFKPlayer> afkMap = new ConcurrentHashMap<>();

    public static AFKPlayer getAFKPlayer(Player player) {
        return afkMap.get(player.getUniqueId());
    }

    public static Collection<AFKPlayer> getAllAFKPlayer() {
        return afkMap.values();
    }

    public static void clearAll() {
        new ArrayList<>(afkMap.values()).forEach(AFKPlayer::leaveAfk);
        afkMap.clear(); // ←其实屁用没有上面那行给删完了
    }

///////////////////////////////////////////////////////////////////////////

    private final Player player;
    private final Location location;
    private CMIHologram hologram;
    private final AFKInfo info;
    private final long startStamp;
    private long intervalStartStamp;

    private boolean available = false;

    public AFKPlayer(Player player) {
        this.player = player;
        this.location = player.getLocation();
        this.info = AFKInfo.getAfkInfo(player);
        this.startStamp = CMIAFKUtils.now();
        this.intervalStartStamp = CMIAFKUtils.nextSec(startStamp);
    }

    public Player getPlayer() {
        return player;
    }

    public AFKInfo getInfo() {
        return info;
    }

    public long getStartStamp() {
        return startStamp;
    }

    public long getIntervalStartStamp() {
        return intervalStartStamp;
    }

    public void setIntervalStartStamp(long intervalStartStamp) {
        this.intervalStartStamp = intervalStartStamp;
    }

    public boolean isAvailable() {
        return available;
    }

    public void startAFK() {
        available = true;
        afkMap.put(player.getUniqueId(), this);
        if (CMIAFK.config.tag.enable) {
            showHologram();
        }
    }

    public void executeAction() {
        info.executeAction(player);
    }

    private void showHologram() {
        this.hologram = new CMIHologram(
                "CMIAFK_"+player.getName(),
                new CMILocation(location.add(0, CMIAFK.config.tag.height, 0))
        );
        hologram.setLines(CMIAFK.config.tag.lines);
        CMI.getInstance().getHologramManager().addHologram(hologram);
        hologram.enable();
    }

    public void updateHologramLocation() {
        hologram.setLoc(player.getLocation().add(0, CMIAFK.config.tag.height, 0));
    }

    private void removeHologram() {
        if (this.hologram != null) {
            hologram.disable();
            hologram.remove();
        }
    }

    public void leaveAfk() {
        available = false;
        afkMap.remove(player.getUniqueId());
        removeHologram();
    }

}
