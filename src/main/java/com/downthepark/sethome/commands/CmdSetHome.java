package com.downthepark.sethome.commands;

import com.downthepark.sethome.SetHome;
import com.downthepark.sethome.utilities.MessageUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.UUID;

public class CmdSetHome implements CommandExecutor {

    private final SetHome instance;
    private final int cooldownTime;
    private final int warmupTime;
    private static HashMap<UUID, Long> cooldownMap;
    private static boolean warmupInEffect;
    private static boolean cooldownInEffect;
    private static BukkitTask warmupTask;

    public CmdSetHome(SetHome instance) {
        this.instance = instance;
        cooldownTime = instance.configUtils.CMD_SETHOME_COOLDOWN;
        warmupTime = instance.configUtils.CMD_SETHOME_WARMUP;
        cooldownMap = new HashMap<>();
        warmupInEffect = false;
        cooldownInEffect = false;
        warmupTask = null;
    }

    public static HashMap<UUID, Long> getCooldownMap() {
        return CmdSetHome.cooldownMap;
    }

    public static boolean isWarmupInEffect() {
        return warmupInEffect;
    }

    public static void setWarmupInEffect(boolean warmupInEffect) {
        CmdSetHome.warmupInEffect = warmupInEffect;
    }

    public boolean isCooldownInEffect() {
        return cooldownInEffect;
    }

    public void setCooldownInEffect(boolean cooldownInEffect) {
        CmdSetHome.cooldownInEffect = cooldownInEffect;
    }

    public static BukkitTask getWarmupTask() {
        return warmupTask;
    }

    public static void setWarmupTask(BukkitTask warmupTask) {
        CmdSetHome.warmupTask = warmupTask;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.DENY_CONSOLE, sender, null);
            return false;
        }

        Player player = (Player) sender;

        // Both cooldown and warmup enabled
        if (cooldownTime > 0 && warmupTime > 0) {
            if (warmupTime > cooldownTime)
                setCooldownInEffect(instance.commandUtils.executeCooldown(getCooldownMap(), player, warmupTime));
            else
                setCooldownInEffect(instance.commandUtils.executeCooldown(getCooldownMap(), player, cooldownTime));
            if (isCooldownInEffect())
                return false;
            executeWarmup(player, warmupTime);
        }
        // Just cooldown enabled
        else if (cooldownTime > 0 && warmupTime == 0) {
            setCooldownInEffect(instance.commandUtils.executeCooldown(getCooldownMap(), player, warmupTime));
            if (isCooldownInEffect())
                return false;
            executeCmd(player);
        }
        // Just warmup enabled
        else if (cooldownTime == 0 && warmupTime > 0) {
            setCooldownInEffect(instance.commandUtils.executeCooldown(getCooldownMap(), player, warmupTime));
            if (isCooldownInEffect())
                return false;
            executeWarmup(player, warmupTime);
        }

        return false;
    }

    private void executeWarmup(Player player, int seconds) {
        instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.WARMUP, player, seconds);
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                executeCmd(player);
                setWarmupInEffect(false);
            }
        };
        setWarmupTask(runnable.runTaskLater(instance, 20L * seconds));
        setWarmupInEffect(true);
    }

    private void executeCmd(Player player) {
        instance.homesUtils.setPlayerHome(player);
        if (instance.configUtils.CMD_SETHOME_MESSAGE_SHOW) {
            instance.messageUtils.displayMessage(MessageUtils.MESSAGE_TYPE.CMD_SETHOME, player, null);
        }
    }

}
