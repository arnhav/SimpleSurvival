package com.github.arnhav.simplesurvival.listener;

import com.github.arnhav.simplesurvival.util.Listener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.spigotmc.event.entity.EntityDismountEvent;

public class MobPickupListener extends Listener implements Runnable {

    private final JavaPlugin plugin;
    private final String tag = "CARRYING", removal = "REMOVAL";

    public MobPickupListener(JavaPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
        Bukkit.getScheduler().runTaskTimer(plugin, this, 0, 20);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPickupEntity(PlayerInteractAtEntityEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (!(entity instanceof LivingEntity livingEntity)) return;
        EquipmentSlot hand = event.getHand();
        if (hand != EquipmentSlot.HAND) return;
        if (!player.isSneaking()) return;
        if (player.getScoreboardTags().contains(tag)) return;
        if (livingEntity.getVehicle() != null) livingEntity.getVehicle().eject();
        event.setCancelled(true);
        player.addPassenger(livingEntity);
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.addScoreboardTag(tag), 1);
    }

    @EventHandler
    public void onRightClickEntity(PlayerInteractEntityEvent event) {
        doEntityPlace(event.getHand(), event.getPlayer());
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if (event.getAction().isLeftClick()) return;
        doEntityPlace(event.getHand(), event.getPlayer());
    }

    private void doEntityPlace(EquipmentSlot hand, Player player) {
        if (hand != EquipmentSlot.HAND) return;
        if (!player.isSneaking()) return;
        if (!player.getScoreboardTags().contains(tag)) return;
        player.removeScoreboardTag(tag);
        player.addScoreboardTag(removal);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.eject();
            player.removeScoreboardTag(removal);
        }, 1);
    }

    @EventHandler
    public void onChangeHands(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (!player.getScoreboardTags().contains(tag)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onSwapHands(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (!player.getScoreboardTags().contains(tag)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        Entity dismounted = event.getDismounted();
        if (!(dismounted instanceof Player player)) return;
        if (!player.getScoreboardTags().contains(tag)) return;
        if (player.getScoreboardTags().contains(removal)) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        Entity vehicle = entity.getVehicle();
        if (vehicle == null) return;
        vehicle.removeScoreboardTag(tag);
        vehicle.removeScoreboardTag(removal);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        player.removeScoreboardTag(tag);
        player.removeScoreboardTag(removal);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!player.getScoreboardTags().contains(tag)) return;
        player.eject();
        player.removeScoreboardTag(tag);
        player.removeScoreboardTag(removal);
    }


    @Override
    public void run() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (!p.getScoreboardTags().contains(tag)) continue;
            PotionEffect effect = new PotionEffect(PotionEffectType.SLOW, 30, 0, false, false, false);
            p.addPotionEffect(effect);
        }
    }
}
