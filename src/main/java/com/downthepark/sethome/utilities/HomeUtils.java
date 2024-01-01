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
    private final File homesFile;
    private final YamlConfiguration homesYaml;

    public HomeUtils(SetHome instance) {
        this.instance = instance;
        homesFile = new File(instance.getDataFolder(), "Homes.yml");
        homesYaml = YamlConfiguration.loadConfiguration(homesFile);
    }

    public void setPlayerHome(Player player) {
        player.sendMessage("Reached set home function");
        homesYaml.set("homes." + player.getUniqueId() + ".X", player.getLocation().getX());
        homesYaml.set("homes." + player.getUniqueId() + ".Y", player.getLocation().getY());
        homesYaml.set("homes." + player.getUniqueId() + ".Z", player.getLocation().getZ());
        homesYaml.set("homes." + player.getUniqueId() + ".Yaw", player.getLocation().getYaw());
        homesYaml.set("homes." + player.getUniqueId() + ".Pitch", player.getLocation().getPitch());
        homesYaml.set("homes." + player.getUniqueId() + ".World", player.getLocation().getWorld().getName());
        saveHomesFile();
        player.sendMessage("finished set home function");
    }

    public Location getPlayerHome(Player player) {
        return new Location(
                Bukkit.getWorld(homesYaml.getString("homes." + player.getUniqueId() + ".World")),
                homesYaml.getDouble("homes." + player.getUniqueId() + ".X"),
                homesYaml.getDouble("homes." + player.getUniqueId() + ".Y"),
                homesYaml.getDouble("homes." + player.getUniqueId() + ".Z"),
                homesYaml.getLong("homes." + player.getUniqueId() + ".Yaw"),
                homesYaml.getLong("homes." + player.getUniqueId() + ".Pitch")
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
            homesYaml.save(homesFile);
        } catch (Exception e) {
            instance.getLogger().log(Level.SEVERE, "Error saving homes.yaml!");
            e.printStackTrace();
        }
    }

}