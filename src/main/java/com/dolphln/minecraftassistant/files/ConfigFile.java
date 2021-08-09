package com.dolphln.minecraftassistant.files;

import com.dolphln.minecraftassistant.Main;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Level;

public class ConfigFile {

    private final Main plugin;

    private YamlConfiguration config;
    private File configFile;

    public ConfigFile(Main plugin) {
        this.plugin = plugin;

        setup();
    }

    public void setup() {
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdir();
        }

        configFile = new File(plugin.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                plugin.saveResource("config.yml", true);
            } catch (Exception e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create config.yml file.");
            }
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        plugin.getLogger().log(Level.FINE, "File config.yml loaded correctly.");
    }

    public YamlConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return configFile;
    }

    public void reloadConfig() {
        setup();
    }

}
