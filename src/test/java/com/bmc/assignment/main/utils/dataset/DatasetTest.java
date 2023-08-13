package com.bmc.assignment.main.utils.dataset;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@SpringBootTest
public class DatasetTest {
    
    @Test
    void fromCSVFile() {
        ClassLoader classLoader = getClass().getClassLoader();
        File csvFile = new File(classLoader.getResource("titanic.csv").getFile());
        
        try {
            Dataset dataset = Dataset.fromCSVFile(csvFile);
            System.out.println("Headers: " + Arrays.toString(dataset.getHeaders()));
            System.out.println("Lines: " + Arrays.deepToString(dataset.getLines()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
