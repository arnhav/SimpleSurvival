package com.github.arnhav.simplesurvival;

import com.github.arnhav.simplesurvival.command.LCCommand;
import com.github.arnhav.simplesurvival.command.LinkCommand;
import com.github.arnhav.simplesurvival.listener.*;
import com.github.arnhav.simplesurvival.tasks.ChunkTickTask;
import com.github.arnhav.simplesurvival.tasks.JebWolfTask;
import com.github.arnhav.simplesurvival.tasks.TabListTask;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleSurvival extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        new LinkCommand("link", this);
        new LCCommand("lc", this);

        new MobGriefListener(this);
        new FarmingListener(this);
        new PlayerDeathListener(this);
        new GraveListener(this);
        new VillagerListener(this);
        new MobPickupListener(this);
        new SignListener(this);
        new EntityPortalListener(this);

        new TabListTask(this);
        new JebWolfTask(this);
        new ChunkTickTask(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
