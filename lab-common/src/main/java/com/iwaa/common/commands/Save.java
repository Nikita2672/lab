package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;

public class Save extends Command {
    public Save() {
        super("save", "сохранить коллекцию в файл", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args) {
        if (CommandAdmin.getCollectionAdmin().write()) {
            return new CommandResult("Saved successfully!");
        }
        return new CommandResult("There is wrong filename on server, or there is no permissions");
    }
}
