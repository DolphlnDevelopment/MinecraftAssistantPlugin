package com.dolphln.minecraftassistant.models;

import com.dolphln.minecraftassistant.socket.ConnectedClient;

import java.util.UUID;

public class VoiceCommand {

    private final ConnectedClient client;
    private final String command;
    private final UUID playerUUID;

    public VoiceCommand(ConnectedClient client, String command, UUID playerUUID) {
        this.client = client;
        this.command = command;
        this.playerUUID = playerUUID;
    }

    public ConnectedClient getClient() {
        return client;
    }

    public String getCommand() {
        return command;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    @Override
    public String toString() {
        return "VoiceCommand from " + playerUUID.toString() + ". Command to execute: " + command;
    }
}
