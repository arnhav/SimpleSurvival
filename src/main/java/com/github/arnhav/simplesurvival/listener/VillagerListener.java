package com.github.arnhav.simplesurvival.listener;

import com.destroystokyo.paper.entity.villager.Reputation;
import com.destroystokyo.paper.entity.villager.ReputationType;
import com.github.arnhav.simplesurvival.util.Listener;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.entity.memory.MemoryKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class VillagerListener extends Listener {

    private final JavaPlugin plugin;

    public VillagerListener(JavaPlugin plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onVillagerInteract(PlayerInteractAtEntityEvent event) {
        Player player = event.getPlayer();
        Entity entity = event.getRightClicked();
        if (!player.isSneaking()) return;
        if (!(entity instanceof Villager villager)) return;
        EquipmentSlot hand = event.getHand();
        if (hand != EquipmentSlot.HAND) return;
        EntityEquipment ee = player.getEquipment();
        ItemStack itemStack = ee.getItem(hand);
        if (itemStack.getType() != Material.WRITABLE_BOOK) return;
        event.setCancelled(true);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            player.closeInventory();
            player.openBook(getVillagerBook(villager, player.getUniqueId()));
        }, 1);
    }

    public ItemStack getVillagerBook(Villager villager, UUID uuid) {
        ItemStack itemStack = new ItemStack(Material.WRITTEN_BOOK);
        BookMeta bookMeta = (BookMeta) itemStack.getItemMeta();
        Component villagerInfo =
                Component.text(
                        "Profession: " + villager.getProfession() + "\n" +
                                "Level: " + villager.getVillagerLevel() + "\n" +
                                "XP: " + villager.getVillagerExperience() + "\n" +
                                "Restocks: " + villager.getRestocksToday() + "\n"
                );
        Location workStation = villager.getMemory(MemoryKey.JOB_SITE);
        villagerInfo = villagerInfo.append(
                Component.text("Workstation: ")
                        .append(
                                Component.text(locationName(workStation) + "\n")
                                        .hoverEvent(HoverEvent.showText(
                                                Component.text(getWorkstationInfo(workStation))
                                        ))
                        )
        );
        Reputation reputation = villager.getReputation(uuid);
        if (reputation != null) {
            villagerInfo = villagerInfo.append(
                    Component.text("Reputation: \n")
            );
            for (ReputationType reputationType : ReputationType.values()) {
                villagerInfo = villagerInfo.append(
                        Component.text("  " + reputationType + ": " + reputation.getReputation(reputationType) + "\n")

                );
            }
        }
        bookMeta.addPages(villagerInfo);
        bookMeta.setTitle("Villager");
        bookMeta.setAuthor("Info");
        itemStack.setItemMeta(bookMeta);
        return itemStack;
    }

    public String locationName(Location l) {
        if (l == null) return "[Missing]";
        return "[Present]";
    }

    public String getWorkstationInfo(Location l) {
        if (l == null) return "Missing!";
        return l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + "\n\n" +
                l.getBlock().getType();
    }
}
