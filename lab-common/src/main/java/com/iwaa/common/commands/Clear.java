package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;
public class Clear extends Command {
    public Clear() {
        super("clear", "очистить коллекцию", 0);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        commandAdmin.getCollectionAdmin().clear();
        return new CommandResult("Your collection was successfully cleared");
    }
}
