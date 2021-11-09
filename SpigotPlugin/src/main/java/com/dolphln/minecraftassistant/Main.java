package com.dolphln.minecraftassistant;

import com.dolphln.minecraftassistant.commands.LinkCommand;
import com.dolphln.minecraftassistant.core.CommandRunner;
import com.dolphln.minecraftassistant.files.ConfigFile;
import com.dolphln.minecraftassistant.socket.SocketServer;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main instance;

    private ConfigFile configFile;

    private SocketServer socketServer;
    private CommandRunner commandRunner;

    @Override
    public void onEnable() {
        instance = this;

        this.configFile = new ConfigFile(this);

        System.out.println("Staring socket at port " + getConfigFile().getConfig().getInt("port"));
        this.socketServer = new SocketServer(this);
        System.out.println("Socket started");

        this.commandRunner = new CommandRunner(this);

        getCommand("link").setExecutor(new LinkCommand(this));

        this.commandRunner.start();
    }

    @Override
    public void onDisable() {
        this.socketServer.close();

        this.commandRunner.stop();
    }

    public void sendMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6[&eAssistant&6] &f" + message));
    }

    public static Main getInstance() {
        return instance;
    }

    public ConfigFile getConfigFile() {
        return configFile;
    }

    public SocketServer getSocketServer() {
        return socketServer;
    }
}
