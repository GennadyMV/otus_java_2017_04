package ru.otus.objectgetsize.impl;

import ru.otus.objectgetsize.GettingSizeError;
import ru.otus.objectgetsize.ObjectGetSize;
import java.lang.instrument.Instrumentation;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.Map;

import com.sun.tools.attach.VirtualMachine;

/**
 * Created by sergey on 09.04.17.
 *
 * This method is based on jol (http://openjdk.java.net/projects/code-tools/jol/)
 */
public class GetSizeInstrumentation implements ObjectGetSize {
// todo: implement getSize for compounded objects (arrays of classes and so on)

    private static Instrumentation instrumentation;
    private static InstrumentationUtils utils;

    private static long arrayHeaderSize = 0;

    public static void premain(String args, Instrumentation pInstrumentation) {
        System.out.println("premain");
        instrumentation = pInstrumentation;
        utils = new InstrumentationUtils();
        arrayHeaderSize = instrumentation.getObjectSize(new Object[]{});
    }

    public static long getArrayHeaderSize() {
        return arrayHeaderSize;
    }

    @Override
    public long getSize(Object object) {
        Class<?> componentType = object.getClass().getComponentType();
        if (object.getClass().isArray() && componentType != null) {
            long size = 0;
            for (int idx = 0; idx < Array.getLength(object); idx++) {
                size += utils.get(componentType.getName());
            }
            return arrayHeaderSize + size;
        } else {
            return instrumentation.getObjectSize(object);
        }
    }

    @Override
    public long getSize(short shortVal) throws GettingSizeError {
        return utils.get("short");
    }

    @Override
    public long getSize(boolean boolVal) throws GettingSizeError {
        return utils.get("boolean");
    }

    @Override
    public long getSize(char charVal) throws GettingSizeError {
        return utils.get("char");
    }

    @Override
    public long getSize(byte byteVal) throws GettingSizeError {
        return utils.get("byte");
    }

    @Override
    public long getSize(int intVal) throws GettingSizeError {
        return utils.get("int");
    }

    @Override
    public long getSize(float floatVal) throws GettingSizeError {
        return utils.get("float");
    }

    @Override
    public long getSize(double doubleVal) throws GettingSizeError {
        return utils.get("double");
    }

    @Override
    public long getSize(long longVal) throws GettingSizeError {
        return utils.get("long");
    }
}
