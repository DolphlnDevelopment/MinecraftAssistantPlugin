package com.dolphln.minecraftassistant.socket;

import com.dolphln.minecraftassistant.Main;
import com.dolphln.minecraftassistant.models.VoiceCommand;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class SocketServer {

    private final Main plugin;

    private ServerSocket serverSocket;
    private final ArrayList<VoiceCommand> commandsToDispatch;

    private final HashMap<String, ConnectedClient> connectedClients;

    private Thread listeningThread;

    public SocketServer(Main plugin) {
        this.plugin = plugin;

        this.commandsToDispatch = new ArrayList<>();

        this.connectedClients = new HashMap<>();

        try {
            this.serverSocket = new ServerSocket(plugin.getConfigFile().getConfig().getInt("port"));
            this.listen();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void listen() {
        AtomicInteger count = new AtomicInteger();
        this.listeningThread = new Thread(() -> {
            while (this.listeningThread != null) {
                try {
                    Socket newConnection = serverSocket.accept();
                    System.out.println("New connection made");
                    ConnectedClient client = new ConnectedClient(commandsToDispatch, newConnection);

                    String name = "client-" + count;
                    client.setName(name);
                    connectedClients.put(name, client);
                    client.start();
                    count.getAndIncrement();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        this.listeningThread.setName("listening");
        this.listeningThread.start();

        new BukkitRunnable() {
            @Override
            public void run() {
                HashMap<String, ConnectedClient> connectedClientHashMap = connectedClients;
                connectedClientHashMap.forEach((s, client) -> {
                    if (client.isClosed()) {
                        connectedClients.remove(s);
                    }
                });
            }
        }.runTaskTimer(plugin, 60 * 20L, 60 * 20L);
    }

    public void close() {

        /*
        [15:02:30] [Server thread/INFO]: [MinecraftAssistant] Disabling MinecraftAssistant v0.1
        [15:02:30] [Server thread/WARN]: java.net.SocketException: Socket is closed
        [15:02:30] [Server thread/WARN]: 	at java.base/java.net.Socket.getInputStream(Socket.java:937)
        [15:02:30] [Server thread/WARN]: 	at com.dolphln.minecraftassistant.socket.ConnectedClient.createStreams(ConnectedClient.java:82)
        [15:02:30] [Server thread/WARN]: 	at com.dolphln.minecraftassistant.socket.ConnectedClient.close(ConnectedClient.java:130)
        [15:02:30] [Server thread/WARN]: 	at com.dolphln.minecraftassistant.socket.ConnectedClient.closeSync(ConnectedClient.java:124)
        [15:02:30] [Server thread/WARN]: 	at java.base/java.util.HashMap$Values.forEach(HashMap.java:1067)
        [15:02:30] [Server thread/WARN]: 	at com.dolphln.minecraftassistant.socket.SocketServer.close(SocketServer.java:77)
        [15:02:30] [Server thread/WARN]: 	at com.dolphln.minecraftassistant.Main.onDisable(Main.java:39)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.plugin.java.JavaPlugin.setEnabled(JavaPlugin.java:265)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.plugin.java.JavaPluginLoader.disablePlugin(JavaPluginLoader.java:405)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.plugin.SimplePluginManager.disablePlugin(SimplePluginManager.java:533)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.plugin.SimplePluginManager.disablePlugins(SimplePluginManager.java:519)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.plugin.SimplePluginManager.disablePlugins(SimplePluginManager.java:512)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.craftbukkit.v1_17_R1.CraftServer.disablePlugins(CraftServer.java:452)
        [15:02:30] [Server thread/WARN]: 	at net.minecraft.server.MinecraftServer.stop(MinecraftServer.java:1032)
        [15:02:30] [Server thread/WARN]: 	at net.minecraft.server.dedicated.DedicatedServer.stop(DedicatedServer.java:819)
        [15:02:30] [Server thread/WARN]: 	at net.minecraft.server.MinecraftServer.x(MinecraftServer.java:1326)
        [15:02:30] [Server thread/WARN]: 	at net.minecraft.server.MinecraftServer.lambda$spin$0(MinecraftServer.java:319)
        [15:02:30] [Server thread/WARN]: 	at java.base/java.lang.Thread.run(Thread.java:831)
        [15:02:30] [client-2/WARN]: java.net.SocketException: Socket closed
        [15:02:30] [client-2/WARN]: 	at java.base/sun.nio.ch.NioSocketImpl.endRead(NioSocketImpl.java:248)
        [15:02:30] [client-2/WARN]: 	at java.base/sun.nio.ch.NioSocketImpl.implRead(NioSocketImpl.java:327)
        [15:02:30] [client-2/WARN]: 	at java.base/sun.nio.ch.NioSocketImpl.read(NioSocketImpl.java:350)
        [15:02:30] [client-2/WARN]: 	at java.base/sun.nio.ch.NioSocketImpl$1.read(NioSocketImpl.java:803)
        [15:02:30] [client-2/WARN]: 	at java.base/java.net.Socket$SocketInputStream.read(Socket.java:976)
        [15:02:30] [client-2/WARN]: 	at java.base/java.net.Socket$SocketInputStream.read(Socket.java:971)
        [15:02:30] [client-2/WARN]: 	at java.base/java.io.DataInputStream.readByte(DataInputStream.java:271)
        [15:02:30] [client-2/WARN]: 	at com.dolphln.minecraftassistant.socket.ConnectedClient.run(ConnectedClient.java:47)
        [15:02:30] [Server thread/WARN]: java.net.SocketException: Socket is closed
        [15:02:30] [Server thread/WARN]: 	at java.base/java.net.Socket.getOutputStream(Socket.java:1008)
        [15:02:30] [Server thread/WARN]: 	at com.dolphln.minecraftassistant.socket.ConnectedClient.createStreams(ConnectedClient.java:90)
        [15:02:30] [Server thread/WARN]: 	at com.dolphln.minecraftassistant.socket.ConnectedClient.close(ConnectedClient.java:130)
        [15:02:30] [Server thread/WARN]: 	at com.dolphln.minecraftassistant.socket.ConnectedClient.closeSync(ConnectedClient.java:124)
        [15:02:30] [Server thread/WARN]: 	at java.base/java.util.HashMap$Values.forEach(HashMap.java:1067)
        [15:02:30] [Server thread/WARN]: 	at com.dolphln.minecraftassistant.socket.SocketServer.close(SocketServer.java:77)
        [15:02:30] [Server thread/WARN]: 	at com.dolphln.minecraftassistant.Main.onDisable(Main.java:39)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.plugin.java.JavaPlugin.setEnabled(JavaPlugin.java:265)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.plugin.java.JavaPluginLoader.disablePlugin(JavaPluginLoader.java:405)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.plugin.SimplePluginManager.disablePlugin(SimplePluginManager.java:533)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.plugin.SimplePluginManager.disablePlugins(SimplePluginManager.java:519)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.plugin.SimplePluginManager.disablePlugins(SimplePluginManager.java:512)
        [15:02:30] [Server thread/WARN]: 	at org.bukkit.craftbukkit.v1_17_R1.CraftServer.disablePlugins(CraftServer.java:452)
        [15:02:30] [Server thread/WARN]: 	at net.minecraft.server.MinecraftServer.stop(MinecraftServer.java:1032)
        [15:02:30] [Server thread/WARN]: 	at net.minecraft.server.dedicated.DedicatedServer.stop(DedicatedServer.java:819)
        [15:02:30] [Server thread/WARN]: 	at net.minecraft.server.MinecraftServer.x(MinecraftServer.java:1326)
        [15:02:30] [Server thread/WARN]: 	at net.minecraft.server.MinecraftServer.lambda$spin$0(MinecraftServer.java:319)
        [15:02:30] [Server thread/WARN]: 	at java.base/java.lang.Thread.run(Thread.java:831)
        [15:02:30] [Server thread/ERROR]: Error occurred while disabling MinecraftAssistant v0.1 (Is it up to date?)
        java.lang.NullPointerException: Cannot invoke "java.io.DataOutputStream.writeByte(int)" because "this.dataOutputStream" is null
            at com.dolphln.minecraftassistant.socket.ConnectedClient.close(ConnectedClient.java:131) ~[?:?]
            at com.dolphln.minecraftassistant.socket.ConnectedClient.closeSync(ConnectedClient.java:124) ~[?:?]
            at java.util.HashMap$Values.forEach(HashMap.java:1067) ~[?:?]
            at com.dolphln.minecraftassistant.socket.SocketServer.close(SocketServer.java:77) ~[?:?]
            at com.dolphln.minecraftassistant.Main.onDisable(Main.java:39) ~[?:?]
            at org.bukkit.plugin.java.JavaPlugin.setEnabled(JavaPlugin.java:265) ~[patched_1.17.1.jar:git-Purpur-1287]
            at org.bukkit.plugin.java.JavaPluginLoader.disablePlugin(JavaPluginLoader.java:405) ~[patched_1.17.1.jar:git-Purpur-1287]
            at org.bukkit.plugin.SimplePluginManager.disablePlugin(SimplePluginManager.java:533) ~[patched_1.17.1.jar:git-Purpur-1287]
            at org.bukkit.plugin.SimplePluginManager.disablePlugins(SimplePluginManager.java:519) ~[patched_1.17.1.jar:git-Purpur-1287]
            at org.bukkit.plugin.SimplePluginManager.disablePlugins(SimplePluginManager.java:512) ~[patched_1.17.1.jar:git-Purpur-1287]
            at org.bukkit.craftbukkit.v1_17_R1.CraftServer.disablePlugins(CraftServer.java:452) ~[patched_1.17.1.jar:git-Purpur-1287]
            at net.minecraft.server.MinecraftServer.stopServer(MinecraftServer.java:1032) ~[patched_1.17.1.jar:git-Purpur-1287]
            at net.minecraft.server.dedicated.DedicatedServer.stopServer(DedicatedServer.java:819) ~[patched_1.17.1.jar:git-Purpur-1287]
            at net.minecraft.server.MinecraftServer.runServer(MinecraftServer.java:1326) ~[patched_1.17.1.jar:git-Purpur-1287]
            at net.minecraft.server.MinecraftServer.lambda$spin$0(MinecraftServer.java:319) ~[patched_1.17.1.jar:git-Purpur-1287]
            at java.lang.Thread.run(Thread.java:831) [?:?]
         */

        connectedClients.values().forEach(ConnectedClient::closeSync);
        this.listeningThread = null;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void linkCode(UUID player, int code) {
        this.connectedClients.forEach((s, client) -> {
            client.linkPlayer(player, code);
        });
    }

    public VoiceCommand getNextCommand() {
        if (commandsToDispatch.size() == 0) {
            return null;
        }
        return commandsToDispatch.remove(0);
    }
}
