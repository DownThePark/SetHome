package com.downthepark.sethome.utilities;

import com.downthepark.sethome.SetHome;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.function.Consumer;

public class UpdateChecker {

    private final int resourceId;

    public UpdateChecker(int resourceId) {
        this.resourceId = resourceId;
    }

    public void getVersion(final Consumer<String> consumer) {
        if (!SetHome.getInstance().configUtils.EXTRA_CHECK_UPDATES) return;
        Bukkit.getScheduler().runTaskAsynchronously(SetHome.getInstance(), () -> {
            try (InputStream is = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + this.resourceId + "/~").openStream(); Scanner scanner = new Scanner(is)) {
                if (scanner.hasNext()) {
                    consumer.accept(scanner.next());
                }
            } catch (IOException e) {
                SetHome.getInstance().getLogger().info("Unable to check for updates: " + e.getMessage());
            }
        });
    }

}
