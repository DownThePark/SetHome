package com.downthepark.sethome;

import com.downthepark.sethome.commands.Commands;
import com.downthepark.sethome.events.EventMove;
import com.downthepark.sethome.events.EventRespawn;
import com.downthepark.sethome.commands.CommandExecutor;
import com.downthepark.sethome.utilities.ConfigUtils;
import com.downthepark.sethome.utilities.HomeUtils;
import com.downthepark.sethome.utilities.MessageUtils;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SetHome extends JavaPlugin {

    public ConfigUtils configUtils = new ConfigUtils(this);
    public MessageUtils messageUtils = new MessageUtils(this);
    public HomeUtils homesUtils = new HomeUtils(this);
    public Commands commands = new Commands(this);

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("sethome").setExecutor(new CommandExecutor(this));
        getCommand("home").setExecutor(new CommandExecutor(this));
        getCommand("delhome").setExecutor(new CommandExecutor(this));
        getServer().getPluginManager().registerEvents(new EventRespawn(this), this);
        getServer().getPluginManager().registerEvents(new EventMove(this), this);
    }

    @Override
    public void onDisable() {
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
    }

}