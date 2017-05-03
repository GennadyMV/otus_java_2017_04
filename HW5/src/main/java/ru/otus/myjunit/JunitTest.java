package ru.otus.myjunit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sergey on 30.04.17.
 */
public class JunitTest {

    private final String testSuteName;
    private final Class testClass;
    private final List<TestResult> testResults = new ArrayList<>();

    public JunitTest(String testSuteName, Class testClass) {
        this.testSuteName = testSuteName;
        this.testClass = testClass;
    }

    public String getTestSuteName() {
        return testSuteName;
    }

    public Class getTestClass() {
        return testClass;
    }

    public List<TestResult> getTestResults() {
        return testResults;
    }

    public void saveTestResult(TestResult testResult) {
        testResults.add(testResult);
    }

}
