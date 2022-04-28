package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;
public class Info extends Command {
    public Info() {
        super("info", "вывести в стандартный поток вывода информацию о коллекции "
                + "(тип, дата инициализации, количество элементов и т.д.)", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args) {
        return new CommandResult("Collection info:" + "\n" + CommandAdmin.getCollectionAdmin().getInfo());
    }
}
