package ru.otus.myjunit.demo;

/**
 * Created by sergey on 03.05.17.
 */
public class ArithmeticProgression {

    private int a1;
    private int d;

    public ArithmeticProgression(int a1, int d) {
        this.a1 = a1;
        this.d = d;
    }


    public int getAn(int n) {
        return a1 + (n - 1) * d;
    }
}
