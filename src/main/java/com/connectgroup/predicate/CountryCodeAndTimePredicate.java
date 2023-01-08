package com.connectgroup.predicate;

import com.connectgroup.utils.StringUtils;

import java.util.function.Predicate;

public class CountryCodeAndTimePredicate implements Predicate {
    private String country;
    private Long time;

    public CountryCodeAndTimePredicate(String country, Long time) {
        this.country = country;
        this.time = time;
    }

    @Override
    public boolean test(Object line) {
        String[] values = StringUtils.splitLine(String.valueOf(line));
        return values[1].equalsIgnoreCase(country) && Long.parseLong(values[2]) > time;
    }
}
