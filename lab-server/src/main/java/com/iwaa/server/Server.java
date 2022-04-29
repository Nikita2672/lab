package com.iwaa.server;

import com.iwaa.common.controllers.CommandListener;
import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.state.State;
import com.iwaa.server.collection.CollectionAdminImpl;
import com.iwaa.server.config.IOConfigurator;

import java.io.IOException;

public final class Server {

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        IOConfigurator ioConfigurator = new IOConfigurator();
        if (ioConfigurator.configure()) {
            CollectionAdminImpl collectionAdmin = CollectionAdminImpl.configFromFile(IOConfigurator.COLLECTION_FILE_READER,
                    ioConfigurator.getInputFile());
            collectionAdmin.setIoConfigurator(ioConfigurator);
            CommandAdmin commandAdmin = new CommandAdmin();
            commandAdmin.setCollectionAdmin(collectionAdmin);
            try {
                State state = new State();
                ServerConnectAdminImpl serverConnectAdminImpl = new ServerConnectAdminImpl(commandAdmin, state);
                commandAdmin.setServerConnectAdmin(serverConnectAdminImpl);
                Thread commandListenerThread = new Thread(new CommandListener(commandAdmin, state));
                commandListenerThread.start();
                serverConnectAdminImpl.run();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
