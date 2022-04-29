package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;

import java.util.stream.Collectors;

public class Help extends Command {
    public Help() {
        super("help", "вывести справку по доступным командам", 0);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        return new CommandResult(CommandAdmin.getCommands()
                .values()
                .stream()
                .map(value -> value.getName() + " - " + value.getDescription())
                .collect(Collectors.joining("\n")));
    }
}
