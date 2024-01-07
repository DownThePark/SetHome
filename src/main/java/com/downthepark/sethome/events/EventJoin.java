package com.downthepark.sethome.events;

import com.downthepark.sethome.SetHome;
import com.downthepark.sethome.commands.CommandExecutor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class EventJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (SetHome.getInstance().configUtils.CMD_SETHOME_WARMUP_CANCEL_ON_MOVE) {
            CommandExecutor.getWarmupInEffect().get(CommandExecutor.COMMAND_TYPE.SETHOME).putIfAbsent(event.getPlayer().getUniqueId(), false);
            CommandExecutor.getWarmupTask().get(CommandExecutor.COMMAND_TYPE.SETHOME).putIfAbsent(event.getPlayer().getUniqueId(), null);
        }
        if (SetHome.getInstance().configUtils.CMD_HOME_WARMUP_CANCEL_ON_MOVE) {
            CommandExecutor.getWarmupInEffect().get(CommandExecutor.COMMAND_TYPE.HOME).putIfAbsent(event.getPlayer().getUniqueId(), false);
            CommandExecutor.getWarmupTask().get(CommandExecutor.COMMAND_TYPE.HOME).putIfAbsent(event.getPlayer().getUniqueId(), null);
        }
        if (SetHome.getInstance().configUtils.CMD_DELETEHOME_WARMUP_CANCEL_ON_MOVE) {
            CommandExecutor.getWarmupInEffect().get(CommandExecutor.COMMAND_TYPE.DELETEHOME).putIfAbsent(event.getPlayer().getUniqueId(), false);
            CommandExecutor.getWarmupTask().get(CommandExecutor.COMMAND_TYPE.DELETEHOME).putIfAbsent(event.getPlayer().getUniqueId(), null);
        }
    }

}
