package com.downthepark.sethome.converters;

import com.downthepark.sethome.SetHome;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

public class HomesV5ToV6 {

    private final SetHome instance;
    private File source;
    private File destination;

    public HomesV5ToV6(SetHome instance) {
        this.instance = instance;
        source = new File(instance.getDataFolder(), "Homes.yml");
        destination = new File(instance.getDataFolder(), "Homes.yml.v5.backup");
        if (!homesDotYamlExists()) {
            source = null;
            destination = null;
            return;
        }
        //backupHomesDotYaml();
        convertHomes();
    }

    private boolean homesDotYamlExists() {
        if (source.exists()) {
            return true;
        }
        else {
            return false;
        }
    }

    private void backupHomesDotYaml() {
        if (destination.exists()) return;
        instance.getLogger().log(Level.INFO, "Old homes file found! Backing up to: " + destination);
        try {
            Files.copy(source.toPath(), destination.toPath());
            instance.getLogger().log(Level.INFO, "Old configuration successfully backed up to: " + destination);
        }
        catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "An error occurred while trying to backup " + source + " - Please manually backup this file.");
            throw new RuntimeException(e);
        }
    }

    private void convertHomes() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(source);
        Set<String> keys = yaml.getKeys(true);

        HashMap<String, String> pairs = new HashMap<>();

        for (String k : keys) {
            if (getDecimals(k) == 2) {
                if (k.endsWith(".X") || k.endsWith(".Y") || k.endsWith(".Z")) {
                    String temp = k.substring(6);
                    //String uuidStr = temp.substring(0, temp.length() - 2);
                    instance.getLogger().info(temp);
                    pairs.put(temp, String.valueOf(yaml.getDouble(k)));
                }
                if (k.endsWith(".Yaw")) {
                    String temp = k.substring(6);
                    instance.getLogger().info(temp);
                    pairs.put(temp, String.valueOf(yaml.getDouble(k)));
                }
                if (k.endsWith(".Pitch")) {
                    String temp = k.substring(6);
                    instance.getLogger().info(temp);
                    pairs.put(temp, String.valueOf(yaml.getDouble(k)));
                }
                if (k.endsWith(".World")) {
                    String temp = k.substring(6);
                    instance.getLogger().info(temp);
                    pairs.put(temp, yaml.getString(k));
                }
            }

        }

        pairs.forEach((k, v) -> System.out.println("key: " + k + " value: " + v));

        //source.delete();
    }

    private int getDecimals(String string) {
        return (int) string.chars().filter(ch -> ch == '.').count();
    }

}
