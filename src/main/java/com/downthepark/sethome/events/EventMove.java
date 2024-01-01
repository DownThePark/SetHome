package com.downthepark.sethome.events;

import com.downthepark.sethome.SetHome;
import com.downthepark.sethome.commands.CommandExecutor;
import com.downthepark.sethome.utilities.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventMove implements Listener {

    private final SetHome instance;

    public EventMove(SetHome instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (instance.configUtils.CMD_SETHOME_WARMUP_CANCEL_ON_MOVE) {
            cancelMove(CommandExecutor.COMMAND_TYPE.SETHOME, event.getPlayer());
        }
        if (instance.configUtils.CMD_HOME_WARMUP_CANCEL_ON_MOVE) {
            cancelMove(CommandExecutor.COMMAND_TYPE.HOME, event.getPlayer());
        }
        if (instance.configUtils.CMD_DELETEHOME_WARMUP_CANCEL_ON_MOVE) {
            cancelMove(CommandExecutor.COMMAND_TYPE.DELETEHOME, event.getPlayer());
        }
    }

    private void cancelMove(CommandExecutor.COMMAND_TYPE commandType, Player player) {
        if (CommandExecutor.getWarmupInEffect().get(commandType)) {
            CommandExecutor.getWarmupInEffect().put(commandType, false);
            CommandExecutor.getWarmupTask().get(commandType).cancel();
            instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.ON_MOVE, player, null);
        }
    }

}
