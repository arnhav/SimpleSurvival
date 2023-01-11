package com.github.arnhav.simplesurvival.listener;

import com.github.arnhav.simplesurvival.util.Listener;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDeathListener extends Listener {

    public PlayerDeathListener(JavaPlugin plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Component message = event.deathMessage();
        if (message == null) return;
        message = message.append(Component.text(". Press F to pay respects."));
        event.deathMessage(message);
    }
}
