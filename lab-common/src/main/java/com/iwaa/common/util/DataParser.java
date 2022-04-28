package com.iwaa.common.util;

import java.util.ArrayList;

public final class DataParser {

    private DataParser() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static String[] parse(String data) {
        if ("".equals(data) || data == null) {
            return new String[0];
        }
        String[] args = smartSplit(data.trim());
        for (String str : args
        ) {
            str = str.trim();
        }
        return args;
    }

    private static String[] smartSplit(String line) {
        ArrayList<String> splitLine = new ArrayList<>();
        StringBuilder currentString = new StringBuilder();
        boolean screeningStarted = false;
        for (char ch : line.toCharArray()) {
            if (ch == ' ' && !screeningStarted) {
                if (!"".equals(currentString.toString())) {
                    splitLine.add(currentString.toString());
                }
                currentString.setLength(0);
            } else if (ch == '"') {
                screeningStarted = !screeningStarted;
            } else {
                currentString.append(ch);
            }
        }
        splitLine.add(currentString.toString());
        String[] args = new String[splitLine.size()];
        splitLine.toArray(args);
        return args;
    }
}
