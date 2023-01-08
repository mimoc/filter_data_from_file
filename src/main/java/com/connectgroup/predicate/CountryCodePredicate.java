package com.connectgroup.predicate;

import com.connectgroup.utils.StringUtils;

import java.util.function.Predicate;

public class CountryCodePredicate implements Predicate {
    private String countryCode;

    public CountryCodePredicate(String country) {
        this.countryCode = country;
    }

    @Override
    public boolean test(Object o) {
        var countryCodeParsed = StringUtils.splitLine(String.valueOf(o))[1];
        return this.countryCode.equalsIgnoreCase(countryCodeParsed);
    }
}
