package ru.otus.myjunit;

import org.junit.Test;
import ru.otus.myjunit.test.TestOne;
import ru.otus.myjunit.test.TestForException;
import ru.otus.myjunit.test.TestVoid;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * Created by sergey on 30.04.17.
 */
public class TestExecutorTest {

    @Test
    public void getTestMethod() {
        List<Method> testMethodsList = TestExecutor.getInstance().getTestMethods(new JunitTest("test", TestOne.class));
        assertEquals("test Methods Counter", 2, testMethodsList.size());
        assertEquals("test Method sum", "summ", testMethodsList.get(0).getName());
        assertEquals("test Method diff", "diff", testMethodsList.get(1).getName());
    }

    @Test
    public void executeTestMethods() throws IllegalAccessException, InstantiationException, NoSuchMethodException {
        TestOne testOneSpy = spy(TestOne.class);
        JunitTest testClass = new JunitTest("test", TestOne.class);
        TestExecutor testExecutorSpy = spy(TestExecutor.getInstance());
        when(testExecutorSpy.createInstanceForTest(testClass)).thenReturn(testOneSpy);

        testExecutorSpy.executeTestMethods(testClass);
        List<TestResult> testResults = testClass.getTestResults();

        assertEquals("test Methods result Counter", 2, testResults.size());
        Map<String, TestResult> testResultMap = testResults.stream()
                .collect(Collectors.toMap(TestResult::getTestName, res -> res));

        assertEquals("summ result OK", true, testResultMap.get("summ").isOk());
        assertEquals("diff result OK", true, testResultMap.get("diff").isOk());

        verify(testOneSpy, times(2)).before();
        verify(testOneSpy, times(1)).summ();
        verify(testOneSpy, times(1)).diff();
        verify(testOneSpy, times(2)).after();
    }


    @Test(expected = ru.otus.myjunit.AssertionError.class)
    public void assertFail() {
        Assert.assertFail("fail as expected");
    }


    @Test(expected = ru.otus.myjunit.AssertionError.class)
    public void assertTrue_false() {
        Assert.assertTrue("test Assert true", false);
    }

    @Test
    public void assertTrue() {
        Assert.assertTrue("test Assert true", true);
    }


    @Test(expected = ru.otus.myjunit.AssertionError.class)
    public void assertNotNull_null() {
        Assert.assertNotNull("test Assert null", null);
    }

    @Test
    public void assertNotNull() {
        Assert.assertNotNull("test Assert null", "notNull");
    }

    @Test
    public void getBefore_null() throws MyJunitException {
        JunitTest testClass = new JunitTest("test", TestVoid.class);
        Method before = TestExecutor.getInstance().getBefore(testClass);
        assertNull("before method null",  before);
    }

    @Test(expected = MyJunitException.class)
    public void getBefore_too_many() throws MyJunitException {
        JunitTest testClass = new JunitTest("test", TestForException.class);
        TestExecutor.getInstance().getBefore(testClass);
    }

    @Test
    public void getBefore() throws MyJunitException {
        JunitTest testClass = new JunitTest("test", TestOne.class);
        Method before = TestExecutor.getInstance().getBefore(testClass);
        assertEquals("before method",  "before", before.getName());
    }

    @Test
    public void getAfter_null() throws MyJunitException {
        JunitTest testClass = new JunitTest("test", TestVoid.class);
        Method after = TestExecutor.getInstance().getAfter(testClass);
        assertNull("after method null",  after);
    }

    @Test(expected = MyJunitException.class)
    public void getAfter_too_many() throws MyJunitException {
        JunitTest testClass = new JunitTest("test", TestForException.class);
        TestExecutor.getInstance().getAfter(testClass);
    }

    @Test
    public void getAfter() throws MyJunitException {
        JunitTest testClass = new JunitTest("test", TestOne.class);
        Method after = TestExecutor.getInstance().getAfter(testClass);
        assertEquals("after method",  "after", after.getName());
    }

    @Test
    public void executeBefore() throws MyJunitException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        TestOne testOneSpy = spy(TestOne.class);
        JunitTest testClass = new JunitTest("test", TestOne.class);

        TestExecutor testExecutorSpy = spy(TestExecutor.getInstance());
        when(testExecutorSpy.createInstanceForTest(testClass)).thenReturn(testOneSpy);

        testExecutorSpy.executeBefore(testClass);
        verify(testOneSpy, times(1)).before();
    }

    @Test
    public void executeAfter() throws MyJunitException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        TestOne testOneSpy = spy(TestOne.class);
        JunitTest testClass = new JunitTest("test", TestOne.class);

        TestExecutor testExecutorSpy = spy(TestExecutor.getInstance());
        when(testExecutorSpy.createInstanceForTest(testClass)).thenReturn(testOneSpy);

        testExecutorSpy.executeAfter(testClass);
        verify(testOneSpy, times(1)).after();
    }

}