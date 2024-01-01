package com.downthepark.sethome.utilities;

import com.downthepark.sethome.SetHome;

public class ConfigUtils {

    private final SetHome instance;

    // Settings for /sethome
    public boolean CMD_SETHOME_MESSAGE_SHOW;
    public int CMD_SETHOME_COOLDOWN;
    public int CMD_SETHOME_WARMUP;
    public boolean CMD_SETHOME_WARMUP_CANCEL_ON_MOVE;

    // Settings for /home
    public boolean CMD_HOME_MESSAGE_SHOW;
    public int CMD_HOME_COOLDOWN;
    public int CMD_HOME_WARMUP;
    public boolean CMD_HOME_WARMUP_CANCEL_ON_MOVE;

    // Settings for /deletehome
    public boolean CMD_DELETEHOME_MESSAGE_SHOW;
    public int CMD_DELETEHOME_COOLDOWN;
    public int CMD_DELETEHOME_WARMUP;
    public boolean CMD_DELETEHOME_WARMUP_CANCEL_ON_MOVE;

    // Extra settings
    public boolean EXTRA_PLAY_WARP_SOUND;
    public boolean EXTRA_RESPAWN_AT_HOME;
    public boolean EXTRA_CHECK_UPDATES;

    // Settings for messages
    public String MESSAGE_CMD_SETHOME;
    public String MESSAGE_CMD_HOME;
    public String MESSAGE_CMD_DELETEHOME;
    public String MESSAGE_MISSING_HOME;
    public String MESSAGE_MISSING_WORLD;
    public String MESSAGE_COOLDOWN;
    public String MESSAGE_WARMUP;
    public String MESSAGE_ON_MOVE;
    public String MESSAGE_DENY_CONSOLE;

    public ConfigUtils(SetHome instance) {
        this.instance = instance;
        this.reloadConfig();
    }

    public void reloadConfig() {
        instance.reloadConfig();
        CMD_SETHOME_MESSAGE_SHOW = instance.getConfig().getBoolean("cmd-sethome-message-show");
        CMD_SETHOME_COOLDOWN = instance.getConfig().getInt("cmd-sethome-cooldown");
        CMD_SETHOME_WARMUP = instance.getConfig().getInt("cmd-sethome-warmup");
        CMD_SETHOME_WARMUP_CANCEL_ON_MOVE = instance.getConfig().getBoolean("cmd-sethome-warmup-cancel-on-move");
        CMD_HOME_MESSAGE_SHOW = instance.getConfig().getBoolean("cmd-home-message-show");
        CMD_HOME_COOLDOWN = instance.getConfig().getInt("cmd-home-cooldown");
        CMD_HOME_WARMUP = instance.getConfig().getInt("cmd-home-warmup");
        CMD_HOME_WARMUP_CANCEL_ON_MOVE = instance.getConfig().getBoolean("cmd-home-warmup-cancel-on-move");
        CMD_DELETEHOME_MESSAGE_SHOW = instance.getConfig().getBoolean("cmd-deletehome-message-show");
        CMD_DELETEHOME_COOLDOWN = instance.getConfig().getInt("cmd-deletehome-cooldown");
        CMD_DELETEHOME_WARMUP = instance.getConfig().getInt("cmd-deletehome-warmup");
        CMD_DELETEHOME_WARMUP_CANCEL_ON_MOVE = instance.getConfig().getBoolean("cmd-deletehome-warmup-cancel-on-move");
        EXTRA_PLAY_WARP_SOUND = instance.getConfig().getBoolean("extra-play-warp-sound");
        EXTRA_RESPAWN_AT_HOME = instance.getConfig().getBoolean("extra-respawn-at-home");
        EXTRA_CHECK_UPDATES = instance.getConfig().getBoolean("extra-check-updates");
        MESSAGE_CMD_SETHOME = instance.getConfig().getString("message-cmd-sethome");
        MESSAGE_CMD_HOME = instance.getConfig().getString("message-cmd-home");
        MESSAGE_CMD_DELETEHOME = instance.getConfig().getString("message-cmd-deletehome");
        MESSAGE_MISSING_HOME = instance.getConfig().getString("message-missing-home");
        MESSAGE_MISSING_WORLD = instance.getConfig().getString("message-missing-world");
        MESSAGE_COOLDOWN = instance.getConfig().getString("message-cooldown");
        MESSAGE_WARMUP = instance.getConfig().getString("message-warmup");
        MESSAGE_ON_MOVE = instance.getConfig().getString("message-on-move");
        MESSAGE_DENY_CONSOLE = instance.getConfig().getString("message-deny-console");
    }

}
