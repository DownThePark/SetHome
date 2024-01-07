package com.downthepark.sethome.events;

import com.downthepark.sethome.SetHome;
import com.downthepark.sethome.commands.CommandExecutor;
import com.downthepark.sethome.utilities.MessageUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class EventMove implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (CommandExecutor.getWarmupInEffect().containsKey(event.getPlayer().getUniqueId())) {
            if (CommandExecutor.getWarmupInEffect().get(event.getPlayer().getUniqueId()).containsKey(CommandExecutor.COMMAND_TYPE.SETHOME)) {
                if (CommandExecutor.getWarmupInEffect().get(event.getPlayer().getUniqueId()).get(CommandExecutor.COMMAND_TYPE.SETHOME) == true) {
                    CommandExecutor.getWarmupTask().get(event.getPlayer().getUniqueId()).get(CommandExecutor.COMMAND_TYPE.SETHOME).cancel();
                    CommandExecutor.getWarmupInEffect().get(event.getPlayer().getUniqueId()).put(CommandExecutor.COMMAND_TYPE.SETHOME, false);
                    SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.ON_MOVE, event.getPlayer(), null);
                }
            }
            if (CommandExecutor.getWarmupInEffect().get(event.getPlayer().getUniqueId()).containsKey(CommandExecutor.COMMAND_TYPE.HOME)) {
                if (CommandExecutor.getWarmupInEffect().get(event.getPlayer().getUniqueId()).get(CommandExecutor.COMMAND_TYPE.HOME) == true) {
                    CommandExecutor.getWarmupTask().get(event.getPlayer().getUniqueId()).get(CommandExecutor.COMMAND_TYPE.HOME).cancel();
                    CommandExecutor.getWarmupInEffect().get(event.getPlayer().getUniqueId()).put(CommandExecutor.COMMAND_TYPE.HOME, false);
                    SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.ON_MOVE, event.getPlayer(), null);
                }
            }
            if (CommandExecutor.getWarmupInEffect().get(event.getPlayer().getUniqueId()).containsKey(CommandExecutor.COMMAND_TYPE.DELETEHOME)) {
                if (CommandExecutor.getWarmupInEffect().get(event.getPlayer().getUniqueId()).get(CommandExecutor.COMMAND_TYPE.DELETEHOME) == true) {
                    CommandExecutor.getWarmupTask().get(event.getPlayer().getUniqueId()).get(CommandExecutor.COMMAND_TYPE.DELETEHOME).cancel();
                    CommandExecutor.getWarmupInEffect().get(event.getPlayer().getUniqueId()).put(CommandExecutor.COMMAND_TYPE.DELETEHOME, false);
                    SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.ON_MOVE, event.getPlayer(), null);
                }
            }
        }
    }

}
