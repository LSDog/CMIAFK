package fun.LSDog.CMIAFK;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class CMIAFK extends JavaPlugin {

    public CMIAFK() { instance = this;}
    public static CMIAFK instance;

    public static class config {
        public static String prefix = "§3§lCMIAFK >>§r ";
        public static boolean debug = false;
        public static class tag {
            public static boolean enable = true;
            public static List<String> lines = Collections.singletonList("&7&l[&7&l AFK &7&l]");
            public static double height = 3.25;
        }

    }

    public File fileConfig = new File(getDataFolder() + File.separator + "config.yml");

    @Override
    public void onEnable() {

        if (!fileConfig.exists()) {
            saveDefaultConfig();
        }

        try {
            getConfig().load(fileConfig);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }

        loadFromConfig();

        Objects.requireNonNull(getCommand("cmiafk")).setExecutor(new CommandCMIAFK());

        Bukkit.getPluginManager().registerEvents(new EventAFK(), this);

        AFKTimer.startTimer();

        log("§eCMIAFK§r Enabled! plugin by §b§lLSDog§r.");
    }

    @Override
    public void onDisable() {
        AFKTimer.stopTimer();
        AFKPlayer.clearAll();
        log("CMIAFK disabled.");
    }


    private void loadFromConfig() {
        loadAFKInfos();

        config.debug = getConfig().getBoolean("debug");
        config.prefix = getConfig().getString("prefix");
        config.tag.enable = getConfig().getBoolean("tag.enable");
        if (config.tag.enable) {
            config.tag.lines = getConfig().getStringList("tag.lines");
            config.tag.height = getConfig().getDouble("tag.height");
        }
    }

    public static void reload() {
        Collection<AFKPlayer> afkPlayers = AFKPlayer.getAllAFKPlayer();
        List<Player> afkingPlayerList = new ArrayList<>();
        afkPlayers.forEach(afkPlayer -> afkingPlayerList.add(afkPlayer.getPlayer()));
        AFKPlayer.clearAll();
        instance.reloadConfig();
        instance.loadFromConfig();
        afkingPlayerList.forEach(player -> new AFKPlayer(player).startAFK());
    }

    public static void loadAFKInfos() {
        LinkedHashMap<String, AFKInfo> afkInfoMap = new LinkedHashMap<>();
        Objects.requireNonNull(instance.getConfig().getConfigurationSection("afk")).getValues(false).forEach(
                (key, object) -> {
                    if (object instanceof ConfigurationSection) {
                        ConfigurationSection section = (ConfigurationSection) object;
                        String permission = section.getString("permission");
                        AFKInfo afkInfo = new AFKInfo (
                                section.getString("name"),
                                permission,
                                section.getInt("interval"),
                                section.getStringList("action"));
                        if (permission != null) {
                            afkInfoMap.put(permission, afkInfo);
                        }
                        if (key.equals("default")) {
                            AFKInfo.setDefaultAFKInfo(afkInfo);
                        }
                    }
                }
        );
        AFKInfo.setAfkInfoMap(afkInfoMap);
    }

    public static void log(Object object) {
        Bukkit.getConsoleSender().sendMessage("[CMIAFK] "+object);
    }

    public static void debug(Object object) {
        if (config.debug) Bukkit.getConsoleSender().sendMessage("[CMIAFK]§c[DEBUG]§r "+object);
    }
}
