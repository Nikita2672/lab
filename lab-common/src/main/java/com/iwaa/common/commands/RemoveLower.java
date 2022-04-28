package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.data.Route;
import com.iwaa.common.data.RouteCreator;
import com.iwaa.common.network.CommandResult;

import java.io.IOException;

public class RemoveLower extends Command {
    public RemoveLower() {
        super("remove_lower", "удалить из коллекции все элементы, меньшие, чем заданный", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        try {
            RouteCreator routeCreator = new RouteCreator();
            Route comparedRoute = routeCreator.createRoute();
            return new Object[]{comparedRoute};
        } catch (IOException e) {
            System.out.println("Input error.");
        }
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args) {
        int collectionLen = CommandAdmin.getCollectionAdmin().getCollection().size();
        CommandAdmin.getCollectionAdmin().getCollection().removeIf(route -> route.compareTo((Route) args[0]) < 0);
        return new CommandResult((collectionLen - CommandAdmin.getCollectionAdmin().getCollection().size()) + " object(s) was deleted.");
    }
}
