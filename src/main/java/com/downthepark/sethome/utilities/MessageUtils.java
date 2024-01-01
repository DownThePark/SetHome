package com.downthepark.sethome.utilities;

import com.downthepark.sethome.SetHome;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public class MessageUtils {

    private final SetHome instance;

    public MessageUtils(SetHome instance) {
        this.instance = instance;
    }

    public enum MESSAGE_TYPE {
        CMD_SETHOME,
        CMD_HOME,
        CMD_DELHOME,
        MISSING_HOME,
        MISSING_WORLD,
        COOLDOWN,
        WARMUP,
        ON_MOVE,
        DENY_CONSOLE
    }

    public void displayMessage(MESSAGE_TYPE messageType, CommandSender sender, Integer seconds) {
        if (messageType == MESSAGE_TYPE.CMD_SETHOME) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_CMD_SETHOME, sender.getName(), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.CMD_HOME) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_CMD_HOME, sender.getName(), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.CMD_DELHOME) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_CMD_DELHOME, sender.getName(), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.MISSING_HOME) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_MISSING_HOME, sender.getName(), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.MISSING_WORLD) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_MISSING_WORLD, sender.getName(), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.COOLDOWN) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_COOLDOWN, sender.getName(), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.WARMUP) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_WARMUP, sender.getName(), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.ON_MOVE) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_ON_MOVE, sender.getName(), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.DENY_CONSOLE) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_DENY_CONSOLE, sender.getName(), Optional.ofNullable(seconds)));
        }
    }

    /*public void displayMessage(CommandSender sender, MESSAGE_TYPE messageType, String playerName, Integer seconds) {
        if (messageType == MESSAGE_TYPE.CMD_SETHOME) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_CMD_SETHOME, Optional.ofNullable(playerName), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.CMD_HOME) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_CMD_HOME, Optional.ofNullable(playerName), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.CMD_DELHOME) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_CMD_DELHOME, Optional.ofNullable(playerName), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.MISSING_HOME) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_MISSING_HOME, Optional.ofNullable(playerName), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.MISSING_WORLD) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_MISSING_WORLD, Optional.ofNullable(playerName), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.COOLDOWN) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_COOLDOWN, Optional.ofNullable(playerName), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.WARMUP) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_WARMUP, Optional.ofNullable(playerName), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.ON_MOVE) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_ON_MOVE, Optional.ofNullable(playerName), Optional.ofNullable(seconds)));
        }
        else if (messageType == MESSAGE_TYPE.DENY_CONSOLE) {
            sender.sendMessage(formatMessage(instance.configUtils.MESSAGE_DENY_CONSOLE, Optional.ofNullable(playerName), Optional.ofNullable(seconds)));
        }
    }*/

    private String formatMessage(String message, String playerName, Optional<Integer> seconds) {
        String formatted;

        if (seconds.isPresent()) {
            formatted = ChatColor.translateAlternateColorCodes('&', message)
                    .replace("%player%", playerName)
                    .replace("%seconds%", String.valueOf(seconds.get()));
        }
        else {
            formatted = ChatColor.translateAlternateColorCodes('&', message)
                    .replace("%player%", playerName);
        }

        return formatted;
    }

}