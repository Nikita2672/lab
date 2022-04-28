package com.iwaa.client;

import com.iwaa.common.network.NetworkListener;
import com.iwaa.common.network.Request;
import com.iwaa.common.network.CommandResult;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class ClientNetwork implements NetworkListener {
    private static final int TIMEOUT = 10000;
    private final ConnectionHandler connectionHandler;
    private final Reader reader = new InputStreamReader(System.in);

    public ClientNetwork(ConnectionHandler connectionHandler) {
        this.connectionHandler = connectionHandler;
    }

    @Override
    public CommandResult listen(Request request) {
        CommandResult commandResult = null;
        if (!connectionHandler.isOpen()) {
            connectionHandler.openConnection();
        }
        if (connectionHandler.isOpen()) {
            try {
                ClientAdmin.send(request, connectionHandler.getOutputStream());
                connectionHandler.getSocket().setSoTimeout(TIMEOUT);
                commandResult = ClientAdmin.get(connectionHandler.getInputStream(), connectionHandler.getSocket().getReceiveBufferSize());
            } catch (IOException e) {
                System.out.println(e.getMessage());
                connectionHandler.close();
            }
        }
        return commandResult;
    }
}
