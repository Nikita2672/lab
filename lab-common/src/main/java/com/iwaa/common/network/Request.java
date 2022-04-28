package com.iwaa.common.network;

import com.iwaa.common.commands.Command;

import java.io.Serializable;
import java.util.Arrays;

public class Request implements Serializable {
    private Command command;
    private Object[] args;

    public Request(Command command, Object[] args) {
        this.command = command;
        this.args = args;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    @Override
    public String toString() {
        return "Request{"
                + "command=" + command
                + ", args=" + Arrays.toString(args)
                + '}';
    }
}
