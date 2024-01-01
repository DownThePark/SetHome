package com.downthepark.sethome.events;

import com.downthepark.sethome.SetHome;
import com.downthepark.sethome.commands.CmdHome;
import com.downthepark.sethome.commands.CmdSetHome;
import com.downthepark.sethome.utilities.MessageUtils;
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
            if (CmdSetHome.isWarmupInEffect()) {
                CmdSetHome.getWarmupTask().cancel();
                CmdSetHome.setWarmupInEffect(false);
                instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.ON_MOVE, event.getPlayer(), null);
            }
        }
        if (instance.configUtils.CMD_HOME_WARMUP_CANCEL_ON_MOVE) {
            if (CmdHome.isWarmupInEffect()) {
                CmdHome.getWarmupTask().cancel();
                CmdHome.setWarmupInEffect(false);
                instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.ON_MOVE, event.getPlayer(), null);
            }
        }
        if (instance.configUtils.CMD_DELHOME_WARMUP_CANCEL_ON_MOVE) {

        }
    }

}
