package com.github.arnhav.simplesurvival.listener;

import com.destroystokyo.paper.MaterialSetTag;
import com.github.arnhav.simplesurvival.util.Listener;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SignListener extends Listener {

    public SignListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onRightClickSign(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (block == null) return;
        if (!MaterialSetTag.SIGNS.isTagged(block.getType())) return;
        Player player = event.getPlayer();
        if (!player.isSneaking()) return;
        player.openSign((Sign) block.getState());
    }
}
