package com.downthepark.sethome;

import com.downthepark.sethome.commands.Commands;
import com.downthepark.sethome.converters.ConfigV5ToV6;
import com.downthepark.sethome.events.EventMove;
import com.downthepark.sethome.events.EventRespawn;
import com.downthepark.sethome.commands.CommandExecutor;
import com.downthepark.sethome.utilities.ConfigUtils;
import com.downthepark.sethome.utilities.HomeUtils;
import com.downthepark.sethome.utilities.MessageUtils;
import com.downthepark.sethome.utilities.UpdateChecker;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SetHome extends JavaPlugin {

    public final ConfigUtils configUtils = new ConfigUtils(this);
    public final MessageUtils messageUtils = new MessageUtils(this);
    public final HomeUtils homesUtils = new HomeUtils(this);
    public final Commands commands = new Commands(this);
    public final ConfigV5ToV6 configV5ToV6 = new ConfigV5ToV6(this);

    @Override
    public void onEnable() {
        // Copy default config
        saveDefaultConfig();

        // Register commands
        getCommand("sethome").setExecutor(new CommandExecutor(this));
        getCommand("home").setExecutor(new CommandExecutor(this));
        getCommand("delhome").setExecutor(new CommandExecutor(this));

        // Register events
        getServer().getPluginManager().registerEvents(new EventRespawn(this), this);
        getServer().getPluginManager().registerEvents(new EventMove(this), this);

        // Check for updates
        new UpdateChecker(this, 32748).getVersion(version -> {
            if (getDescription().getVersion().equals(version)) {
                getLogger().info("There are no new updates available.");
            } else {
                getLogger().info("There is a new update available! Update available at https://www.spigotmc.org/resources/set-home.32748/");
            }
        });
    }

    @Override
    public void onDisable() {
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
    }

}