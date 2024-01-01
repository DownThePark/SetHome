package com.downthepark.sethome.converters;

import com.downthepark.sethome.SetHome;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;

public class ConfigManipulation {

    private final SetHome instance;
    private final Path configPath;

    public ConfigManipulation(SetHome instance) {
        this.instance = instance;
        configPath = Path.of(instance.getDataFolder() + "/config.yml");
    }

    public boolean oldConfigExists(String oldSetting) {
        if (oldSetting != null) {
            return true;
        } else {
            return false;
        }
    }

    public void backupOldConfig(Path destination) {
        if (Files.exists(destination)) return;
        instance.getLogger().log(Level.INFO, "Old configuration found! Backing up to: " + destination);
        try {
            Files.copy(configPath, destination);
            instance.getLogger().log(Level.INFO, "Old configuration successfully backed up to: " + destination);
        }
        catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "An error occurred while trying to backup " + configPath + " - Please manually backup this file.");
            throw new RuntimeException(e);
        }
    }

    public void createNewConfig() {
        instance.saveResource(configPath.getFileName().toString(), true);
        instance.reloadConfig();
    }

}
