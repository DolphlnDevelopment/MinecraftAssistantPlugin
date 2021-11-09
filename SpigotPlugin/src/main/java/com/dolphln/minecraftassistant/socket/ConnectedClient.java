package com.dolphln.minecraftassistant.socket;

import com.dolphln.minecraftassistant.models.VoiceCommand;
import com.dolphln.minecraftassistant.utils.RandomUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.UUID;

public class ConnectedClient extends Thread {

    private UUID identifier;
    private final int linkingCode;

    private final ArrayList<VoiceCommand> commandsToDispatch;
    private UUID linkedPlayer;

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private boolean done;

    private final Socket socket;

    public ConnectedClient(ArrayList<VoiceCommand> commandsToDispatch, Socket socket) {
        this.commandsToDispatch = commandsToDispatch;
        this.socket = socket;
        this.linkingCode = RandomUtils.getRandomInt(111111, 999999);

        this.done = false;

        try {
            this.socket.setKeepAlive(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        createStreams();

        while (!this.done) {
            try {
                byte messageType = dataInputStream.readByte();

                switch (messageType) {
                    case 1 -> {
                        this.identifier = UUID.fromString(dataInputStream.readUTF());
                        this.dataOutputStream.writeByte(1);
                        this.dataOutputStream.writeUTF(String.valueOf(this.linkingCode));
                        this.dataOutputStream.flush();
                    }
                    case 2 -> {
                        if (linkedPlayer != null) {
                            String command = dataInputStream.readUTF();
                            System.out.println("Got command from " + linkedPlayer + ": " + command);
                            commandsToDispatch.add(new VoiceCommand(this, command, linkedPlayer));
                        }
                    }
                    case -1 -> {
                        System.out.println("-1");
                        finish();
                        done = true;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                finish();
                done = true;
            }
        }

        closeStreams();
    }

    public void createStreams() {
        try {
            if (this.dataInputStream == null) {
                this.dataInputStream = new DataInputStream(this.socket.getInputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (this.dataOutputStream == null) {
                this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void closeStreams() {
        try {
            if (this.dataInputStream != null) {
                this.dataInputStream.close();
            }
            this.dataInputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (this.dataOutputStream != null) {
                this.dataOutputStream.close();
            }
            this.dataOutputStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized void linkPlayer(UUID playerUUID, int linkingCode) {
        if (linkedPlayer == null && linkingCode == this.linkingCode) {
            this.linkedPlayer = playerUUID;
        }
    }

    synchronized void closeSync() {
        close();
    }

    public void close() {
        try {
            closeStreams();
            createStreams();
            dataOutputStream.writeByte(-1);
            dataOutputStream.flush();
            finish();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finish() {
        try {
            closeStreams();
            this.socket.close();
            this.done = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    synchronized boolean isClosed() {
        return this.socket.isClosed();
    }
}
