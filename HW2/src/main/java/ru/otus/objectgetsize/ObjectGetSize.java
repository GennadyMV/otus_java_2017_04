package ru.otus.objectgetsize;

/**
 * Created by sergey on 09.04.17.
 */
public interface ObjectGetSize {

    long getSize(Object object) throws GettingSizeError;
    long getSize(short shortVal) throws GettingSizeError;
    long getSize(byte byteVal) throws GettingSizeError;
    long getSize(int intVal) throws GettingSizeError;
    long getSize(float floatVal) throws GettingSizeError;
    long getSize(double doubleVal) throws GettingSizeError;
    long getSize(long longVal) throws GettingSizeError;
    long getSize(boolean boolVal) throws GettingSizeError;
    long getSize(char charVal) throws GettingSizeError;


}
