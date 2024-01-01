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
        DELHOME
    }

    private final SetHome instance;
    HashMap<COMMAND_TYPE, Integer> cooldownTime;
    HashMap<COMMAND_TYPE, Integer> warmupTime;
    HashMap<COMMAND_TYPE, Boolean> cooldownInEffect;
    HashMap<COMMAND_TYPE, Boolean> warmupInEffect;
    HashMap<COMMAND_TYPE, HashMap<UUID, Long>> cooldownSeconds;
    HashMap<COMMAND_TYPE, BukkitTask> warmupTask;

    public CommandExecutor(SetHome instance) {
        this.instance = instance;
        initializeHashMaps();
    }

    private void initializeHashMaps() {
        cooldownTime = new HashMap<>();
        warmupTime = new HashMap<>();
        cooldownInEffect = new HashMap<>();
        warmupInEffect = new HashMap<>();
        cooldownSeconds = new HashMap<>();
        warmupTask = new HashMap<>();
        cooldownTime.put(CommandExecutor.COMMAND_TYPE.SETHOME, instance.configUtils.CMD_SETHOME_COOLDOWN);
        cooldownTime.put(CommandExecutor.COMMAND_TYPE.HOME, instance.configUtils.CMD_HOME_COOLDOWN);
        cooldownTime.put(CommandExecutor.COMMAND_TYPE.DELHOME, instance.configUtils.CMD_DELHOME_COOLDOWN);
        warmupTime.put(CommandExecutor.COMMAND_TYPE.SETHOME, instance.configUtils.CMD_SETHOME_WARMUP);
        warmupTime.put(CommandExecutor.COMMAND_TYPE.HOME, instance.configUtils.CMD_HOME_WARMUP);
        warmupTime.put(CommandExecutor.COMMAND_TYPE.DELHOME, instance.configUtils.CMD_DELHOME_WARMUP);
        cooldownInEffect.put(CommandExecutor.COMMAND_TYPE.SETHOME, false);
        cooldownInEffect.put(CommandExecutor.COMMAND_TYPE.HOME, false);
        cooldownInEffect.put(CommandExecutor.COMMAND_TYPE.DELHOME, false);
        warmupInEffect.put(CommandExecutor.COMMAND_TYPE.SETHOME, false);
        warmupInEffect.put(CommandExecutor.COMMAND_TYPE.HOME, false);
        warmupInEffect.put(CommandExecutor.COMMAND_TYPE.DELHOME, false);
        cooldownSeconds.put(CommandExecutor.COMMAND_TYPE.SETHOME, new HashMap<>());
        cooldownSeconds.put(CommandExecutor.COMMAND_TYPE.HOME, new HashMap<>());
        cooldownSeconds.put(CommandExecutor.COMMAND_TYPE.DELHOME, new HashMap<>());
        warmupTask.put(CommandExecutor.COMMAND_TYPE.SETHOME, null);
        warmupTask.put(CommandExecutor.COMMAND_TYPE.HOME, null);
        warmupTask.put(CommandExecutor.COMMAND_TYPE.DELHOME, null);
    }

    public HashMap<COMMAND_TYPE, Boolean> getWarmupInEffect() {
        return this.warmupInEffect;
    }

    public HashMap<COMMAND_TYPE, BukkitTask> getWarmupTask() {
        return this.warmupTask;
    }

    public void executeCmd(CommandExecutor.COMMAND_TYPE commandType, Player player) {
        if (commandType == CommandExecutor.COMMAND_TYPE.SETHOME)
            instance.commands.cmdSetHome(player);
        else if (commandType == CommandExecutor.COMMAND_TYPE.HOME)
            instance.commands.cmdHome(player);
        else if (commandType == CommandExecutor.COMMAND_TYPE.DELHOME)
            instance.commands.cmdDelHome(player);
    }

    public boolean executeCooldown(HashMap<UUID, Long> cooldownMap, Player player, int seconds) {
        if (cooldownMap.containsKey(player.getUniqueId())) {
            long secondsLeft = ((cooldownMap.get(player.getUniqueId()) / 1000) + seconds) - (System.currentTimeMillis() / 1000);
            if (secondsLeft > 0) {
                instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.COOLDOWN, player, (int) secondsLeft);
                return true;
            }
        }
        cooldownMap.put(player.getUniqueId(), System.currentTimeMillis());
        return false;
    }

    public void executeWarmup(CommandExecutor.COMMAND_TYPE commandType, Player player, int seconds) {
        instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.WARMUP, player, seconds);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                executeCmd(commandType, player);
                warmupInEffect.put(commandType, false);
            }
        };
        warmupTask.put(commandType, runnable.runTaskLater(instance, 20L * seconds));
        warmupInEffect.put(commandType, true);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.DENY_CONSOLE, sender, null);
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
        else if (command.getName().equals("delhome")) {
            commandType = COMMAND_TYPE.DELHOME;
        }

        // Both cooldown and warmup enabled
        if (cooldownTime.get(commandType) > 0 && warmupTime.get(commandType) > 0) {
            if (warmupTime.get(commandType) > cooldownTime.get(commandType))
                cooldownInEffect.put(commandType, executeCooldown(cooldownSeconds.get(commandType), player, warmupTime.get(commandType)));
            else
                cooldownInEffect.put(commandType, executeCooldown(cooldownSeconds.get(commandType), player, cooldownTime.get(commandType)));
            if (cooldownInEffect.get(commandType))
                return false;
            executeWarmup(commandType, player, warmupTime.get(commandType));
        }
        // Just cooldown enabled
        else if (cooldownTime.get(commandType) > 0 && warmupTime.get(commandType) == 0) {
            cooldownInEffect.put(commandType, executeCooldown(cooldownSeconds.get(commandType), player, cooldownTime.get(commandType)));
            if (cooldownInEffect.get(commandType))
                return false;
            executeCmd(commandType, player);
        }
        // Just warmup enabled
        else if (cooldownTime.get(commandType) == 0 && warmupTime.get(commandType) > 0) {
            cooldownInEffect.put(commandType, executeCooldown(cooldownSeconds.get(commandType), player, cooldownTime.get(commandType)));
            if (cooldownInEffect.get(commandType))
                return false;
            executeWarmup(commandType, player, warmupTime.get(commandType));
        // Neither cooldown or warmup are enabled
        } else {
            executeCmd(commandType, player);
        }

        return false;
    }

}
