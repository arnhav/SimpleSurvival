package com.github.arnhav.simplesurvival.listener;

import com.destroystokyo.paper.MaterialSetTag;
import com.github.arnhav.simplesurvival.util.Listener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;

public class FarmingListener extends Listener {

    private final JavaPlugin plugin;

    public FarmingListener(JavaPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (block == null) return;
        if (!MaterialSetTag.CROPS.isTagged(block.getType())) return;
        if (event.getAction().isLeftClick()) return;
        BlockData blockData = block.getBlockData();
        Ageable age = (Ageable) blockData;
        if (age.getAge() != age.getMaximumAge()) return;
        event.getPlayer().breakBlock(block);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Block block = event.getBlock();
        BlockData blockData = block.getBlockData();
        Material material = block.getType();
        if (!MaterialSetTag.CROPS.isTagged(material)) return;
        Ageable age = (Ageable) blockData;
        if (age.getAge() != age.getMaximumAge()) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            block.setType(material);

            BlockData newBlockData = block.getBlockData();
            if (blockData instanceof Directional od && newBlockData instanceof Directional nd) {
                nd.setFacing(od.getFacing());
            }
            block.setBlockData(newBlockData);
        }, 1);
    }

    @EventHandler
    public void onBlockDrop(BlockDropItemEvent event) {
        Block block = event.getBlock();
        Material material = block.getType();
        if (!MaterialSetTag.CROPS.isTagged(material)) return;
        event.getItems().forEach(item -> {
            if (item.getItemStack().getType().toString().contains("_SEED")) item.getItemStack().subtract();
        });
    }

}
