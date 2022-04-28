package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;

import java.util.Queue;
import java.util.stream.Collectors;

public class History extends Command {
    public History() {
        super("history", "вывести последние 11 команд (без их аргументов)", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args) {
        if (CommandAdmin.getCommandHistory().isEmpty()) {
            return new CommandResult("History is empty.");
        } else {
            Queue<String> history = CommandAdmin.getCommandHistory();
            return new CommandResult("The last 11 commands were:\n" + history.stream().
                    limit(history.size() - 1).
                    collect(Collectors.joining("\n")));
        }
    }
}
