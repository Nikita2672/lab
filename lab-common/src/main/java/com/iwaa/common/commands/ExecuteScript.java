package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.controllers.CommandListener;
import com.iwaa.common.network.CommandResult;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class ExecuteScript extends Command {
    private final Set<String> fileHistory = new HashSet<>();

    public ExecuteScript() {
        super("execute_script", "считать и исполнить скрипт из указанного файла."
                + " В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в "
                + "интерактивном режиме", 1);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        return args;
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        String fileName = (String) args[0];
        if (fileHistory.contains(fileName)) {
            return new CommandResult("There is a problem: script will loop.");
        } else {
            try {
                CommandListener listenerFromFile = new CommandListener(new FileReader(fileName));
                fileHistory.add(fileName);
                listenerFromFile.run();
            } catch (IOException e) {
                return new CommandResult("Wrong opening file");
            }
            fileHistory.remove(fileName);
        }
        return new CommandResult("Exiting from " + fileName);
    }
}
