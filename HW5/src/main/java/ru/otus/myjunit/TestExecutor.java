package ru.otus.myjunit;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import ru.otus.myjunit.annotations.After;
import ru.otus.myjunit.annotations.Before;
import ru.otus.myjunit.annotations.Test;
import ru.otus.myjunit.annotations.TestSute;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by sergey on 30.04.17.
 */
public class TestExecutor {

    private Reflections prepareReflections(String packageName) {
        return new Reflections(new ConfigurationBuilder()
                .setScanners(new SubTypesScanner(false))
                .setUrls(ClasspathHelper.forClassLoader(new ClassLoader[0]))
                .filterInputsBy(new FilterBuilder()
                        .include(FilterBuilder.prefix(packageName))
                        .exclude(FilterBuilder.prefix(packageName + ".package-info"))));
    }

    private TestExecutor(){};

    private static TestExecutor instance = new TestExecutor();

    public static TestExecutor getInstance() {
        return instance;
    }

    private Object testClassInstance;

    public List<JunitTest> getTestList(String packageName) throws MyJunitException {
        List<JunitTest> testList = new ArrayList<>();

        Reflections reflections = new Reflections(packageName);
        Set<Class<?>> annotatedPackage = reflections.getTypesAnnotatedWith(TestSute.class);
        if (annotatedPackage == null || annotatedPackage.isEmpty()) {
            throw new MyJunitException("In the package:" + packageName + " tests not found");
        }

        annotatedPackage.forEach(annotPack -> {
            Package pack = annotPack.getPackage();
            Set<Class<?>> testClasses = prepareReflections(pack.getName()).getSubTypesOf(Object.class);
            testClasses.stream().filter(testClass ->
                    Arrays.stream(testClass.getMethods()).anyMatch(method -> method.isAnnotationPresent(Test.class)))
                    .forEach(testClass -> testList.add(new JunitTest(pack.getAnnotation(TestSute.class).name(), testClass)));
        });
        return testList;
    }

    public List<Method> getTestMethods(JunitTest test) {
        return Arrays.stream(test.getTestClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Test.class))
                .collect(Collectors.toList());
    }

    public Object createInstanceForTest(JunitTest test) throws IllegalAccessException, InstantiationException {
        if (testClassInstance == null) {
            testClassInstance = test.getTestClass().newInstance();
        }
        return testClassInstance;
    }

    public void executeTestMethods(JunitTest test) throws IllegalAccessException, InstantiationException {
        List<Method> testMethodsList = getTestMethods(test);
        testMethodsList.forEach((Method method) -> {
            try {
                Object obj = createInstanceForTest(test);
                executeBefore(test);
                obj.getClass().getMethod(method.getName()).invoke(obj);
                executeAfter(test);
                test.saveTestResult(new TestResult(method.getName(), true, null));
            } catch (Exception e) {
                Throwable targetException;
                TestResult testResult = null;

                if (e instanceof InvocationTargetException) {
                    targetException = ((InvocationTargetException) e).getTargetException();
                    if (targetException != null && targetException instanceof AssertionError) {
                        testResult = new TestResult(method.getName(), false, targetException.getMessage());
                    }
                }

                if (testResult == null) {
                    testResult = new TestResult(method.getName(), false, e.getMessage());
                    e.printStackTrace();
                }
                test.saveTestResult(testResult);
            }
        });
    }

    public Method getBefore(JunitTest test) throws MyJunitException {
        List<Method> beforeMethods = Arrays.stream(test.getTestClass().getMethods())
                .filter(method -> method.isAnnotationPresent(Before.class))
                .collect(Collectors.toList());
        if (beforeMethods.isEmpty()) {
            return null;
        }
        if (beforeMethods.size() > 1) {
            throw new MyJunitException("Found too more before methods:" + beforeMethods.size());
        }
        return beforeMethods.get(0);
    }

    public Method getAfter(JunitTest test) throws MyJunitException {
        List<Method> afterMethods = Arrays.stream(test.getTestClass().getMethods())
                .filter(method -> method.isAnnotationPresent(After.class))
                .collect(Collectors.toList());
        if (afterMethods.isEmpty()) {
            return null;
        }
        if (afterMethods.size() > 1) {
            throw new MyJunitException("Found too more after methods:" + afterMethods.size());
        }
        return afterMethods.get(0);
    }

    public void executeBefore(JunitTest test) throws MyJunitException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Method before = getBefore(test);
        if (before != null) {
            Object obj = createInstanceForTest(test);
            obj.getClass().getMethod(before.getName()).invoke(obj);
        }
    }

    public void executeAfter(JunitTest test) throws MyJunitException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Method after = getAfter(test);
        if (after != null) {
            Object obj = createInstanceForTest(test);
            obj.getClass().getMethod(after.getName()).invoke(obj);
        }
    }
}
