package ru.otus.objectgetsize.impl;

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;

/**
 * Created by sergey on 12.04.17.
 */
public class InstrumentationUtils {

    private final Unsafe U;

    private final int booleanSize;
    private final int byteSize;
    private final int shortSize;
    private final int charSize;
    private final int floatSize;
    private final int intSize;
    private final int longSize;
    private final int doubleSize;

    private static class MyBooleans4 {
        private boolean f1, f2, f3, f4;
    }

    private static class MyBytes4 {
        private byte f1, f2, f3, f4;
    }

    private static class MyShorts4 {
        private short f1, f2, f3, f4;
    }

    private static class MyChars4 {
        private char f1, f2, f3, f4;
    }

    private static class MyInts4 {
        private int f1, f2, f3, f4;
    }

    private static class MyFloats4 {
        private float f1, f2, f3, f4;
    }

    private static class MyLongs4 {
        private long f1, f2, f3, f4;
    }

    private static class MyDoubles4 {
        private double f1, f2, f3, f4;
    }

    InstrumentationUtils() {
        U = tryUnsafe();

        booleanSize = getMinDiff(MyBooleans4.class);
        byteSize = getMinDiff(MyBytes4.class);
        shortSize = getMinDiff(MyShorts4.class);
        charSize = getMinDiff(MyChars4.class);
        floatSize = getMinDiff(MyFloats4.class);
        intSize = getMinDiff(MyInts4.class);
        longSize = getMinDiff(MyLongs4.class);
        doubleSize = getMinDiff(MyDoubles4.class);
    }


    private int getMinDiff(Class<?> klass) {
        try {
            int off1 = (int) U.objectFieldOffset(klass.getDeclaredField("f1"));
            int off2 = (int) U.objectFieldOffset(klass.getDeclaredField("f2"));
            int off3 = (int) U.objectFieldOffset(klass.getDeclaredField("f3"));
            int off4 = (int) U.objectFieldOffset(klass.getDeclaredField("f4"));
            return minDiff(off1, off2, off3, off4);
        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("Infrastructure failure, klass = " + klass, e);
        }
    }

    private static int minDiff(int... offs) {
        int min = Integer.MAX_VALUE;
        for (int o1 : offs) {
            for (int o2 : offs) {
                if (o1 != o2) {
                    min = Math.min(min, Math.abs(o1 - o2));
                }
            }
        }
        return min;
    }

    private static Unsafe tryUnsafe() {
        return AccessController.doPrivileged(
                (PrivilegedAction<Unsafe>) () -> {
                    try {
                        Field unsafe = Unsafe.class.getDeclaredField("theUnsafe");
                        unsafe.setAccessible(true);
                        return (Unsafe) unsafe.get(null);
                    } catch (NoSuchFieldException e) {
                        throw new IllegalStateException(e);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                }
        );
    }

    long get(String klassName) {
        switch (klassName) {
            case "byte": return byteSize;
            case "boolean": return booleanSize;
            case "short": return shortSize;
            case "char": return charSize;
            case "int": return intSize;
            case "float": return floatSize;
            case "long": return longSize;
            case "double": return doubleSize;
            default:
                throw new IllegalArgumentException("unknown type:" + klassName);
        }
    }
}
