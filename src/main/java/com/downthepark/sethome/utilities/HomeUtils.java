package com.downthepark.sethome.utilities;

import com.downthepark.sethome.SetHome;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;

public class HomeUtils {

    private final String homesFilePath;
    private final HashMap<UUID, File> homeFiles;
    private final HashMap<UUID, YamlConfiguration> homeYamls;

    public final String PATH_X = "Homes.main.X";
    public final String PATH_Y = "Homes.main.Y";
    public final String PATH_Z = "Homes.main.Z";
    public final String PATH_YAW = "Homes.main.Yaw";
    public final String PATH_PITCH = "Homes.main.Pitch";
    public final String PATH_WORLD = "Homes.main.World";
    
    public HomeUtils() {
        homesFilePath = SetHome.getInstance().getDataFolder() + File.separator + "homes";
        homeFiles = new HashMap<>();
        homeYamls = new HashMap<>();
        if (!new File(homesFilePath).exists()) {
            try {
                Files.createDirectories(Paths.get(homesFilePath));
            }
            catch (IOException e) {
                // Do nothing
            }
        }

    }

    public HashMap<UUID, File> getHomeFiles() {
        return homeFiles;
    }

    public HashMap<UUID, YamlConfiguration> getHomeYamls() {
        return homeYamls;
    }

    public boolean homeExists(Player player, boolean verbose) {
        if (getHomeYaml(player).getString(PATH_WORLD) == null) {
            if (verbose)
                SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.MISSING_HOME, player, null);
            return false;
        }
        if (Bukkit.getWorld(getHomeYaml(player).getString(PATH_WORLD)) == null) {
            if (verbose)
                SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.MISSING_WORLD, player, null);
            return false;
        }
        return true;
    }

    public void setPlayerHome(Player player) {
        getHomeYaml(player).set(PATH_X, player.getLocation().getX());
        getHomeYaml(player).set(PATH_Y, player.getLocation().getY());
        getHomeYaml(player).set(PATH_Z, player.getLocation().getZ());
        getHomeYaml(player).set(PATH_YAW, player.getLocation().getYaw());
        getHomeYaml(player).set(PATH_PITCH, player.getLocation().getPitch());
        getHomeYaml(player).set(PATH_WORLD, player.getLocation().getWorld().getName());
        saveHomesFile(player);
        if (SetHome.getInstance().configUtils.CMD_SETHOME_MESSAGE_SHOW)
            SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.CMD_SETHOME, player, null);
    }

    public Location getPlayerHome(Player player) {
        return new Location(
                Bukkit.getWorld(getHomeYaml(player).getString(PATH_WORLD)),
                getHomeYaml(player).getDouble(PATH_X),
                getHomeYaml(player).getDouble(PATH_Y),
                getHomeYaml(player).getDouble(PATH_Z),
                getHomeYaml(player).getLong(PATH_YAW),
                getHomeYaml(player).getLong(PATH_PITCH)
        );
    }

    public void sendPlayerHome(Player player) {
        if (!homeExists(player, true)) return;
        Location home = getPlayerHome(player);
        player.teleport(home);
        if (SetHome.getInstance().configUtils.CMD_HOME_MESSAGE_SHOW)
            SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.CMD_HOME, player, null);
        if (SetHome.getInstance().configUtils.EXTRA_PLAY_WARP_SOUND)
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
    }

    public void deletePlayerHome(Player player) {
        if (!homeExists(player, true)) return;
        getHomeYaml(player).set(PATH_X, null);
        getHomeYaml(player).set(PATH_Y, null);
        getHomeYaml(player).set(PATH_Z, null);
        getHomeYaml(player).set(PATH_YAW, null);
        getHomeYaml(player).set(PATH_PITCH, null);
        getHomeYaml(player).set(PATH_WORLD, null);
        saveHomesFile(player);
        if (SetHome.getInstance().configUtils.CMD_DELETEHOME_MESSAGE_SHOW)
            SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.CMD_DELETEHOME, player, null);
    }

    private void saveHomesFile(Player player) {
        try {
            getHomeYaml(player).save(getHomeFile(player));
        } catch (Exception e) {
            SetHome.getInstance().getLogger().log(Level.SEVERE, "Error saving home for " + player.getName() + "!");
            e.printStackTrace();
        }
    }

    private File getHomeFile(Player player) {
        if (!homeFiles.containsKey(player.getUniqueId())) {
            homeFiles.put(player.getUniqueId(), new File(homesFilePath, player.getUniqueId() + ".yml"));
        }
        if (!homeFiles.get(player.getUniqueId()).exists()) {
            try {
                homeFiles.get(player.getUniqueId()).createNewFile();
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return homeFiles.get(player.getUniqueId());
    }

    private YamlConfiguration getHomeYaml(Player player) {
        if (!homeYamls.containsKey(player.getUniqueId())) {
            homeYamls.put(player.getUniqueId(), YamlConfiguration.loadConfiguration(getHomeFile(player)));
        }
        return homeYamls.get(player.getUniqueId());
    }

}