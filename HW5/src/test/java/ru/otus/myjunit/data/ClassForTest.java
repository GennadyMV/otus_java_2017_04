package ru.otus.myjunit.data;

/**
 * Created by sergey on 30.04.17.
 */
public class ClassForTest {
    private int x;
    private int y;

    private static int summX = 0;
    private static int summY = 0;

    public ClassForTest(int x, int y) {
        this.x = x;
        this.y = y;

        summX += x;
        summY += y;
    }

    public int summ() {
        return x + y;
    }

    public int diff() {
        return  x - y;
    }

    public static int getSummX() {
        return summX;
    }

    public static int getSummY() {
        return summY;
    }

    public static void resetSummX() {
        summX = 0;
    }

    public static void resetSummY() {
        summY = 0;
    }

    public static void incSummX() {
        summX++;
    }

    public static void incSummY() {
        summY++;
    }
}
