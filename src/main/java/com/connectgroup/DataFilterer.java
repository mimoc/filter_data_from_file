package com.connectgroup;

import com.connectgroup.predicate.AboveAverageTimePredicate;
import com.connectgroup.predicate.CountryCodePredicate;
import com.connectgroup.predicate.CountryCodeAndTimePredicate;
import com.connectgroup.utils.StringUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

public final class DataFilterer {
    private DataFilterer() {
    }

    public static List<String> filterByCountry(Reader source, String country) throws IOException {
        if (country == null || country.equals("")) throw new IllegalArgumentException("Country must have value");

        CountryCodePredicate countryCodePredicate = new CountryCodePredicate(country);
        return retrieveFilteredLines(source, countryCodePredicate);
    }


    public static List<String> filterByCountryWithResponseTimeAboveLimit(Reader source, String country, long limit) throws IOException {
        if (country == null || country.equals("")) throw new IllegalArgumentException("Country must have value");
        if (limit < 0) throw new IllegalArgumentException("Limit must be positive");

        CountryCodeAndTimePredicate countryTimePredicate = new CountryCodeAndTimePredicate(country, limit);
        return retrieveFilteredLines(source, countryTimePredicate);
    }

    public static List<String> filterByResponseTimeAboveAverage(Reader source) throws IOException {

        return getLinesAboveAverageTime(source);
    }

    private static List<String> retrieveFilteredLines(Reader source, Predicate<String> predicate) throws IOException {
        List<String> filteredLines = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(source)) {
            bufferedReader.lines().forEach(line -> {
                if (predicate.test(line))
                    filteredLines.add(line);
            });
        }
        return filteredLines;
    }

    private static List<String> getLinesAboveAverageTime(Reader source) throws IOException {
        AtomicLong nrOfLines = new AtomicLong();
        AtomicLong totalTime = new AtomicLong();
        List<String> fileLines = new ArrayList<>();
        List<String> filteredLines = new ArrayList<>();

        try (BufferedReader bufferedReader = new BufferedReader(source)) {
            bufferedReader.lines().skip(1).forEach(line -> {
                String time = StringUtils.splitLine(line)[2];
                if (time != null)
                    totalTime.addAndGet(Long.valueOf(time));

                nrOfLines.getAndIncrement();
                fileLines.add(line);
            });

            var averageTime = nrOfLines.get() == 0 ? 0 : totalTime.get() / nrOfLines.get();

            AboveAverageTimePredicate aboveAverageTimePredicate = new AboveAverageTimePredicate(averageTime);

            fileLines.forEach(line -> {
                if (aboveAverageTimePredicate.test(line))
                    filteredLines.add(line);
            });
        }
        return filteredLines;
    }
}
