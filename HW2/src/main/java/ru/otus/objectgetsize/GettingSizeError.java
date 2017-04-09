package ru.otus.objectgetsize;

/**
 * Created by sergey on 09.04.17.
 */
public class GettingSizeError extends Exception {

    public GettingSizeError(String message, IllegalAccessException e) {
        super(message, e);
    }

    public GettingSizeError(String message) {
        super(message);
    }
}
