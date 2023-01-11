package com.github.arnhav.simplesurvival.command;

import com.github.arnhav.simplesurvival.util.CommandExecutor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LinkCommand extends CommandExecutor {

    public LinkCommand(String command, JavaPlugin plugin) {
        super(command, plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player s)) return false;

        ItemStack itemStack = s.getInventory().getItemInMainHand();
        if (itemStack.getType() == Material.AIR) {
            sender.sendActionBar(Component.text("Invalid item!"));
            return true;
        }

        Component display = Component.text(s.getName() + " linked their ")
                .append(itemStack.displayName());

        for (Player p : Bukkit.getOnlinePlayers()) {
            p.sendMessage(display.hoverEvent(itemStack));
        }

        return true;
    }
}
