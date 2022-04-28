package com.iwaa.server;

import com.iwaa.common.controllers.CollectionAdmin;
import com.iwaa.common.controllers.CommandListener;
import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.server.collection.CollectionAdminImpl;
import com.iwaa.server.config.IOConfigurator;

import java.io.IOException;

public final class Server {

    private Server() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        if (IOConfigurator.configure()) {
            CollectionAdmin collectionAdmin = CollectionAdminImpl.configFromFile(IOConfigurator.COLLECTION_FILE_READER,
                    IOConfigurator.getInputFile());
            CommandAdmin.setCollectionAdmin(collectionAdmin);
            try {
                ServerConnectAdmin serverConnectAdmin = new ServerConnectAdmin();
                Thread commandListenerThread = new Thread(new CommandListener());
                commandListenerThread.start();
                serverConnectAdmin.run();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
