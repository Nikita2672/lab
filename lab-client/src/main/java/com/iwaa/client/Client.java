package com.iwaa.client;

import com.iwaa.common.controllers.CommandListener;
import com.iwaa.common.controllers.CommandAdmin;

public final class Client {
    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        ConnectionHandler connectionHandler = new ConnectionHandler();
        ClientNetwork clientListener = new ClientNetwork(connectionHandler);
        CommandAdmin.setNetworkListener(clientListener);
        CommandListener commandListener = new CommandListener();
        CommandListener.setOnClient();
        System.out.println("Hello!");
        connectionHandler.openConnection();
        commandListener.run();
    }
}
