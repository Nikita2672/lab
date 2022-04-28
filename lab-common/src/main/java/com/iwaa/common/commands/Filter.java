package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;

public class Filter extends Command {
    public Filter() {
        super("filter_less_than_distance", "вывести элементы, значение поля distance которых меньше заданного", 1);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        try {
            return new Object[]{Long.parseLong((String) args[0])};
        } catch (NumberFormatException e) {
            System.out.println("Please use (Long) distance");
        }
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args) {
        return new CommandResult(CommandAdmin.getCollectionAdmin().outFilter((Long) args[0]));
    }
}
