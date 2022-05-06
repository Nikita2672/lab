package com.iwaa.client;

import com.iwaa.common.controllers.CommandListener;
import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.state.State;

import java.util.NoSuchElementException;

public final class Client {
    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        try {
            State state = new State();
            ConnectionHandler connectionHandler = new ConnectionHandler();
            ClientNetwork clientListener = new ClientNetwork(connectionHandler);
            CommandAdmin commandAdmin = new CommandAdmin();
            commandAdmin.setNetworkListener(clientListener);
            CommandListener commandListener = new CommandListener(commandAdmin, state);
            commandAdmin.setCommandListener(commandListener);
            commandListener.setOnClient();
            System.out.println("Hello!");
            connectionHandler.openConnection();
            commandListener.run();
        } catch (NoSuchElementException e) {
            System.out.println("Bye bye");
        }
    }
}
