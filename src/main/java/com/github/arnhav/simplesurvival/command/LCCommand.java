package com.github.arnhav.simplesurvival.command;

import com.github.arnhav.simplesurvival.util.CommandExecutor;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class LCCommand extends CommandExecutor {

    public LCCommand(String command, JavaPlugin plugin) {
        super(command, plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.isOp()) return true;

        for (World w : Bukkit.getWorlds()) {
            for (Chunk c : w.getForceLoadedChunks()) {
                sender.sendMessage("[" + c.getX() + ", " + c.getZ() + "]");
                sender.sendMessage(" - Entities: " + c.isEntitiesLoaded());
                sender.sendMessage(" - Loaded: " + c.isLoaded());
                sender.sendMessage(" - Force: " + c.isForceLoaded());
                sender.sendMessage(String.join(",", plugins(c)));
            }
        }

        return true;
    }

    private List<String> plugins(Chunk c) {
        List<String> list = new ArrayList<>();
        c.getPluginChunkTickets().forEach(p -> list.add(p.getName()));
        return list;
    }
}
