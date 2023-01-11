package com.github.arnhav.simplesurvival.tasks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class TabListTask implements Runnable {

    public TabListTask(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 10);
    }

    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendPlayerListHeader(
                    Component.text("We Aight", NamedTextColor.DARK_AQUA)
            );
            p.sendPlayerListFooter(
                    Component.text("■ ", NamedTextColor.GREEN).append(Component.text(p.getPing() + "ms"))
            );
        }
    }
}
