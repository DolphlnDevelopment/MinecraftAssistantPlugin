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
    private ArrayList<VoiceCommand> commandsToDispatch;

    private HashMap<String, ConnectedClient> connectedClients;

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
            while (true) {
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
        }.runTaskTimer(plugin, 60*20L, 60*20L);
    }

    public void close() {
        connectedClients.values().forEach(ConnectedClient::closeSync);
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
