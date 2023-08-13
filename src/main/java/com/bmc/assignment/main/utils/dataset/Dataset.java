package com.bmc.assignment.main.utils.dataset;

import lombok.Data;
import lombok.NonNull;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Dataset {
    private final String[] headers;
    private final String[][] lines;
    
    public static Dataset fromCSVFile(File csvFile) throws IOException {
        try (FileReader reader = new FileReader(csvFile);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withHeader())) {
            
            List<CSVRecord> records = csvParser.getRecords();
            if (records.isEmpty()) {
                throw new IllegalArgumentException("CSV file is empty");
            }
            
            String[] headers = csvParser.getHeaderMap().keySet().toArray(new String[0]);
            List<String[]> linesList = new ArrayList<>();
            for (CSVRecord record : records) {
                String[] values = new String[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    values[i] = record.get(headers[i]);
                }
                linesList.add(values);
            }
            
            String[][] lines = linesList.toArray(new String[0][0]);
            return new Dataset(headers, lines);
        }
    }
    
    public Dataset(@NonNull final String[] headers, @NonNull final String[][] lines) {
        assetValid(headers, lines);
        this.headers = headers;
        this.lines = lines;
    }
    
    private void assetValid(@NonNull final String[] headers, @NonNull final String[][] lines) {
        assert Arrays.stream(lines).allMatch(line -> line.length == headers.length);
    }
}


