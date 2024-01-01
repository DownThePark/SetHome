package com.downthepark.sethome.utilities;

import com.downthepark.sethome.SetHome;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
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

    public boolean homeExists(Player player, boolean verbose) {
        String worldPath = "Homes." + player.getUniqueId() + ".World";
        if (homesYaml.getString(worldPath) == null) {
            if (verbose)
                instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.MISSING_HOME, player, null);
            return false;
        }
        if (Bukkit.getWorld(homesYaml.getString(worldPath)) == null) {
            if (verbose)
                instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.MISSING_WORLD, player, null);
            return false;
        }
        return true;
    }

    public void setPlayerHome(Player player) {
        homesYaml.set("Homes." + player.getUniqueId() + ".X", player.getLocation().getX());
        homesYaml.set("Homes." + player.getUniqueId() + ".Y", player.getLocation().getY());
        homesYaml.set("Homes." + player.getUniqueId() + ".Z", player.getLocation().getZ());
        homesYaml.set("Homes." + player.getUniqueId() + ".Yaw", player.getLocation().getYaw());
        homesYaml.set("Homes." + player.getUniqueId() + ".Pitch", player.getLocation().getPitch());
        homesYaml.set("Homes." + player.getUniqueId() + ".World", player.getLocation().getWorld().getName());
        saveHomesFile();
        if (instance.configUtils.CMD_SETHOME_MESSAGE_SHOW)
            instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.CMD_SETHOME, player, null);
    }

    public Location getPlayerHome(Player player) {
        return new Location(
                Bukkit.getWorld(homesYaml.getString("Homes." + player.getUniqueId() + ".World")),
                homesYaml.getDouble("Homes." + player.getUniqueId() + ".X"),
                homesYaml.getDouble("Homes." + player.getUniqueId() + ".Y"),
                homesYaml.getDouble("Homes." + player.getUniqueId() + ".Z"),
                homesYaml.getLong("Homes." + player.getUniqueId() + ".Yaw"),
                homesYaml.getLong("Homes." + player.getUniqueId() + ".Pitch")
        );
    }

    public void sendPlayerHome(Player player) {
        if (!homeExists(player, true)) return;
        Location home = getPlayerHome(player);
        player.teleport(home);
        if (instance.configUtils.CMD_HOME_MESSAGE_SHOW)
            instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.CMD_HOME, player, null);
        if (instance.configUtils.EXTRA_PLAY_WARP_SOUND)
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
    }

    public void deletePlayerHome(Player player) {
        homesYaml.set("Homes." + player.getUniqueId() + ".X", null);
        homesYaml.set("Homes." + player.getUniqueId() + ".Y", null);
        homesYaml.set("Homes." + player.getUniqueId() + ".Z", null);
        homesYaml.set("Homes." + player.getUniqueId() + ".Yaw", null);
        homesYaml.set("Homes." + player.getUniqueId() + ".Pitch", null);
        homesYaml.set("Homes." + player.getUniqueId() + ".World", null);
        saveHomesFile();
        if (instance.configUtils.CMD_DELETEHOME_MESSAGE_SHOW)
            instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.CMD_DELETEHOME, player, null);
    }

    public void saveHomesFile() {
        try {
            homesYaml.save(homesFile);
        } catch (Exception e) {
            instance.getLogger().log(Level.SEVERE, "Error saving Homes.yaml!");
            e.printStackTrace();
        }
    }

}