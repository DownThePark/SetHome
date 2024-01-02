package com.downthepark.sethome.events;

import com.downthepark.sethome.SetHome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventRespawn implements Listener {

    private final SetHome instance;

    public EventRespawn(SetHome instance) {
        this.instance = instance;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (instance.configUtils.EXTRA_RESPAWN_AT_HOME)
            if (instance.homeUtils.homeExists(event.getPlayer(), false))
                event.setRespawnLocation(instance.homeUtils.getPlayerHome(event.getPlayer()));
    }

}
