package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;

public class Show extends Command {

    public Show() {
        super("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении", 0);
    }


    @Override
    public Object[] readArgs(Object[] args) {
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args) {
        return new CommandResult(CommandAdmin.getCollectionAdmin().show());
    }
}
