package ru.otus.objectgetsize;

/**
 * Created by sergey on 09.04.17.
 */
public interface ObjectGetSize {

    long getSize(Object object) throws GettingSizeError;
}
