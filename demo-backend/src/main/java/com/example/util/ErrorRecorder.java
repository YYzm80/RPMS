package com.example.util;

import java.util.ArrayList;
import java.util.List;

public class ErrorRecorder {
    private static final List<String> errors = new ArrayList<>();

    public static void addError(String error) {
        errors.add(error);
    }

    public static List<String> getErrors() {
        return new ArrayList<>(errors);
    }

    public static void clearErrors() {
        errors.clear();
    }
}
