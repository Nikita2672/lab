package com.iwaa.common.controllers;

import com.iwaa.common.commands.Add;
import com.iwaa.common.commands.AddIfMax;
import com.iwaa.common.commands.Clear;
import com.iwaa.common.commands.Command;
import com.iwaa.common.commands.ExecuteScript;
import com.iwaa.common.commands.Exit;
import com.iwaa.common.commands.Filter;
import com.iwaa.common.commands.GroupCounting;
import com.iwaa.common.commands.Help;
import com.iwaa.common.commands.History;
import com.iwaa.common.commands.Info;
import com.iwaa.common.commands.PrintField;
import com.iwaa.common.commands.Remove;
import com.iwaa.common.commands.RemoveLower;
import com.iwaa.common.commands.Save;
import com.iwaa.common.commands.Show;
import com.iwaa.common.commands.Update;
import com.iwaa.common.network.NetworkListener;
import com.iwaa.common.network.Request;
import com.iwaa.common.network.CommandResult;
import com.iwaa.common.network.ServerConnectAdmin;
import com.iwaa.common.util.DataParser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;

public final class CommandAdmin {
    private static final int HISTORY_LENGTH = 12;
    private static final Map<String, Command> CLIENT_COMMANDS = new HashMap<>();
    private static final Map<String, Command> SERVER_COMMANDS = new HashMap<>();
    private static final Map<String, Command> COMMANDS_EXECUTING_WITHOUT_SENDING = new HashMap<>();
    private final Queue<String> commandHistory = new LinkedList<>();
    private NetworkListener networkListener;
    private CollectionAdmin collectionAdmin;
    private CommandListener commandListener;
    private ServerConnectAdmin serverConnectAdmin;

    public CommandAdmin() {
        CLIENT_COMMANDS.put("add", new Add());
        CLIENT_COMMANDS.put("add_if_max", new AddIfMax());
        CLIENT_COMMANDS.put("clear", new Clear());
        CLIENT_COMMANDS.put("exit", new Exit());
        CLIENT_COMMANDS.put("history", new History());
        CLIENT_COMMANDS.put("info", new Info());
        CLIENT_COMMANDS.put("filter_less_than_distance", new Filter());
        CLIENT_COMMANDS.put("remove_by_id", new Remove());
        CLIENT_COMMANDS.put("remove_lower", new RemoveLower());
        CLIENT_COMMANDS.put("print_field_descending_distance", new PrintField());
        CLIENT_COMMANDS.put("update", new Update());
        CLIENT_COMMANDS.put("group_counting_by_distance", new GroupCounting());
        CLIENT_COMMANDS.put("execute_script", new ExecuteScript());
        CLIENT_COMMANDS.put("help", new Help());
        CLIENT_COMMANDS.put("show", new Show());

        SERVER_COMMANDS.put("save", new Save());
        SERVER_COMMANDS.put("exit", new Exit());

        COMMANDS_EXECUTING_WITHOUT_SENDING.putAll(SERVER_COMMANDS);
        COMMANDS_EXECUTING_WITHOUT_SENDING.put("execute_script", new ExecuteScript());
    }

    public ServerConnectAdmin getServerConnectAdmin() {
        return serverConnectAdmin;
    }

    public Queue<String> getCommandHistory() {
        return commandHistory;
    }

    public NetworkListener getNetworkListener() {
        return networkListener;
    }

    public CommandListener getCommandListener() {
        return commandListener;
    }

    public void setServerConnectAdmin(ServerConnectAdmin serverConnectAdmin) {
        this.serverConnectAdmin = serverConnectAdmin;
    }

    public void setCommandListener(CommandListener commandListener) {
        this.commandListener = commandListener;
    }

    public CollectionAdmin getCollectionAdmin() {
        return collectionAdmin;
    }

    public void setCollectionAdmin(CollectionAdmin collectionAdmin) {
        this.collectionAdmin = collectionAdmin;
    }

    public void setNetworkListener(NetworkListener networkListener) {
        this.networkListener = networkListener;
    }

    public static Map<String, Command> getCommands() {
        return CLIENT_COMMANDS;
    }

    public void addCommandToHistory(String commandName) {
        commandHistory.add(commandName);
        if (commandHistory.size() > HISTORY_LENGTH) {
            commandHistory.poll();
        }
    }

    public CommandResult onCommandReceived(String inputData) {
        boolean fromClient = commandListener.isOnClient();
        Map<String, Command> commands = SERVER_COMMANDS;
        if (fromClient) {
            commands = CLIENT_COMMANDS;
        }
        String[] commandWithRawArgs = DataParser.parse(inputData);
        String commandName = commandWithRawArgs[0].toLowerCase(Locale.ROOT);
        String[] rawArgs = Arrays.copyOfRange(commandWithRawArgs, 1, commandWithRawArgs.length);
        if (commands.containsKey(commandName)) {
            Command command = commands.get(commandName);
            return processCommand(command, rawArgs);
        }
        return new CommandResult("No such command, call \"help\" to see the list of commands.");
    }

    public CommandResult processCommand(Command command, String[] rawArgs) {
        if (rawArgs.length == command.getInlineArgsCount()) {
            Object[] commandArgs = command.readArgs(rawArgs, this);
            if (commandArgs != null) {
                if (COMMANDS_EXECUTING_WITHOUT_SENDING.containsKey(command.getName())) {
                    return executeCommand(command, commandArgs);
                } else {
                    return networkListener.listen(new Request(command, commandArgs));
                }
            }
        } else {
            return new CommandResult("Wrong number of arguments.");
        }
        return null;
    }

    public CommandResult executeCommand(Command command, Object[] args) {
        this.addCommandToHistory(command.getName());
        return command.execute(args, this);
    }
}
