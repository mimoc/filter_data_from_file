package com.connectgroup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class DataFiltererTest {
    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    @Test
    public void shouldReturnEmptyList_WhenLogFileIsEmpty() throws IOException {
        assertTrue(DataFilterer.filterByCountry(openFile("src/test/resources/empty"), "GB").isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldReturnException_WhenFilterByEmptyCountry() throws IOException {
        DataFilterer.filterByCountry(
                openFile("src/test/resources/multi-lines"), "");
    }

    @Test
    public void shouldReturnOneLine_WhenFilterByCountryWhenLogFileIsSingleLine() throws IOException {
        assertEquals(1, DataFilterer.filterByCountry(
                openFile("src/test/resources/single-line"), "GB").size());
    }

    @Test
    public void shouldRetrieveCorrectData_WhenFilterByCountry() throws IOException {
        List<String> filteredLines = DataFilterer.filterByCountry(
                openFile("src/test/resources/multi-lines"), "US");

        assertOutput("src/test/resources/filtered-by-country", filteredLines);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_WhenFilterByCountryWithResponseTimeAboveLimitInvalid_countryNull() throws IOException {
        DataFilterer.filterByCountryWithResponseTimeAboveLimit(
                openFile("src/test/resources/multi-lines"), null, 500);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowException_WhenFilterByCountryWithResponseTimeAboveLimitInvalid_limitMinus1() throws IOException {
        DataFilterer.filterByCountryWithResponseTimeAboveLimit(
                openFile("src/test/resources/multi-lines"), null, -1);
    }

    @Test
    public void shouldReturnEmptyList_WhenFilterByCountryWithResponseTimeAboveLimitLogFileIsEmpty() throws IOException {
        assertTrue(DataFilterer.filterByCountryWithResponseTimeAboveLimit(
                openFile("src/test/resources/empty"), "US", 500).isEmpty());
    }

    @Test
    public void shouldRetrieveCorrectData_WhenFilterByCountryWithResponseTimeAboveLimit() throws IOException {
        List<String> filteredLines = DataFilterer.filterByCountryWithResponseTimeAboveLimit(
                openFile("src/test/resources/multi-lines"), "US", 500);

        assertOutput("src/test/resources/filtered-by-country-and-response-time", filteredLines);
    }

    @Test
    public void shouldReturnEmptyList_WhenFilterByTimeAboveAverageLogFileIsEmpty() throws IOException {
        assertTrue(DataFilterer.filterByResponseTimeAboveAverage(
                openFile("src/test/resources/empty")).isEmpty());
    }

    @Test
    public void shouldReturnEmptyList_WhenFilterByTimeAboveAverageLogFileIsSingleLine() throws IOException {
        assertEquals(0,DataFilterer.filterByResponseTimeAboveAverage(
                openFile("src/test/resources/single-line")).size());
    }

    @Test
    public void shouldRetrieveCorrectData_WhenFilterByTimeAboveAverage() throws IOException {
        List<String> filteredLines = DataFilterer.filterByResponseTimeAboveAverage(
                openFile("src/test/resources/multi-lines"));

        assertOutput("src/test/resources/filtered-by-time-above-average", filteredLines);
    }

    private FileReader openFile(String filename) throws FileNotFoundException {
        return new FileReader(filename);
    }

    private void writeToFile(File fileName, List<String> lines) throws IOException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            lines.forEach(line -> {
                try {
                    bufferedWriter.write(line);
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

    }

    private boolean areFilesEqual(String firstFile, String secondFile) throws IOException {
        byte[] first = Files.readAllBytes(Path.of(firstFile));
        byte[] second = Files.readAllBytes(Path.of(secondFile));
        return Arrays.equals(first, second);
    }

    private void assertOutput(String expectedFile, List<String> filteredLines) throws IOException {
        final File tempFile = tempFolder.newFile("temp.txt");

        writeToFile(tempFile, filteredLines);

        assertTrue(areFilesEqual(tempFile.getAbsolutePath(), expectedFile));
    }
}

