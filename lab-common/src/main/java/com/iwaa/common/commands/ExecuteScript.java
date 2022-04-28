package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandListener;
import com.iwaa.common.network.CommandResult;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ExecuteScript extends Command {
    private static final Set<String> FILE_HISTORY = new HashSet<>();

    public ExecuteScript() {
        super("execute_script", "считать и исполнить скрипт из указанного файла."
                + " В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в "
                + "интерактивном режиме", 1);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        return args;
    }

    @Override
    public CommandResult execute(Object[] args) {
        String fileName = (String) args[0];
        if (FILE_HISTORY.contains(fileName)) {
            return new CommandResult("There is a problem: script will loop.");
        } else {
            try {
                CommandListener listenerFromFile = new CommandListener(new FileReader(fileName));
                FILE_HISTORY.add(fileName);
                listenerFromFile.run();
            } catch (IOException e) {
                return new CommandResult("Wrong opening file");
            }
            FILE_HISTORY.remove(fileName);
        }
        return new CommandResult("Exiting from " + fileName);
    }
}
