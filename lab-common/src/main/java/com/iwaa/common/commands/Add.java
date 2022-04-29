package com.iwaa.common.commands;


import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.data.Route;
import com.iwaa.common.data.RouteCreator;
import com.iwaa.common.network.CommandResult;

import java.io.IOException;

public class Add extends Command {
    public Add() {
        super("add", "добавить новый элемент в коллекцию", 0);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        try {
            RouteCreator routeCreator = new RouteCreator();
            Route routeToAdd = routeCreator.createRoute();
            return new Object[]{routeToAdd};
        } catch (IOException e) {
            System.out.println("Input error.");
            return new Object[0];
        }
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        Route routeToAdd = (Route) args[0];
        commandAdmin.getCollectionAdmin().add(routeToAdd);
        return new CommandResult("your Route was successfully added");
    }
}
