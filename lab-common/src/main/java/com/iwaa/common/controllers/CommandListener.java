package com.iwaa.common.controllers;

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
    private Reader reader;
    private State state;
    private CommandAdmin commandAdmin;
    private FileManager fileManager = new FileManager();

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

    public FileManager getFileManager() {
        return fileManager;
    }

    public State getState() {
        return state;
    }

    public Reader getReader() {
        return reader;
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
            fileManager.connectToFile(file);
            reader = new FileReader(file);
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
            reader = new InputStreamReader(System.in);
        } catch (IOException e) {
            System.out.println("There is no such file or you haven't permissions");
            reader = new InputStreamReader(System.in);
        }
    }
}
