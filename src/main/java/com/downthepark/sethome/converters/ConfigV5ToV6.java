package com.downthepark.sethome.converters;

import com.downthepark.sethome.SetHome;

public class ConfigV5ToV6 {

    private SetHome instance;

    public ConfigV5ToV6(SetHome instance) {
        this.instance = instance;
        if (!oldConfigExists()) return;
        backupOldConfig();
        createNewConfig();
        copyV5ToV6();
    }

    private boolean oldConfigExists() {
        return false;
    }

    private void backupOldConfig() {

    }

    private void createNewConfig() {

    }

    private void copyV5ToV6() {

    }

}