package com.downthepark.sethome.converters;

import com.downthepark.sethome.SetHome;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class HomesV61ToV62 {

    private File v61ToV62File;
    YamlConfiguration yaml;

    public HomesV61ToV62() {
        if (converted()) return;
        convert();
    }

    private boolean converted() {
        v61ToV62File = new File(SetHome.getInstance().getDataFolder() + File.separator + "homes", "converted.yml");
        if (v61ToV62File.exists()) {
            return true;
        }
        return false;
    }

    private void convert() {
        File dir = new File(SetHome.getInstance().getDataFolder() + File.separator + "homes");
        File[] directoryListing = dir.listFiles();
        for (File f : directoryListing) {
            //String uuid = f.getName().substring(0, f.getName().length() - 4);
            yaml = YamlConfiguration.loadConfiguration(f);

            if (yaml.getString("World") == null) return;

            double x = yaml.getDouble("X");
            double y = yaml.getDouble("Y");
            double z = yaml.getDouble("Z");
            long yaw = yaml.getLong("Yaw");
            long pitch = yaml.getLong("Pitch");
            String world = yaml.getString("World");

            yaml.set("X", null);
            yaml.set("Y", null);
            yaml.set("Z", null);
            yaml.set("Yaw", null);
            yaml.set("Pitch", null);
            yaml.set("World", null);

            yaml.set(SetHome.getInstance().homeUtils.PATH_X, x);
            yaml.set(SetHome.getInstance().homeUtils.PATH_Y, y);
            yaml.set(SetHome.getInstance().homeUtils.PATH_Z, z);
            yaml.set(SetHome.getInstance().homeUtils.PATH_YAW, yaw);
            yaml.set(SetHome.getInstance().homeUtils.PATH_PITCH, pitch);
            yaml.set(SetHome.getInstance().homeUtils.PATH_WORLD, world);
            try {
                yaml.save(f);
            }
            catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        yaml = YamlConfiguration.loadConfiguration(v61ToV62File);
        yaml.set("v6.Dot1ToDot2", true);
        try {
            yaml.save(v61ToV62File);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
