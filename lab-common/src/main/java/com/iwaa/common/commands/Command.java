package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;

import java.io.Serializable;

public abstract class Command implements Serializable {

    private final String name;
    private final String description;
    private final int inlineArgsCount;

    protected Command(String name, String description, int inlineArgsCount) {
        this.name = name;
        this.description = description;
        this.inlineArgsCount = inlineArgsCount;
    }
    public abstract CommandResult execute(Object[] args, CommandAdmin commandAdmin);

    public abstract Object[] readArgs(Object[] args, CommandAdmin commandAdmin);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getInlineArgsCount() {
        return inlineArgsCount;
    }
}
