package com.downthepark.sethome.commands;

import com.downthepark.sethome.SetHome;
import com.downthepark.sethome.utilities.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    public enum COMMAND_TYPE {
        SETHOME,
        HOME,
        DELETEHOME
    }

    private HashMap<COMMAND_TYPE, Integer> cooldownTime;
    private HashMap<COMMAND_TYPE, Integer> warmupTime;
    private HashMap<COMMAND_TYPE, Boolean> cooldownInEffect;
    private static HashMap<COMMAND_TYPE, HashMap<UUID, Boolean>> warmupInEffect;
    private HashMap<COMMAND_TYPE, HashMap<UUID, Long>> cooldownSecondsLeft;
    private static HashMap<COMMAND_TYPE, HashMap<UUID, BukkitTask>> warmupTask;

    public CommandExecutor() {
        initializeHashMaps();
    }

    private void initializeHashMaps() {
        cooldownTime = new HashMap<>();
        warmupTime = new HashMap<>();
        cooldownInEffect = new HashMap<>();
        warmupInEffect = new HashMap<>();
        cooldownSecondsLeft = new HashMap<>();
        warmupTask = new HashMap<>();
        cooldownTime.put(COMMAND_TYPE.SETHOME, SetHome.getInstance().configUtils.CMD_SETHOME_COOLDOWN);
        cooldownTime.put(COMMAND_TYPE.HOME, SetHome.getInstance().configUtils.CMD_HOME_COOLDOWN);
        cooldownTime.put(COMMAND_TYPE.DELETEHOME, SetHome.getInstance().configUtils.CMD_DELETEHOME_COOLDOWN);
        warmupTime.put(COMMAND_TYPE.SETHOME, SetHome.getInstance().configUtils.CMD_SETHOME_WARMUP);
        warmupTime.put(COMMAND_TYPE.HOME, SetHome.getInstance().configUtils.CMD_HOME_WARMUP);
        warmupTime.put(COMMAND_TYPE.DELETEHOME, SetHome.getInstance().configUtils.CMD_DELETEHOME_WARMUP);
        cooldownInEffect.put(COMMAND_TYPE.SETHOME, false);
        cooldownInEffect.put(COMMAND_TYPE.HOME, false);
        cooldownInEffect.put(COMMAND_TYPE.DELETEHOME, false);
        warmupInEffect.put(COMMAND_TYPE.SETHOME, null);
        warmupInEffect.put(COMMAND_TYPE.HOME, null);
        warmupInEffect.put(COMMAND_TYPE.DELETEHOME, null);
        cooldownSecondsLeft.put(COMMAND_TYPE.SETHOME, new HashMap<>());
        cooldownSecondsLeft.put(COMMAND_TYPE.HOME, new HashMap<>());
        cooldownSecondsLeft.put(COMMAND_TYPE.DELETEHOME, new HashMap<>());
        warmupTask.put(COMMAND_TYPE.SETHOME, null);
        warmupTask.put(COMMAND_TYPE.HOME, null);
        warmupTask.put(COMMAND_TYPE.DELETEHOME, null);
    }

    public static HashMap<COMMAND_TYPE, HashMap<UUID, Boolean>> getWarmupInEffect() {
        return warmupInEffect;
    }

    public static HashMap<COMMAND_TYPE, HashMap<UUID, BukkitTask>> getWarmupTask() {
        return warmupTask;
    }

    public void executeCmd(COMMAND_TYPE commandType, Player player) {
        if (commandType == COMMAND_TYPE.SETHOME)
            SetHome.getInstance().commands.cmdSetHome(player);
        else if (commandType == COMMAND_TYPE.HOME)
            SetHome.getInstance().commands.cmdHome(player);
        else if (commandType == COMMAND_TYPE.DELETEHOME)
            SetHome.getInstance().commands.cmdDeleteHome(player);
    }

    public boolean executeCooldown(HashMap<UUID, Long> cooldownMap, Player player, int seconds) {
        if (cooldownMap.containsKey(player.getUniqueId())) {
            long secondsLeft = ((cooldownMap.get(player.getUniqueId()) / 1000) + seconds) - (System.currentTimeMillis() / 1000);
            if (secondsLeft > 0) {
                SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.COOLDOWN, player, (int) secondsLeft);
                return true;
            }
        }
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
        return false;
    }

    public void executeWarmup(CommandExecutor.COMMAND_TYPE commandType, Player player, int seconds) {
        SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.WARMUP, player, seconds);
        HashMap<UUID, Boolean> running = new HashMap<>();
        running.put(player.getUniqueId(), false);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                executeCmd(commandType, player);
                warmupInEffect.put(commandType, running);
            }
        };
        running.put(player.getUniqueId(), true);
        warmupInEffect.put(commandType, running);
        HashMap<UUID, BukkitTask> task = new HashMap<>();
        task.put(player.getUniqueId(), runnable.runTaskLater(SetHome.getInstance(), 20L * seconds));
        warmupTask.put(commandType, task);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.DENY_CONSOLE, sender, null);
            return false;
        }

        Player player = (Player) sender;
        COMMAND_TYPE commandType = null;

        if (command.getName().equals("sethome")) {
            commandType = COMMAND_TYPE.SETHOME;
        }
        else if (command.getName().equals("home")) {
            commandType = COMMAND_TYPE.HOME;
        }
        else if (command.getName().equals("deletehome")) {
            commandType = COMMAND_TYPE.DELETEHOME;
        }

        int cooldownSeconds = cooldownTime.get(commandType);
        int warmupSeconds = warmupTime.get(commandType);
        // Both cooldown and warmup enabled
        if (cooldownSeconds > 0 && warmupSeconds > 0) {
            cooldownInEffect.put(commandType, executeCooldown(cooldownSecondsLeft.get(commandType), player, cooldownSeconds));
            if (cooldownInEffect.get(commandType))
                return false;
            executeWarmup(commandType, player, warmupSeconds);
        }
        // Just cooldown enabled
        else if (cooldownSeconds > 0) {
            cooldownInEffect.put(commandType, executeCooldown(cooldownSecondsLeft.get(commandType), player, cooldownSeconds));
            if (cooldownInEffect.get(commandType))
                return false;
            executeCmd(commandType, player);
        }
        // Just warmup enabled
        else if (warmupSeconds > 0) {
            executeWarmup(commandType, player, warmupSeconds);
        // Both cooldown and warmup disabled
        } else {
            executeCmd(commandType, player);
        }

        return false;
    }

}
