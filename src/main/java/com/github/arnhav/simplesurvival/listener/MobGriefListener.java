package com.github.arnhav.simplesurvival.listener;

import com.github.arnhav.simplesurvival.util.Listener;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class MobGriefListener extends Listener {

    public MobGriefListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onCreeperBlowUp(EntityExplodeEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Creeper)) return;
        event.blockList().clear();
    }

    @EventHandler
    public void onEndermanPickupBlock(EntityChangeBlockEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Enderman)) return;
        event.setCancelled(true);
    }

}
