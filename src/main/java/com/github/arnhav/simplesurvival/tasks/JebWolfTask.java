package com.github.arnhav.simplesurvival.tasks;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.World;
import org.bukkit.entity.Wolf;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JebWolfTask implements Runnable {

    private final List<DyeColor> colors = new ArrayList<>();

    public JebWolfTask(JavaPlugin plugin) {
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 20);
        colors.addAll(Arrays.asList(DyeColor.values()));
    }

    @Override
    public void run() {
        for (World world : Bukkit.getWorlds()) {
            for (Wolf wolf : world.getEntitiesByClass(Wolf.class)) {
                if (wolf.getOwner() == null) continue;
                Component customName = wolf.customName();
                if (customName == null) continue;
                String name = PlainTextComponentSerializer.plainText().serialize(customName);
                if (!name.equalsIgnoreCase("jeb_")) continue;
                int index = colors.indexOf(wolf.getCollarColor()) + 1;
                if (index == colors.size()) index = 0;
                wolf.setCollarColor(colors.get(index));
            }
        }
    }
}
