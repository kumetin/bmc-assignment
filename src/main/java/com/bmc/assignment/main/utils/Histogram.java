package com.bmc.assignment.main.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;

@Data
public class Histogram {
    
    private static Logger logger = LoggerFactory.getLogger(Histogram.class);
    private final PercentileData[] percentilesData;
    
    public Histogram(PercentileData[] result) {
        this.percentilesData = result;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Histogram{\n");
        for (int i=0 ; i<percentilesData.length ; i++) {
            final PercentileData percentileData = percentilesData[i];
            sb.append("\t(%3d) %s\n".formatted(i+1, percentileData));
        }
        sb.append("}");
        return sb.toString();
    }
    
    @Data
    @AllArgsConstructor
    @Builder
    public static class PercentileData {
        @NonNull ArrayList<Double> values;
        @NonNull PercentileRange range;
        
        public PercentileData() {
        }
        
        public int frequency() {
            return values.size();
        }
        
        @Override
        public String toString() {
            return "%3.0f - %3.0f : %d".formatted(range.lowerBound, range.upperBound, frequency());
        }
    }
    
    public static Histogram percentilesHistogram(@NonNull double[] values, int classesNum) {
        logger.info("Calculating a histogram of " + classesNum + " percentiles for " + values.length + " values");
        
        final ValuesData valuesData = new ValuesData(Double.MAX_VALUE, Double.MIN_VALUE);
        Arrays.stream(values).forEach(v -> {
            valuesData.min = min(v, valuesData.min);
            valuesData.max = max(v, valuesData.max);
        });
        
        final int classWidth = (int) Math.ceil((valuesData.max - valuesData.min) / classesNum);
        
        final PercentileData.PercentileDataBuilder[] partialResult =
            new PercentileData.PercentileDataBuilder[classesNum];
        for (int i=0 ; i<partialResult.length ; i++) {
            PercentileData.PercentileDataBuilder builder = PercentileData.builder();
            partialResult[i] = builder
                .values(new ArrayList<>())
                .range(new PercentileRange(i*classWidth, (i+1)*classWidth));
        }
        
        Arrays.stream(values).sorted().forEach(observation -> {
            int upperBoundary = (observation > classWidth)
                ? Math.multiplyExact( (int) Math.ceil(observation / classWidth), classWidth)
                : classWidth;
            int lowerBoundary = (upperBoundary > classWidth)
                ? Math.subtractExact(upperBoundary, classWidth)
                : 0;
            int classIndex = lowerBoundary / classWidth;
            if (classIndex == classesNum) classIndex--;
            partialResult[classIndex].values.add(observation);
            partialResult[classIndex].range(new PercentileRange(lowerBoundary, upperBoundary));
            
            logger.debug("value %4.3f goes into percentile #%d (%d - %d) : current frequency %d".formatted(
                observation, classIndex+1, lowerBoundary, upperBoundary, partialResult[classIndex].values.size()));
        });
        
        final List<PercentileData> percentiles =
            Arrays.stream(partialResult).map(PercentileData.PercentileDataBuilder::build).toList();
        
        return new Histogram(percentiles.toArray(new PercentileData[0]));
    }
    
    @Data
    @AllArgsConstructor
    public static class ValuesData {
        double min;
        double max;
    }
    @Data
    @AllArgsConstructor
    public static class PercentileRange {
        double lowerBound;
        double upperBound;
    }
    
}
