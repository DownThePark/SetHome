package com.downthepark.sethome.utilities;

import com.downthepark.sethome.SetHome;

public class ConfigUtils {

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

    // Settings for /delhome
    public boolean CMD_DELHOME_MESSAGE_SHOW;
    public int CMD_DELHOME_COOLDOWN;
    public int CMD_DELHOME_WARMUP;
    public boolean CMD_DELHOME_WARMUP_CANCEL_ON_MOVE;

    // Extra settings
    public boolean EXTRA_PLAY_WARP_SOUND;
    public boolean EXTRA_RESPAWN_AT_HOME;
    public boolean EXTRA_CHECK_UPDATES;

    // Config settings
    public boolean CONFIG_AUTO_RELOAD;
    public int CONFIG_AUTO_RELOAD_INTERVAL;

    // Settings for messages
    public String MESSAGE_CMD_SETHOME;
    public String MESSAGE_CMD_HOME;
    public String MESSAGE_CMD_DELHOME;
    public String MESSAGE_MISSING_HOME;
    public String MESSAGE_MISSING_WORLD;
    public String MESSAGE_COOLDOWN;
    public String MESSAGE_WARMUP;
    public String MESSAGE_ON_MOVE;
    public String MESSAGE_DENY_CONSOLE;

    public ConfigUtils(SetHome instance) {
        CMD_SETHOME_MESSAGE_SHOW = instance.getConfig().getBoolean("cmd-sethome-message-show");
        CMD_SETHOME_COOLDOWN = instance.getConfig().getInt("cmd-sethome-cooldown");
        CMD_SETHOME_WARMUP = instance.getConfig().getInt("cmd-sethome-warmup");
        CMD_SETHOME_WARMUP_CANCEL_ON_MOVE = instance.getConfig().getBoolean("cmd-sethome-warmup-cancel-on-move");
        CMD_HOME_MESSAGE_SHOW = instance.getConfig().getBoolean("cmd-home-message-show");
        CMD_HOME_COOLDOWN = instance.getConfig().getInt("cmd-home-cooldown");
        CMD_HOME_WARMUP = instance.getConfig().getInt("cmd-home-warmup");
        CMD_HOME_WARMUP_CANCEL_ON_MOVE = instance.getConfig().getBoolean("cmd-home-warmup-cancel-on-move");
        CMD_DELHOME_MESSAGE_SHOW = instance.getConfig().getBoolean("cmd-delhome-message-show");
        CMD_DELHOME_COOLDOWN = instance.getConfig().getInt("cmd-delhome-cooldown");
        CMD_DELHOME_WARMUP = instance.getConfig().getInt("cmd-delhome-warmup");
        CMD_DELHOME_WARMUP_CANCEL_ON_MOVE = instance.getConfig().getBoolean("cmd-delhome-warmup-cancel-on-move");
        EXTRA_PLAY_WARP_SOUND = instance.getConfig().getBoolean("extra-play-warp-sound");
        EXTRA_RESPAWN_AT_HOME = instance.getConfig().getBoolean("extra-respawn-at-home");
        EXTRA_CHECK_UPDATES = instance.getConfig().getBoolean("extra-check-updates");
        CONFIG_AUTO_RELOAD = instance.getConfig().getBoolean("config-auto-reload");
        CONFIG_AUTO_RELOAD_INTERVAL = instance.getConfig().getInt("config-auto-reload-interval");
        MESSAGE_CMD_SETHOME = instance.getConfig().getString("message-cmd-sethome");
        MESSAGE_CMD_HOME = instance.getConfig().getString("message-cmd-home");
        MESSAGE_CMD_DELHOME = instance.getConfig().getString("message-cmd-delhome");
        MESSAGE_MISSING_HOME = instance.getConfig().getString("message-missing-home");
        MESSAGE_MISSING_WORLD = instance.getConfig().getString("message-missing-world");
        MESSAGE_COOLDOWN = instance.getConfig().getString("message-cooldown");
        MESSAGE_WARMUP = instance.getConfig().getString("message-warmup");
        MESSAGE_ON_MOVE = instance.getConfig().getString("message-on-move");
        MESSAGE_DENY_CONSOLE = instance.getConfig().getString("message-deny-console");
    }

}
