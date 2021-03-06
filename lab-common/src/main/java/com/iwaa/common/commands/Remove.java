package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;

public class Remove extends Command {
    public Remove() {
        super("remove_by_id", "удалить элемент из коллекции по его id", 1);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        try {
            return new Object[]{Long.parseLong((String) args[0])};
        } catch (NumberFormatException e) {
            System.out.println("Wrong argument, Please enter integer ID");
        }
        return null;
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        boolean result = commandAdmin.getCollectionAdmin().removeById((Long) args[0]);
        if (result) {
            return new CommandResult("Element with \"" + args[0] + "\" ID was successfully deleted");
        }
        return new CommandResult("There is no Element with such ID");
    }
}
