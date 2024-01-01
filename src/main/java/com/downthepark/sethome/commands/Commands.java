package com.downthepark.sethome.commands;

import com.downthepark.sethome.SetHome;
import com.downthepark.sethome.utilities.MessageUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Commands {

    private final SetHome instance;

    public Commands(SetHome instance) {
        this.instance = instance;
    }

    public void cmdSetHome(Player player) {
        instance.homesUtils.setPlayerHome(player);
        if (instance.configUtils.CMD_SETHOME_MESSAGE_SHOW) {
            instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.CMD_SETHOME, player, null);
        }
    }

    public void cmdHome(Player player) {
        if (!instance.homesUtils.homeExists(player)) {
            instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.MISSING_HOME, player, null);
        }
        else {
            instance.homesUtils.sendPlayerHome(player);
            if (instance.configUtils.CMD_HOME_MESSAGE_SHOW)
                instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.CMD_HOME, player, null);
            if (instance.configUtils.EXTRA_PLAY_WARP_SOUND)
                player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }
    }

    public void cmdDelHome(Player player) {
        //asdf
    }

}
