package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;

import java.io.File;

public class ExecuteScript extends Command {

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
        if (commandAdmin.getCommandListener().getFileManager().getCurrentFiles().contains(new File(fileName))) {
            return new CommandResult("The command was ignored because it will call recursion.");
        } else {
            File file = new File(fileName);
            commandAdmin.getCommandListener().runFile(file);
        }
        return new CommandResult("Exiting from " + fileName);
    }
}
