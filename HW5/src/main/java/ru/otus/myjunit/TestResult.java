package ru.otus.myjunit;

/**
 * Created by sergey on 01.05.17.
 */
public class TestResult {
    private String testName;
    private boolean ok;
    private String errorMessage;

    public TestResult(String testName, boolean ok, String errorMessage) {
        this.testName = testName;
        this.ok = ok;
        this.errorMessage = errorMessage;
    }

    public String getTestName() {
        return testName;
    }

    public boolean isOk() {
        return ok;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public String toString() {
        String result = "testName:'" + testName + "', ok:" + ok;
        if (!ok) {
            result += ", error:" + errorMessage;
        }
        return result;
    }
}
