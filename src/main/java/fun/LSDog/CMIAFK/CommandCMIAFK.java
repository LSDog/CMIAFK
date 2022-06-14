package fun.LSDog.CMIAFK;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class CommandCMIAFK implements TabExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {

        if (args.length == 0) {
            sender.sendMessage(CMIAFK.config.prefix + "§8v" + CMIAFK.instance.getDescription().getVersion() + "§r\n" +
                    (sender.hasPermission("CMIAFK.admin") ?
                            "§b/cafk§r§3 list§l §r§7- 列出afk中的玩家\n" +
                            "§b/cafk§r§3 reload§l §r§7- 重载配置和插件, afk中的玩家会重新开始计算\n":"")
            );


        } else if (args.length == 1) {
            switch (args[0].toLowerCase(Locale.ENGLISH)) {
                case "list":
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            StringBuilder sb = new StringBuilder();
                            sb.append(CMIAFK.config.prefix).append("\n§7==== AFK player list ====§r\n");
                            if (sender instanceof Player) {
                                AFKPlayer.getAllAFKPlayer().forEach(afkPlayer -> sb
                                        .append("§b")
                                        .append(afkPlayer.getPlayer().getName())
                                        .append("    §3")
                                        .append(CMIAFKUtils.getHms(CMIAFKUtils.now() - afkPlayer.getStartStamp()))
                                        .append("    §7")
                                        .append(afkPlayer.getInfo().getName())
                                        .append("\n"));
                            } else {
                                AFKPlayer.getAllAFKPlayer().forEach(afkPlayer -> sb
                                        .append(afkPlayer.getPlayer().getName())
                                        .append("    \t")
                                        .append(CMIAFKUtils.getHms(CMIAFKUtils.now() - afkPlayer.getStartStamp()))
                                        .append("    \t")
                                        .append(afkPlayer.getInfo().getName())
                                        .append("\n"));
                            }
                            sb.append("§7==== AFK player list ====§r\n");
                            sender.sendMessage(sb.toString());
                        }
                    }.runTaskAsynchronously(CMIAFK.instance);
                    break;
                case "reload":
                    CMIAFK.reload();
                    sender.sendMessage(CMIAFK.config.prefix + "§7reload OK!");
                    break;
            }


        } else {
            sender.sendMessage(CMIAFK.config.prefix + "§7指令参数长度错误, 使用 §b/cafk §7查看帮助");


        }
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, String[] args) {
        if (args.length == 1) {
            if (sender.hasPermission("CMIAFK.admin")) {
                return Arrays.asList("list", "reload");
            }
        }
        return null;
    }
}
