package com.downthepark.sethome.converters;

import com.downthepark.sethome.SetHome;
import org.bukkit.configuration.file.YamlConfiguration;

import java.nio.file.Path;

public class ConfigV5ToV6 {

    private final SetHome instance;

    public ConfigV5ToV6(SetHome instance) {
        this.instance = instance;
        Path backupPath = Path.of(instance.getDataFolder() + "/config.yml.v5.backup");
        if (!instance.configManipulation.oldConfigExists(instance.getConfig().getString("sethome-message"))) return;
        instance.configManipulation.backupOldConfig(backupPath);
        instance.configManipulation.createNewConfig();
        copyV5ToV6(backupPath);
    }

    private void copyV5ToV6(Path backupPath) {
        YamlConfiguration oldConfigYaml = YamlConfiguration.loadConfiguration(backupPath.toFile());

        boolean oldCmdSetHomeMessageShow = oldConfigYaml.getBoolean("show-sethome-message");
        boolean oldCmdHomeMessageShow = oldConfigYaml.getBoolean("show-teleport-message");
        boolean oldExtraPlayWarpSound = oldConfigYaml.getBoolean("play-warp-sound");
        boolean oldExtraRespawnAtHome = oldConfigYaml.getBoolean("respawn-player-at-home");
        String oldMessageCmdSetHome = oldConfigYaml.getString("sethome-message");
        String oldMessageCmdHome = oldConfigYaml.getString("teleport-message");

        instance.getConfig().set("cmd-sethome-message-show", oldCmdSetHomeMessageShow);
        instance.getConfig().set("cmd-home-message-show", oldCmdHomeMessageShow);
        instance.getConfig().set("extra-play-warp-sound", oldExtraPlayWarpSound);
        instance.getConfig().set("extra-respawn-at-home", oldExtraRespawnAtHome);
        instance.getConfig().set("message-cmd-sethome", oldMessageCmdSetHome);
        instance.getConfig().set("message-cmd-home", oldMessageCmdHome);

        instance.saveConfig();
        instance.configUtils.reloadConfig();
    }

}