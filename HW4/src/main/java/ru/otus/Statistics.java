package ru.otus;

import java.util.HashMap;
import java.util.Map;

class Statistics {
    private final Map<String,Long> countMinor = new HashMap<>();
    private final Map<String,Long> durationMinor = new HashMap<>();

    private Map<String,Long>  countMajor = new HashMap<>();
    private Map<String,Long>  durationMajor = new HashMap<>();

    void intCountMinor(String gcName) {
        long value = 1;
        if (countMinor.containsKey(gcName)) {
            value = countMinor.get(gcName) + 1;
        }
        countMinor.put(gcName, value);
    }

    void intCountMajor(String gcName) {
        long value = 1;
        if (countMajor.containsKey(gcName)) {
            value = countMajor.get(gcName) + 1;
        }
        countMajor.put(gcName, value);
    }

    void addDurationMinor(String gcName, long lastDuration) {
        long value = lastDuration;
        if (durationMinor.containsKey(gcName)) {
            value += durationMinor.get(gcName);
        }
        durationMinor.put(gcName, value);
    }

    void addDurationFull(String gcName, long lastDuration) {
        long value = lastDuration;
        if (durationMajor.containsKey(gcName)) {
            value += durationMajor.get(gcName);
        }
        durationMajor.put(gcName, value);
    }

    Map<String, Long> getCountMinor() {
        return countMinor;
    }

    Map<String, Long> getCountMajor() {
        return countMajor;
    }

    Map<String, Long> getDurationMinor() {
        return durationMinor;
    }

    Map<String, Long> getDurationMajor() {
        return durationMajor;
    }
}
