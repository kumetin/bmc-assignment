package com.bmc.assignment.main.utils.histogram;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Data
public class SummarizedHistogram {
    
    @NonNull private ArrayList<SummarizedPercentileData> percentiles;
    
    public static SummarizedHistogram fromHistogram(Histogram histogram) {
        final List<SummarizedPercentileData> percentiles = Arrays.stream(histogram.getPercentilesData())
            .map(d -> new SummarizedPercentileData(d.getRange()))
            .toList();
        return new SummarizedHistogram(new ArrayList<>(percentiles));
    }
    
    @Data
    public static class SummarizedPercentileData {
        @NonNull Histogram.PercentileRange range;
        int frequency;
    }
}
