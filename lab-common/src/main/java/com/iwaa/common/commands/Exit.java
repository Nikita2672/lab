package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandListener;
import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.Request;
import com.iwaa.common.network.CommandResult;
import com.iwaa.common.state.State;

public class Exit extends Command {
    public Exit() {
        super("exit", "завершить программу", 0);
    }

    @Override
    public Object[] readArgs(Object[] args) {
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args) {
        State.switchPerformanceStatus();
        if (CommandListener.isOnClient()) {
            System.out.println("Bye bye");
            return CommandAdmin.getNetworkListener().listen(new Request(new Save(), new Object[]{}));
        } else {
            System.out.println("Saving...");
            CommandAdmin.getCollectionAdmin().write();
            return new CommandResult("Shut down.");
        }
    }
}
