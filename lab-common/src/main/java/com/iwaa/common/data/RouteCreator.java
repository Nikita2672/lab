package com.iwaa.common.data;

import com.iwaa.common.controllers.FileManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Function;
import java.util.function.Predicate;

public class RouteCreator {
    private static final int MAX_X = 245;
    private static final int MAX_Y = 362;
    private static final int MIN_DISTANCE = 1;
    private static final String ERROR = "Your argument wasn't correct. Please try again!";
    private static Reader STREAM_READER = new InputStreamReader(System.in);
    private static FileManager fileManager1;

    public RouteCreator() {
    }

    public static void setStreamReader(Reader streamReader) {
        STREAM_READER = streamReader;
    }

    public static void setStreamReader(Reader streamReader, FileManager fileManager) {
        STREAM_READER = streamReader;
        fileManager1 = fileManager;
    }

    public static Reader getStreamReader() {
        return STREAM_READER;
    }

    public static Route createRoute() throws IOException {
        return askForRoute();
    }

    private static Route askForRoute() throws IOException {
        String name = createName();
        Long distance = createDistance();
        Coordinates coordinates = createCoordinates();
        Location from = createLocation();
        Location to = createLocation();
        return new Route(name, coordinates, from, to, distance);
    }

    private static String createName() throws IOException {
        return valid(arg -> ((String) arg).length() > 0, "Enter name (String)",
                ERROR, "The string must not be empry", x -> x, false);
    }

    private static Long createDistance() throws IOException {
        return valid(arg -> ((Long) arg) > MIN_DISTANCE, "Enter Distance (Long) > 1 (can't be null)",
                ERROR, "Distance must be > 1. Try again", Long::parseLong, false);
    }

    private static Coordinates createCoordinates() throws IOException {
        System.out.println("Enter coordinates");
        float x = valid(arg -> ((Float) arg) <= MAX_X, "Enter x (float) <= 245",
                ERROR, "x must be <= 245. Try again", Float::parseFloat, false);
        Float y = valid(arg -> ((Float) arg) <= MAX_Y, "Enter y (float) <= 362",
                ERROR, "y must be <= 362. Try again", Float::parseFloat, false);
        return new Coordinates(y, x);
    }

    private static Location createLocation() throws IOException {
        System.out.println("Enter location");
        Long x = valid(arg -> true, "Please enter x (Long) can't be null",
                ERROR, "x mustn't be null", Long::parseLong, false);
        int y = valid(arg -> true, "Please enter y int",
                ERROR, "y mustn't be null", Integer::parseInt, false);
        Integer z = valid(arg -> true, "Please enter z (Integer) can't be null",
                ERROR, "z mustn't be null", Integer::parseInt, false);

        return new Location(x, y, z);
    }

    public static <T> T valid(Predicate<Object> predicate,
                       String message,
                       String error,
                       String wrong,
                       Function<String, T> function,
                       Boolean nullable) throws IOException {
        System.out.println(message);
        String input;
        T value;
        BufferedReader in = new BufferedReader(STREAM_READER);
        do {
            try {
                if (STREAM_READER.getClass() != FileReader.class) {
                    input = in.readLine();
                } else {
                    input = fileManager1.nextLine();
                }
                if ("".equals(input) && Boolean.TRUE.equals(nullable)) {
                    return null;
                }
                if (null == input && STREAM_READER.getClass() == FileReader.class) {
                    STREAM_READER = new InputStreamReader(System.in);
                    in = new BufferedReader(STREAM_READER);
                    input = in.readLine();
                }
                value = function.apply(input);
            } catch (IllegalArgumentException e) {
                System.out.println(error);
                continue;
            }
            if (predicate.test(value)) {
                return value;
            } else {
                System.out.println(wrong);
            }
            if (null == input && STREAM_READER.getClass() == FileReader.class) {
                STREAM_READER = new InputStreamReader(System.in);
            }
        } while (true);
    }
}
