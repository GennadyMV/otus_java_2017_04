package ru.otus.myjunit.demo.test;

import ru.otus.myjunit.Assert;
import ru.otus.myjunit.JunitTest;
import ru.otus.myjunit.MyJunitException;
import ru.otus.myjunit.TestExecutor;
import ru.otus.myjunit.annotations.After;
import ru.otus.myjunit.annotations.Before;
import ru.otus.myjunit.annotations.Test;
import ru.otus.myjunit.demo.ArithmeticProgression;

import java.util.List;

/**
 * Created by sergey on 03.05.17.
 */
public class ArithmeticProgressionTest {
    public static void main(String[] args) throws MyJunitException {
        List<JunitTest> testList = TestExecutor.getInstance().getTestList("ru.otus.myjunit.demo.test");
        testList.forEach(test -> {
            try {
                TestExecutor.getInstance().executeTestMethods(test);
                test.getTestResults().forEach(System.out::println);
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }
        });
    }



    private ArithmeticProgression progression;

    @Before
    public void initProgression() {
        progression = new ArithmeticProgression(0, 2);
    }


    @Test
    public void getAn2() {
        int a2 = progression.getAn(2);
        Assert.assertTrue("a2 must be 2", 2 == a2);
    }

    @Test
    public void getAn3() {
        int a3 = progression.getAn(3);
        Assert.assertTrue("a3 must be 4", 4 == a3);
    }

    @Test
    public void getAn100() {
        int a100 = progression.getAn(100) - 1; //to Get Demo Error
        Assert.assertTrue("a100 must be 198", 198 == a100);
    }

    @After
    public void afterCaclulation() {
        Assert.assertNotNull("progression is not null", progression);
    }
}
