package com.downthepark.sethome.utilities;

import com.downthepark.sethome.SetHome;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CommandUtils {

    private SetHome instance;

    public CommandUtils(SetHome instance) {
        this.instance = instance;
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

}
