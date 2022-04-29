package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.Request;
import com.iwaa.common.network.CommandResult;

public class Exit extends Command {
    public Exit() {
        super("exit", "завершить программу", 0);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        if (commandAdmin.getCommandListener().isOnClient()) {
            commandAdmin.getCommandListener().getState().switchPerformanceStatus();
            System.out.println("Bye bye");
            return commandAdmin.getNetworkListener().listen(new Request(new Save(), new Object[]{}));
        } else {
            System.out.println("Saving...");
            commandAdmin.getCollectionAdmin().write();
            commandAdmin.getServerConnectAdmin().getState().switchPerformanceStatus();
            return new CommandResult("Shut down.");
        }
    }
}
