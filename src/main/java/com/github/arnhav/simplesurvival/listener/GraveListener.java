package com.github.arnhav.simplesurvival.listener;

import com.github.arnhav.simplesurvival.util.Listener;
import de.tr7zw.nbtapi.NBTCompound;
import de.tr7zw.nbtapi.NBTEntity;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class GraveListener extends Listener {

    public GraveListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractArmorStand(PlayerInteractAtEntityEvent event) {
        if (!(event.getRightClicked() instanceof ArmorStand as)) return;
        if (!as.getScoreboardTags().contains("GRAVE")) return;
        event.setCancelled(true);
        Player p = event.getPlayer();
        if (!claimGrave(as, p)) return;
        as.remove();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.isCancelled()) return;
        if (event.getKeepInventory()) return;
        Player p = event.getEntity();
        spawnGrave(p);
        event.getDrops().removeIf(Objects::nonNull);
    }

    public List<String> getListFromArray(ItemStack[] itemStacks) {
        List<String> items = new ArrayList<>();
        for (ItemStack itemStack : itemStacks) {
            items.add(itemToString(itemStack));
        }
        return items;
    }

    public ItemStack[] getArrayFromList(List<String> items) {
        if (items == null) return new ItemStack[0];
        ItemStack[] itemStacks = new ItemStack[items.size()];
        for (int i = 0; i < items.size(); i++) {
            itemStacks[i] = stringToItem(items.get(i));
        }
        return itemStacks;
    }

    public String itemToString(ItemStack itemStack) {
        YamlConfiguration config = new YamlConfiguration();
        config.set("i", itemStack);
        return config.saveToString();
    }

    public ItemStack stringToItem(String string) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(string);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return config.getItemStack("i");
    }

    public void spawnGrave(Player p) {
        ArmorStand as = (ArmorStand) p.getWorld().spawnEntity(p.getLocation(), EntityType.ARMOR_STAND);
        as.setBasePlate(false);
        as.setArms(true);
        as.setInvulnerable(true);
        as.setGravity(false);
        as.setCustomNameVisible(true);
        as.setCustomName(p.getName() + "'s Grave");
        as.addScoreboardTag("GRAVE");

        for (EquipmentSlot es : EquipmentSlot.values()) {
            as.setItem(es, p.getEquipment().getItem(es));
        }

        NBTEntity nbte = new NBTEntity(as);
        NBTCompound nbtc = nbte.getPersistentDataContainer();
        nbtc.setObject("ac", getListFromArray(p.getInventory().getArmorContents()));
        nbtc.setObject("sc", getListFromArray(p.getInventory().getStorageContents()));
        nbtc.setObject("ec", getListFromArray(p.getInventory().getExtraContents()));
        nbtc.setString("ownerUUID", p.getUniqueId().toString());

        p.getInventory().setContents(new ItemStack[p.getInventory().getContents().length]);
    }

    public boolean claimGrave(ArmorStand as, Player p) {
        NBTEntity nbte = new NBTEntity(as);
        NBTCompound nbtc = nbte.getPersistentDataContainer();
        if (!nbtc.hasKey("ownerUUID")) return false;
        if (!nbtc.getString("ownerUUID").equals(p.getUniqueId().toString())) return false;
        ItemStack[] curContents = p.getInventory().getContents();
        p.getInventory().setArmorContents(getArrayFromList(nbtc.getObject("ac", List.class)));
        p.getInventory().setStorageContents(getArrayFromList(nbtc.getObject("sc", List.class)));
        p.getInventory().setExtraContents(getArrayFromList(nbtc.getObject("ec", List.class)));
        for (ItemStack cur : curContents) {
            if (cur == null) continue;
            HashMap<Integer, ItemStack> overflow = p.getInventory().addItem(cur);
            for (ItemStack o : overflow.values()) {
                p.getWorld().dropItem(p.getLocation(), o);
            }
        }
        return true;
    }

}
