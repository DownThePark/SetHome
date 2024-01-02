package com.downthepark.sethome.converters;

import com.downthepark.sethome.SetHome;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

public class HomesV5ToV6 {

    private final SetHome instance;
    private File sourcePath;
    private File backupPath;

    public HomesV5ToV6(SetHome instance) {
        this.instance = instance;
        sourcePath = new File(instance.getDataFolder(), "Homes.yml");
        backupPath = new File(instance.getDataFolder(), "Homes.yml.v5.backup");
        if (!homesDotYamlExists()) {
            sourcePath = null;
            backupPath = null;
            return;
        }
        //backupHomesDotYaml();
        convertHomes();
    }

    public void clearMemory() {
        //
    }

    private boolean homesDotYamlExists() {
        if (sourcePath.exists()) {
            return true;
        }
        else {
            return false;
        }
    }

    private void backupHomesDotYaml() {
        if (backupPath.exists()) return;
        instance.getLogger().log(Level.INFO, "Old homes file found! Backing up to: " + backupPath);
        try {
            Files.copy(sourcePath.toPath(), backupPath.toPath());
            instance.getLogger().log(Level.INFO, "Old configuration successfully backed up to: " + backupPath);
        }
        catch (IOException e) {
            instance.getLogger().log(Level.SEVERE, "An error occurred while trying to backup " + sourcePath + " - Please manually backup this file.");
            throw new RuntimeException(e);
        }
    }

    private void convertHomes() {
        YamlConfiguration yaml = YamlConfiguration.loadConfiguration(sourcePath);
        Set<String> keys = yaml.getKeys(true);

        HashMap<UUID, HashMap<String, String>> keysAndValues = new HashMap<>();
        HashMap<String, String> pairs = new HashMap<>();
        UUID uuid = null;

        for (String k : keys) {
            if (getDecimals(k) == 1) {
                uuid = UUID.fromString(k.substring(6));
            }
            if (getDecimals(k) == 2) {
                if (k.endsWith(".X") || k.endsWith(".Y") || k.endsWith(".Z")) {
                    pairs.put(k.substring(k.length() - 1), String.valueOf(yaml.getDouble(k)));
                }
                else if (k.endsWith(".Yaw")) {
                    pairs.put(k.substring(k.length() - 3), String.valueOf(yaml.getDouble(k)));
                }
                else if (k.endsWith(".Pitch")) {
                    pairs.put(k.substring(k.length() - 5), String.valueOf(yaml.getDouble(k)));
                }
                else if (k.endsWith(".World")) {
                    pairs.put(k.substring(k.length() - 5), yaml.getString(k));
                }
                System.out.println(pairs.size());
            }
            if (pairs.size() == 6) {
                keysAndValues.put(uuid, pairs);
                pairs = new HashMap<>();
            }
        }

        if (keysAndValues.containsKey(UUID.fromString("708b1d16-927d-4460-95dd-be953a6d4a94"))) {
            System.out.println("true");
        } else {
            System.out.println("false");
        }

        keysAndValues.forEach((k, v) -> System.out.println("Key: " + k + ", Value: " + v.get("X")));

        //sourcePath.delete();
    }

    private int getDecimals(String string) {
        return (int) string.chars().filter(ch -> ch == '.').count();
    }

}