package com.github.arnhav.simplesurvival.listener;

import com.github.arnhav.simplesurvival.util.Listener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityPortalListener extends Listener {

    public EntityPortalListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onEntityGoThruPortal(EntityPortalExitEvent event) {
        Location dest = event.getTo();
        if (dest == null) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;
        cloneThisEntity((LivingEntity) event.getEntity(), dest);
    }

    public LivingEntity cloneThisEntity(LivingEntity e, Location dest) {
        World world = dest.getWorld();

        LivingEntity newE = (LivingEntity) world.spawnEntity(dest, e.getType());
        newE.setCustomName(e.getCustomName());
        AttributeInstance health = newE.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (health != null) health.setBaseValue(e.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());
        AttributeInstance moveSpeed = newE.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
        if (moveSpeed != null) moveSpeed.setBaseValue(e.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue());
        //-----Animals-----//
        if (newE instanceof Animals) {
            ((Animals) newE).setAge(((Animals) e).getAge());
            newE.setTicksLived(e.getTicksLived());
            //-----Tameable Animals-----//
            if (newE instanceof Tameable) {
                if (((Tameable) e).isTamed()) {
                    ((Tameable) newE).setOwner(((Tameable) e).getOwner());
                }
                if (newE instanceof AbstractHorse) {//All Horse Types
                    AttributeInstance jumpHeight = newE.getAttribute(Attribute.HORSE_JUMP_STRENGTH);
                    jumpHeight.setBaseValue(e.getAttribute(Attribute.HORSE_JUMP_STRENGTH).getBaseValue());
                    if (((AbstractHorse) e).getInventory().getSaddle() != null) {
                        ((AbstractHorse) newE).getInventory().setSaddle(((AbstractHorse) e).getInventory().getSaddle());
                    }
                    if (newE instanceof Horse) {//Horse
                        ((Horse) newE).setColor(((Horse) e).getColor());
                        ((Horse) newE).setStyle(((Horse) e).getStyle());
                        if (((Horse) e).getInventory().getArmor() != null) {
                            ((Horse) newE).getInventory().setSaddle(((Horse) e).getInventory().getArmor());
                        }
                    }
                    if (newE instanceof ChestedHorse) {//Mule, Donkey, Llama
                        ((ChestedHorse) e).getInventory().getStorageContents();
                        ((ChestedHorse) newE).setCarryingChest(true);
                        ((ChestedHorse) newE).getInventory().setContents(((ChestedHorse) e).getInventory().getContents());
                        if (newE instanceof Llama) {
                            if (((Llama) e).getInventory().getDecor() != null) {
                                ((Llama) newE).getInventory().setDecor(((Llama) e).getInventory().getDecor());
                            }
                        }
                    }
                }
                if (newE instanceof Sittable) {//Cat, Wolf, Parrot
                    ((Sittable) newE).setSitting(true);
                    if (newE instanceof Cat) {
                        ((Cat) newE).setCatType(((Cat) e).getCatType());
                        ((Cat) newE).setCollarColor(((Cat) e).getCollarColor());
                    }
                    if (newE instanceof Parrot) {
                        ((Parrot) newE).setVariant(((Parrot) e).getVariant());
                    }
                    if (newE instanceof Wolf) {
                        ((Wolf) newE).setCollarColor(((Wolf) e).getCollarColor());
                    }
                }
            }
        }
        e.remove();
        return newE;
    }
}
