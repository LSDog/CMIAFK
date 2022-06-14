package fun.LSDog.CMIAFK;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AFKInfo {

    private static final Random RANDOM = new Random();

    private static AFKInfo defaultAFKInfo;
    private static LinkedHashMap<String, AFKInfo> afkInfoMap;

    public static AFKInfo getAfkInfo(Player player) {
        for (Map.Entry<String, AFKInfo> entry : afkInfoMap.entrySet()) {
            if (player.hasPermission(entry.getKey())) {
                return entry.getValue();
            }
        }
        return defaultAFKInfo;
    }

    public static void setAfkInfoMap(LinkedHashMap<String, AFKInfo> afkInfoMap) {
        AFKInfo.afkInfoMap = afkInfoMap;
        CMIAFK.debug(afkInfoMap.values());
    }

    public static void setDefaultAFKInfo(AFKInfo defaultAFKInfo) {
        AFKInfo.defaultAFKInfo = defaultAFKInfo;
    }

    //////////////////////////////////////////////////////////////////////////////////////////

    private final String name;
    private final String permission;
    private final int interval;
    private final List<String> action;

    public AFKInfo(String name, String permission, int interval, List<String> action) {
        this.name = name;
        this.permission = permission;
        this.interval = interval;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public int getInterval() {
        return interval;
    }

    public void executeAction(Player player) {
        if (action == null || action.isEmpty()) return;
        String stringAction = action.get(RANDOM.nextInt(action.size()));
        stringAction = PlaceholderAPI.setPlaceholders(player, stringAction);
        String[] part = stringAction.split(":");
        CMIAFK.debug("Execute command for §l" + player.getName() + "§r\n\t" +
                "'" + stringAction + "'"
        );
        if (part.length < 2) return;
        switch (part[0].toLowerCase()) {
            case "console":
                Bukkit.getScheduler().callSyncMethod(CMIAFK.instance, () ->
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), part[1]));
                break;
            case "player":
                Bukkit.getScheduler().callSyncMethod(CMIAFK.instance, () ->
                        player.performCommand(part[1]));
                break;
            case "op":
                boolean hasOP = player.isOp();
                try {
                    if (!hasOP) player.setOp(true);
                    Bukkit.getScheduler().callSyncMethod(CMIAFK.instance, () ->
                            Bukkit.dispatchCommand(player, part[1]));
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (!hasOP) player.setOp(false);
                }
                break;
        }
    }

    @Override
    public String toString() {
        return "AFKInfo{" +
                "name='" + name + '\'' +
                ", permission='" + permission + '\'' +
                ", interval=" + interval +
                ", action=" + action +
                '}';
    }
}
