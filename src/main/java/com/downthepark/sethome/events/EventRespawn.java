package com.downthepark.sethome.events;

import com.downthepark.sethome.SetHome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class EventRespawn implements Listener {

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (SetHome.getInstance().configUtils.EXTRA_RESPAWN_AT_HOME)
            if (SetHome.getInstance().homeUtils.homeExists(event.getPlayer(), false))
                event.setRespawnLocation(SetHome.getInstance().homeUtils.getPlayerHome(event.getPlayer()));
    }

}
