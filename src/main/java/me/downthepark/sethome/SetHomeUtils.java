package me.downthepark.sethome;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class SetHomeUtils {

    private SetHome instance;

    public SetHomeUtils(SetHome instance) {
        this.instance = instance;
    }

    public void setHome(Player player) {
        instance.homes.set("Homes." + player.getUniqueId() + ".X", player.getLocation().getX());
        instance.homes.set("Homes." + player.getUniqueId() + ".Y", player.getLocation().getY());
        instance.homes.set("Homes." + player.getUniqueId() + ".Z", player.getLocation().getZ());
        instance.homes.set("Homes." + player.getUniqueId() + ".Yaw", player.getLocation().getYaw());
        instance.homes.set("Homes." + player.getUniqueId() + ".Pitch", player.getLocation().getPitch());
        instance.homes.set("Homes." + player.getUniqueId() + ".World", player.getLocation().getWorld().getName());
        instance.saveHomesFile();
    }

    public void sendHome(Player player) {
        player.teleport(getHomeLocation(player));
    }

    public Location getHomeLocation(Player player) {

        return new Location(
                Bukkit.getWorld(instance.homes.getString("Homes." + player.getUniqueId() + ".World")),
                instance.homes.getDouble("Homes." + player.getUniqueId() + ".X"),
                instance.homes.getDouble("Homes." + player.getUniqueId() + ".Y"),
                instance.homes.getDouble("Homes." + player.getUniqueId() + ".Z"),
                instance.homes.getLong("Homes." + player.getUniqueId() + ".Yaw"),
                instance.homes.getLong("Homes." + player.getUniqueId() + ".Pitch")
        );
    }

    public boolean homeIsNull(Player player) {
        return instance.homes.getString("Homes." + player.getUniqueId()) == null;
    }

    public void deleteHome(Player player) {
        instance.homes.set("Homes." + player.getUniqueId() + ".X", null);
        instance.homes.set("Homes." + player.getUniqueId() + ".Y", null);
        instance.homes.set("Homes." + player.getUniqueId() + ".Z", null);
        instance.homes.set("Homes." + player.getUniqueId() + ".Yaw", null);
        instance.homes.set("Homes." + player.getUniqueId() + ".Pitch", null);
        instance.homes.set("Homes." + player.getUniqueId() + ".World", null);
        instance.saveHomesFile();
    }

}