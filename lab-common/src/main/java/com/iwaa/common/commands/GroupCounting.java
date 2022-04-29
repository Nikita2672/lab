package com.iwaa.common.commands;

import com.iwaa.common.controllers.CommandAdmin;
import com.iwaa.common.network.CommandResult;

public class GroupCounting extends Command {
    public GroupCounting() {
        super("group_counting_by_distance", "сгруппировать элементы коллекции по значению поля distance, "
                + "вывести количество элементов в каждой группе", 0);
    }

    @Override
    public Object[] readArgs(Object[] args, CommandAdmin commandAdmin) {
        return new Object[0];
    }

    @Override
    public CommandResult execute(Object[] args, CommandAdmin commandAdmin) {
        return new CommandResult(commandAdmin.getCollectionAdmin().outputGroupsByDistance());
    }
}
