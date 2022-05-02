package com.iwaa.common.controllers;

import com.iwaa.common.data.RouteCreator;
import com.iwaa.common.network.CommandResult;
import com.iwaa.common.state.State;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

public class CommandListener implements Runnable {
    private boolean onClient;
    private final Reader reader;
    private State state;
    private CommandAdmin commandAdmin;

    public CommandListener(Reader reader, CommandAdmin commandAdmin) {
        this.reader = reader;
        this.state = new State();
        this.commandAdmin = commandAdmin;
    }

    public CommandListener(CommandAdmin commandAdmin, State state) {
        this.reader = new InputStreamReader(System.in);
        this.state = state;
        this.commandAdmin = commandAdmin;
        commandAdmin.setCommandListener(this);
    }

    public State getState() {
        return state;
    }

    public boolean isOnClient() {
        return onClient;
    }

    public void setOnClient() {
        onClient = true;
    }

    public void run() {
        try (BufferedReader in = new BufferedReader(reader)) {
            while (state.getPerformanceStatus()) {
                if (reader.getClass() != FileReader.class) {
                    System.out.println("===========================");
                }
                String input = in.readLine();
                if (input == null) {
                    break;
                }
                if (!"".equals(input)) {
                    CommandResult commandResult = commandAdmin.onCommandReceived(input);
                    if (commandResult != null) {
                        commandResult.showResult();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Invalid output.");
        }
    }

    public void runFile(File file) {
        try {
            FileManager fileManager = new FileManager();
            fileManager.connectToFile(file);
            RouteCreator.setStreamReader(new FileReader(file), fileManager);
            while (state.getPerformanceStatus()) {
                String input = fileManager.nextLine();
                if (input == null) {
                    break;
                }
                if (!"".equals(input)) {
                    CommandResult commandResult = commandAdmin.onCommandReceived(input);
                    if (commandResult != null) {
                        commandResult.showResult();
                    }
                }
            }
            RouteCreator.setStreamReader(new InputStreamReader(System.in));
        } catch (IOException e) {
            System.out.println("Invalid output");
            RouteCreator.setStreamReader(new InputStreamReader(System.in));
        }
    }
}
