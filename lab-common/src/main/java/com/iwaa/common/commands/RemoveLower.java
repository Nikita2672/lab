package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.data.Route;
import com.iwaa.common.data.RouteCreator;
import com.iwaa.common.network.CommandResult;

import java.io.FileReader;
import java.io.IOException;

public class RemoveLower extends Command {
    public RemoveLower() {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", 0);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        try {
            RouteCreator routeCreator = new RouteCreator(commandAdmin.getCommandListener().getReader());
            if (commandAdmin.getCommandListener().getReader().getClass() == FileReader.class) {
                routeCreator.setFileManager1(commandAdmin.getCommandListener().getFileManager());
            }
            Route comparedRoute = routeCreator.createRoute();
            return new Object[]{comparedRoute};
        } catch (IOException e) {
            System.out.println("Input error.");
        }
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        int collectionLen = commandAdmin.getCollectionAdmin().getCollection().size();
        commandAdmin.getCollectionAdmin().getCollection().removeIf(route -> route.compareTo((Route) args[0]) < 0);
        return new CommandResult((collectionLen - commandAdmin.getCollectionAdmin().getCollection().size()) + " object(s) was deleted.");
    }
}
