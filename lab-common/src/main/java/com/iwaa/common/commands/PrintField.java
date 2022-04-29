package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;

public class PrintField extends Command {
    public PrintField() {
        super("print_field_descending_distance", "вывести значения поля distance всех элементов в "
                + "порядке убывания", 0);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        return  new CommandResult(commandAdmin.getCollectionAdmin().outFields());
    }
}
