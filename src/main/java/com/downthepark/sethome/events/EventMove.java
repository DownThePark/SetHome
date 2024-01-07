package com.downthepark.sethome.events;

import com.downthepark.sethome.SetHome;
import com.downthepark.sethome.commands.CommandExecutor;
import com.downthepark.sethome.utilities.MessageUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (SetHome.getInstance().configUtils.CMD_SETHOME_WARMUP_CANCEL_ON_MOVE) {
            cancelMove(CommandExecutor.COMMAND_TYPE.SETHOME, event.getPlayer());
        }
        if (SetHome.getInstance().configUtils.CMD_HOME_WARMUP_CANCEL_ON_MOVE) {
            cancelMove(CommandExecutor.COMMAND_TYPE.HOME, event.getPlayer());
        }
        if (SetHome.getInstance().configUtils.CMD_DELETEHOME_WARMUP_CANCEL_ON_MOVE) {
            cancelMove(CommandExecutor.COMMAND_TYPE.DELETEHOME, event.getPlayer());
        }
    }

    private void cancelMove(CommandExecutor.COMMAND_TYPE commandType, Player player) {
        if (CommandExecutor.getWarmupInEffect().get(commandType).get(player.getUniqueId())) {
            CommandExecutor.getWarmupTask().get(commandType).get(player.getUniqueId()).cancel();
            CommandExecutor.getWarmupInEffect().get(commandType).put(player.getUniqueId(), false);
            SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.ON_MOVE, player, null);
        }
    }

}
