package com.downthepark.sethome.commands;

import com.downthepark.sethome.SetHome;
import org.bukkit.entity.Player;

public class Commands {

    private final SetHome instance;

    public Commands(SetHome instance) {
        this.instance = instance;
    }

    public void cmdSetHome(Player player) {
        instance.homesUtils.setPlayerHome(player);
    }

    public void cmdHome(Player player) {
        instance.homesUtils.sendPlayerHome(player);
    }

    public void cmdDelHome(Player player) {
        instance.homesUtils.deletePlayerHome(player);
    }

}
