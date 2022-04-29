package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.data.Route;
import com.iwaa.common.data.RouteCreator;
import com.iwaa.common.network.Request;
import com.iwaa.common.network.CommandResult;

import java.io.IOException;

public class Update extends Command {
    public Update() {
        super("update", "обновить значение элемента коллекции, id которого равен заданному", 1);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        try {
            long id = Long.parseLong((String) args[0]);
            CommandResult commandResult = commandAdmin.getNetworkListener().listen(new Request(new Remove(), new Object[]{id}));
            if ("There is no Element with such ID".equals(commandResult.getMessage())) {
                System.out.println("There is no Route with such ID");
                return new Object[0];
            }
            RouteCreator routeCreator = new RouteCreator();
            Route route = routeCreator.createRoute();
            route.setId(id);
            return new Object[]{route};
        } catch (IOException e) {
            System.out.println("Input error.");
        } catch (NumberFormatException e) {
            System.out.println("Invalid format of id.");
        }
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        Route updatedRoute = (Route) args[0];
        commandAdmin.getCollectionAdmin().addWithId(updatedRoute);
        return new CommandResult("Your Route was successfully Updated");
    }
}
