package com.downthepark.sethome.utilities;

import com.downthepark.sethome.SetHome;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.logging.Level;

public class HomeUtils {

    private final SetHome instance;
    private File homesFile;

    public HomeUtils(SetHome instance) {
        this.instance = instance;
    }

    public File getHomesFile() {
        homesFile = new File(instance.getDataFolder(), "Homes.yml");
        return homesFile;
    }
    
    public YamlConfiguration getHomesYaml() {
        return YamlConfiguration.loadConfiguration(getHomesFile());
    }

    public void setPlayerHome(Player player) {
        getHomesYaml().set("homes." + player.getUniqueId() + ".X", player.getLocation().getX());
        getHomesYaml().set("homes." + player.getUniqueId() + ".Y", player.getLocation().getY());
        getHomesYaml().set("homes." + player.getUniqueId() + ".Z", player.getLocation().getZ());
        getHomesYaml().set("homes." + player.getUniqueId() + ".Yaw", player.getLocation().getYaw());
        getHomesYaml().set("homes." + player.getUniqueId() + ".Pitch", player.getLocation().getPitch());
        getHomesYaml().set("homes." + player.getUniqueId() + ".World", player.getLocation().getWorld().getName());
        saveHomesFile();
    }

    public Location getPlayerHome(Player player) {
        return new Location(
                Bukkit.getWorld(getHomesYaml().getString("homes." + player.getUniqueId() + ".World")),
                getHomesYaml().getDouble("homes." + player.getUniqueId() + ".X"),
                getHomesYaml().getDouble("homes." + player.getUniqueId() + ".Y"),
                getHomesYaml().getDouble("homes." + player.getUniqueId() + ".Z"),
                getHomesYaml().getLong("homes." + player.getUniqueId() + ".Yaw"),
                getHomesYaml().getLong("homes." + player.getUniqueId() + ".Pitch")
        );
    }

    public void sendPlayerHome(Player player) {
        player.teleport(this.getPlayerHome(player));
    }

    public boolean homeExists(Player player) {
        return getPlayerHome(player) != null;
    }

    public void saveHomesFile() {
        try {
            getHomesYaml().save(getHomesFile());
        } catch (Exception e) {
            instance.getLogger().log(Level.SEVERE, "Error saving homes.yaml!");
            e.printStackTrace();
        }
    }

}