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

    public final ConfigUtils configUtils = new ConfigUtils(this);
    public final MessageUtils messageUtils = new MessageUtils(this);
    public final HomeUtils homesUtils = new HomeUtils(this);
    public final Commands commands = new Commands(this);
    public final ConfigManipulation configManipulation = new ConfigManipulation(this);
    public final ConfigV5ToV6 configV5ToV6 = new ConfigV5ToV6(this);
    public final HomesV5ToV6 homesV5ToV6 = new HomesV5ToV6(this);

    @Override
    public void onEnable() {
        // Copy default config
        saveDefaultConfig();

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
        // Unregister events
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
        /* ToDo: Find areas in code where arrays, maps, and variables
            can be uninitialized, and create a clearMemory() function
            in each applicable class.
        */
        homesUtils.clearMemory();
        homesV5ToV6.clearMemory();
    }

}