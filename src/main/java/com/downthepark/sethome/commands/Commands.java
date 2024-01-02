package com.downthepark.sethome.commands;

import com.downthepark.sethome.SetHome;
import org.bukkit.entity.Player;

public class Commands {

    private final SetHome instance;

    public Commands(SetHome instance) {
        this.instance = instance;
    }

    public void cmdSetHome(Player player) {
        instance.homeUtils.setPlayerHome(player);
    }

    public void cmdHome(Player player) {
        instance.homeUtils.sendPlayerHome(player);
    }

    public void cmdDeleteHome(Player player) {
        if (instance.homeUtils.homeExists(player, true))
            instance.homeUtils.deletePlayerHome(player);
    }

}
