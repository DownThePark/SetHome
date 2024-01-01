package com.downthepark.sethome;

import com.downthepark.sethome.commands.CmdDelHome;
import com.downthepark.sethome.commands.CmdHome;
import com.downthepark.sethome.commands.CmdSetHome;
import com.downthepark.sethome.events.EventMove;
import com.downthepark.sethome.events.EventRespawn;
import com.downthepark.sethome.utilities.CommandUtils;
import com.downthepark.sethome.utilities.ConfigUtils;
import com.downthepark.sethome.utilities.HomeUtils;
import com.downthepark.sethome.utilities.MessageUtils;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SetHome extends JavaPlugin {

    public CommandUtils commandUtils = new CommandUtils(this);
    public ConfigUtils configUtils = new ConfigUtils(this);
    public HomeUtils homesUtils = new HomeUtils(this);
    public MessageUtils messageUtils = new MessageUtils(this);

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getCommand("sethome").setExecutor(new CmdSetHome(this));
        getCommand("home").setExecutor(new CmdHome(this));
        getCommand("delhome").setExecutor(new CmdDelHome(this));
        getServer().getPluginManager().registerEvents(new EventRespawn(this), this);
        getServer().getPluginManager().registerEvents(new EventMove(this), this);
    }

    @Override
    public void onDisable() {
        PlayerRespawnEvent.getHandlerList().unregister(this);
        PlayerMoveEvent.getHandlerList().unregister(this);
        CmdSetHome.getCooldownMap().clear();
    }

}