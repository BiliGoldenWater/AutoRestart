package io.github.biligoldenwater.autorestart;

import io.github.biligoldenwater.autorestart.commands.CommandAutoRestart;
import io.github.biligoldenwater.autorestart.commands.TabAutoRestart;
import io.github.biligoldenwater.autorestart.modules.RestartTimer;
import org.bukkit.plugin.java.JavaPlugin;


public final class AutoRestart extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        new CommandAutoRestart(this);
        new TabAutoRestart(this);

        if (getConfig().getInt("restartTime") <= 600) {
            getConfig().set("restartTime", 600);
        }
        RestartTimer.startRestartTimer(this,getConfig().getInt("restartTime"));

        getLogger().info("AutoRestart Enabled.");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();

        RestartTimer.stopRestartTimer();

        getLogger().info("AutoRestart Disabled.");
    }

}
