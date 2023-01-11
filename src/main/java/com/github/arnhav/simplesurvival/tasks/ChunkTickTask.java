package com.github.arnhav.simplesurvival.tasks;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class ChunkTickTask implements Runnable {

    private final JavaPlugin plugin;

    public ChunkTickTask(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 5 * 20);
    }

    @Override
    public void run() {
        for (World w : Bukkit.getWorlds()) {
            for (Chunk c : w.getForceLoadedChunks()) {
                if (c.getPluginChunkTickets().contains(plugin)) continue;
                c.addPluginChunkTicket(plugin);
            }
        }
    }
}
