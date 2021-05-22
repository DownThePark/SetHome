package me.downthepark.sethome;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class SetHomeEvents implements Listener {
    private SetHome plugin;

    SetHomeEvents(SetHome plugin) { // Constructor
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (plugin.getConfig().getBoolean("respawn-player-at-home")) {
            Player player = event.getPlayer();
            if (plugin.homeIsSet(player)) {
                event.setRespawnLocation(plugin.getHomeLocation(player));
            }
        }
    }
}