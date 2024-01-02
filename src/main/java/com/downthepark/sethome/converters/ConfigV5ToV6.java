package com.downthepark.sethome.converters;

import com.downthepark.sethome.SetHome;
import org.bukkit.configuration.file.YamlConfiguration;

import java.nio.file.Path;

public class ConfigV5ToV6 {

    public ConfigV5ToV6() {
        Path backupPath = Path.of(SetHome.getInstance().getDataFolder() + "/config.yml.v5.backup");
        if (!SetHome.getInstance().configManipulation.oldConfigExists(SetHome.getInstance().getConfig().getString("sethome-message"))) return;
        SetHome.getInstance().configManipulation.backupOldConfig(backupPath);
        SetHome.getInstance().configManipulation.createNewConfig();
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

        SetHome.getInstance().getConfig().set("cmd-sethome-message-show", oldCmdSetHomeMessageShow);
        SetHome.getInstance().getConfig().set("cmd-home-message-show", oldCmdHomeMessageShow);
        SetHome.getInstance().getConfig().set("extra-play-warp-sound", oldExtraPlayWarpSound);
        SetHome.getInstance().getConfig().set("extra-respawn-at-home", oldExtraRespawnAtHome);
        SetHome.getInstance().getConfig().set("message-cmd-sethome", oldMessageCmdSetHome);
        SetHome.getInstance().getConfig().set("message-cmd-home", oldMessageCmdHome);

        SetHome.getInstance().saveConfig();
        SetHome.getInstance().configUtils.reloadConfig();
    }

}