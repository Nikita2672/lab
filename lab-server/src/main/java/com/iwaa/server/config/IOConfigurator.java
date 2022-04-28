package com.iwaa.server.config;

import com.iwaa.common.io.CollectionFileReader;
import com.iwaa.common.io.CollectionFileWriter;
import com.iwaa.server.collection.CollectionAdminImpl;
import com.iwaa.server.parser.Parser;

import java.io.File;

public final class IOConfigurator {
    public static final CollectionFileReader<CollectionAdminImpl> COLLECTION_FILE_READER = new Parser();
    public static final CollectionFileWriter<CollectionAdminImpl> COLLECTION_FILE_WRITER = new Parser();
    private static File inputFile;
    private static File outputFile;

    private IOConfigurator() {

    }

    public static File getInputFile() {
        return inputFile;
    }

    public static File getOutputFile() {
        return outputFile;
    }

    public static boolean configure() {
        if (System.getenv("COLLECTION_FILE") == null) {
            System.out.println(("Set your collection file as a COLLECTION_FILE environment variable and restart the app."));
            return false;
        } else {
            inputFile = new File(System.getenv("COLLECTION_FILE"));
            outputFile = new File(System.getenv("COLLECTION_FILE"));
        }
        System.out.println("Configured successfully!");
        return true;
    }
}
