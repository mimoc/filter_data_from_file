package com.connectgroup.predicate;

import com.connectgroup.utils.StringUtils;

import java.util.function.Predicate;

public class AboveAverageTimePredicate implements Predicate {

    private Long averageTime;

    public AboveAverageTimePredicate(Long averageTime) {
        this.averageTime = averageTime;
    }

    @Override
    public boolean test(Object line) {
        String[] values = StringUtils.splitLine(String.valueOf(line));
        return Long.parseLong(values[2]) > averageTime;
    }
}
