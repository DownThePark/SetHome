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

    private static HashMap<UUID, HashMap<COMMAND_TYPE, Long>> cooldownTask;
    private static HashMap<COMMAND_TYPE, Long> cooldownTaskData;

    private static HashMap<UUID, HashMap<COMMAND_TYPE, Boolean>> warmupInEffect;
    private static HashMap<COMMAND_TYPE, Boolean> warmupInEffectData;
    private static HashMap<UUID, HashMap<COMMAND_TYPE, BukkitTask>> warmupTask;
    private static HashMap<COMMAND_TYPE, BukkitTask> warmupTaskData;

    public CommandExecutor() {
        initializeHashMaps();
    }

    private void initializeHashMaps() {
        cooldownTime = new HashMap<>();
        warmupTime = new HashMap<>();
        cooldownTask = new HashMap<>();
        cooldownTaskData = new HashMap<>();
        warmupInEffect = new HashMap<>();
        warmupInEffectData = new HashMap<>();
        warmupTask = new HashMap<>();
        warmupTaskData = new HashMap<>();
        cooldownTime.put(COMMAND_TYPE.SETHOME, SetHome.getInstance().configUtils.CMD_SETHOME_COOLDOWN);
        cooldownTime.put(COMMAND_TYPE.HOME, SetHome.getInstance().configUtils.CMD_HOME_COOLDOWN);
        cooldownTime.put(COMMAND_TYPE.DELETEHOME, SetHome.getInstance().configUtils.CMD_DELETEHOME_COOLDOWN);
        warmupTime.put(COMMAND_TYPE.SETHOME, SetHome.getInstance().configUtils.CMD_SETHOME_WARMUP);
        warmupTime.put(COMMAND_TYPE.HOME, SetHome.getInstance().configUtils.CMD_HOME_WARMUP);
        warmupTime.put(COMMAND_TYPE.DELETEHOME, SetHome.getInstance().configUtils.CMD_DELETEHOME_WARMUP);
    }

    public static HashMap<UUID, HashMap<COMMAND_TYPE, Long>> getCooldownTask() {
        return cooldownTask;
    }

    public static HashMap<UUID, HashMap<COMMAND_TYPE, Boolean>> getWarmupInEffect() {
        return warmupInEffect;
    }

    public static HashMap<UUID, HashMap<COMMAND_TYPE, BukkitTask>> getWarmupTask() {
        return warmupTask;
    }

    public void executeCmd(Player player, COMMAND_TYPE commandType) {
        if (commandType == COMMAND_TYPE.SETHOME)
            SetHome.getInstance().commands.cmdSetHome(player);
        else if (commandType == COMMAND_TYPE.HOME)
            SetHome.getInstance().commands.cmdHome(player);
        else if (commandType == COMMAND_TYPE.DELETEHOME)
            SetHome.getInstance().commands.cmdDeleteHome(player);
    }

    public boolean executeCooldown(Player player, COMMAND_TYPE commandType, int seconds) {
        if (cooldownTask.containsKey(player.getUniqueId())) {
            if (cooldownTask.get(player.getUniqueId()).containsKey(commandType)) {
                long secondsLeft = ((cooldownTask.get(player.getUniqueId()).get(commandType) / 1000) + seconds) - (System.currentTimeMillis() / 1000);
                if (secondsLeft > 0) {
                    SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.COOLDOWN, player, (int) secondsLeft);
                    return true;
                }
            }
        }
        cooldownTaskData.put(commandType, System.currentTimeMillis());
        cooldownTask.put(player.getUniqueId(), cooldownTaskData);
        return false;
    }

    public void executeWarmup(Player player, COMMAND_TYPE commandType, int seconds) {
        SetHome.getInstance().messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.WARMUP, player, seconds);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                executeCmd(player, commandType);
                warmupInEffect.get(player.getUniqueId()).put(commandType, false);
            }
        };
        warmupInEffectData.put(commandType, true);
        warmupInEffect.put(player.getUniqueId(), warmupInEffectData);
        warmupTaskData.put(commandType, runnable.runTaskLater(SetHome.getInstance(), 20L * seconds));
        warmupTask.put(player.getUniqueId(), warmupTaskData);
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
            boolean running = executeCooldown(player, commandType, cooldownSeconds);
            if (running)
                return false;
            executeWarmup(player, commandType, warmupSeconds);
        }
        // Just cooldown enabled
        else if (cooldownSeconds > 0) {
            boolean running = executeCooldown(player, commandType, cooldownSeconds);
            if (running)
                return false;
            executeCmd(player, commandType);
        }
        // Just warmup enabled
        else if (warmupSeconds > 0) {
            executeWarmup(player, commandType, warmupSeconds);
        }
        // Both cooldown and warmup disabled
        else {
            executeCmd(player, commandType);
        }

        return false;
    }

}
