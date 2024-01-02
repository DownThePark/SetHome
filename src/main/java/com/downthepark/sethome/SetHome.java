package com.downthepark.sethome;

import com.downthepark.sethome.commands.Commands;
import com.downthepark.sethome.converters.ConfigManipulation;
import com.downthepark.sethome.converters.ConfigV5ToV6;
import com.downthepark.sethome.converters.HomesV5ToV6;
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

    public ConfigUtils configUtils;
    public MessageUtils messageUtils;
    public HomeUtils homeUtils;
    public Commands commands;
    public ConfigManipulation configManipulation;
    public ConfigV5ToV6 configV5ToV6;
    public HomesV5ToV6 homesV5ToV6;

    @Override
    public void onEnable() {
        // Copy default config
        saveDefaultConfig();

        // Initialize objects
        configUtils = new ConfigUtils(this);
        messageUtils = new MessageUtils(this);
        homeUtils = new HomeUtils(this);
        commands = new Commands(this);
        configManipulation = new ConfigManipulation(this);
        configV5ToV6 = new ConfigV5ToV6(this);
        homesV5ToV6 = new HomesV5ToV6(this);

        // Register commands
        getCommand("sethome").setExecutor(new CommandExecutor(this));
        getCommand("home").setExecutor(new CommandExecutor(this));
        getCommand("deletehome").setExecutor(new CommandExecutor(this));

        // Register events
        getServer().getPluginManager().registerEvents(new EventRespawn(this), this);
        getServer().getPluginManager().registerEvents(new EventMove(this), this);

        // Check for updates
        new UpdateChecker(this, 32748).getVersion(version -> {
            if (!getDescription().getVersion().equals(version)) {
                getLogger().info("SetHome Remote version: " + version);
                getLogger().info("SetHome Local version: " + getDescription().getVersion());
                getLogger().info("There is a new update available for SetHome! Update available at https://www.spigotmc.org/resources/set-home.32748/");
            }
        });
    }

    @Override
    public void onDisable() {
        // Unregister commands
        getCommand("sethome").setExecutor(null);
        getCommand("home").setExecutor(null);
        getCommand("deletehome").setExecutor(null);

        // Unregister events
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);

        // De-initialize objects
        configUtils = null;
        messageUtils = null;
        homeUtils = null;
        commands = null;
        configManipulation = null;
        configV5ToV6 = null;
        homesV5ToV6 = null;
    }

}