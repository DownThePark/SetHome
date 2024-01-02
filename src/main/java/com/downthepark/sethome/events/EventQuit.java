package com.downthepark.sethome.events;

import com.downthepark.sethome.SetHome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class EventQuit implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (SetHome.getInstance().homeUtils.getHomeFiles().containsKey(event.getPlayer().getUniqueId())) {
            SetHome.getInstance().homeUtils.getHomeFiles().remove(event.getPlayer().getUniqueId());
            //SetHome.getInstance().getLogger().info("Removed " + event.getPlayer().getUniqueId() + " from File HashMap");
        }
        if (SetHome.getInstance().homeUtils.getHomeYamls().containsKey(event.getPlayer().getUniqueId())) {
            SetHome.getInstance().homeUtils.getHomeYamls().remove(event.getPlayer().getUniqueId());
            //SetHome.getInstance().getLogger().info("Removed " + event.getPlayer().getUniqueId() + " from YamlConfiguration HashMap");
        }
    }

}
