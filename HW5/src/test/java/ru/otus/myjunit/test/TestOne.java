package ru.otus.myjunit.test;

import ru.otus.myjunit.annotations.After;
import ru.otus.myjunit.annotations.Before;
import ru.otus.myjunit.annotations.Test;
import ru.otus.myjunit.data.ClassForTest;

import static ru.otus.myjunit.Assert.assertFail;
import static ru.otus.myjunit.Assert.assertTrue;

/**
 * Created by sergey on 30.04.17.
 */
public class TestOne {

    @Before
    public void before() {
        System.out.println("before");
        ClassForTest.resetSummX();
        ClassForTest.resetSummY();
    }

    @After
    public void after() {
        System.out.println("after");
        ClassForTest.incSummX();
        ClassForTest.incSummY();
    }

    @Test
    public void summ() {
        final int x = 3;
        final int y = 2;

        ClassForTest classForTest = new ClassForTest(x, y);
        assertTrue("test Summ", classForTest.summ() == 5);
    }

    @Test
    public void diff() {
        final int x = 3;
        final int y = 2;

        ClassForTest classForTest = new ClassForTest(x, y);
        assertTrue("test diff", classForTest.diff() == 1);
    }


}
