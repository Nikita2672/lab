package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.data.Route;
import com.iwaa.common.data.RouteCreator;
import com.iwaa.common.network.CommandResult;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;

public class AddIfMax extends Command {
    public AddIfMax() {
        super("add_if_max", "добавить новый элемент в коллекцию, если его значение превышает значение наибольшего элемента этой коллекции", 0);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        try {
            RouteCreator routeCreator = new RouteCreator(commandAdmin.getCommandListener().getReader());
            if (commandAdmin.getCommandListener().getReader().getClass() == FileReader.class) {
                routeCreator.setFileManager(commandAdmin.getCommandListener().getFileManager());
            }
            Route routeToAdd = routeCreator.createRoute();
            routeToAdd.setId(1L);
            return new Object[]{routeToAdd};
        } catch (IOException e) {
            System.out.println("Input error.");
            return new Object[0];
        }
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        Route newRoute = (Route) args[0];
        if (!commandAdmin.getCollectionAdmin().getCollection().isEmpty()) {
            if (newRoute.compareTo(Collections.max(commandAdmin.getCollectionAdmin().getCollection())) > 0) {
                commandAdmin.getCollectionAdmin().add(newRoute);
                return new CommandResult("Your element was successfully added");
            } else {
                return new CommandResult("Your element was not max");
            }
        } else {
            commandAdmin.getCollectionAdmin().add(newRoute);
            return new CommandResult("Your element was successfully added");
        }
    }
}
