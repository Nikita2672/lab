package com.iwaa.server.config;

import com.iwaa.common.io.CollectionFileReader;
import com.iwaa.common.io.CollectionFileWriter;
import com.iwaa.server.collection.CollectionAdminImpl;
import com.iwaa.server.parser.Parser;

import java.io.File;

public final class IOConfigurator {
    public static final CollectionFileReader<CollectionAdminImpl> COLLECTION_FILE_READER = new Parser();
    public static final CollectionFileWriter<CollectionAdminImpl> COLLECTION_FILE_WRITER = new Parser();
    private static final String LOCAL_VARIABLE = "COLLECTION_FILE";
    private File inputFile;
    private File outputFile;

    public IOConfigurator() {

    }

    public File getInputFile() {
        return inputFile;
    }

    public File getOutputFile() {
        return outputFile;
    }

    public boolean configure() {
        if (System.getenv(LOCAL_VARIABLE) != null) {
            System.out.println(("Set your collection file as a " + LOCAL_VARIABLE + " environment variable and restart the app."));
            return false;
        } else {
            /*inputFile = new File(System.getenv(LOCAL_VARIABLE));
            outputFile = new File(System.getenv(LOCAL_VARIABLE));*/
            inputFile = new File("C:\\Users\\iwaa0\\IdeaProjects\\Эталон\\lab\\file");
            outputFile = new File("C:\\Users\\iwaa0\\IdeaProjects\\Эталон\\lab\\file");
        }
        System.out.println("Configured successfully!");
        return true;
    }
}
