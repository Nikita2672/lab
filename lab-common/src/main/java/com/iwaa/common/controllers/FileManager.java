package com.iwaa.common.controllers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Stack;

public class FileManager {
    private final Stack<BufferedReader> currentFilesReaders = new Stack<>();
    private final Stack<File> currentFiles = new Stack<>();

    public String nextLine() {
        if (!currentFilesReaders.isEmpty()) {
            try {
                String input = currentFilesReaders.peek().readLine();
                if (input == null) {
                    currentFiles.pop();
                    currentFilesReaders.pop().close();
                    return nextLine();
                } else {
                    return input;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return "";
    }

    public Stack<BufferedReader> getCurrentFilesReaders() {
        return currentFilesReaders;
    }

    public Stack<File> getCurrentFiles() {
        return currentFiles;
    }

    public void connectToFile(File file) throws IOException, UnsupportedOperationException {
        BufferedReader newReader = new BufferedReader(new FileReader(file));
        currentFiles.push(file);
        currentFilesReaders.push(newReader);
    }
}
