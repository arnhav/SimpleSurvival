package com.github.arnhav.simplesurvival.util;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class CommandExecutor implements org.bukkit.command.CommandExecutor {

    public CommandExecutor(String command, JavaPlugin plugin) {
        PluginCommand c = plugin.getCommand(command);
        if (c == null) return;
        c.setExecutor(this);
    }

}
