package com.github.arnhav.simplesurvival.util;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class Listener implements org.bukkit.event.Listener {

    public Listener(JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

}
