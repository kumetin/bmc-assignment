package com.bmc.assignment.main;

import com.bmc.assignment.main.utils.Histogram;
import com.bmc.assignment.main.utils.dataset.Dataset;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;

@Component
public class MainHandler {
    
    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);
    
    public Mono<ServerResponse> titanicFairPricePercentilesHistogram(ServerRequest serverRequest) {
        try {
            final ClassLoader classLoader = getClass().getClassLoader();
            final File csvFile = new File(Objects.requireNonNull(classLoader.getResource("titanic.csv")).getFile());
            final Dataset dataset;
            dataset = Dataset.fromCSVFile(csvFile);
            final double[] input = Arrays.stream(dataset.getColumn("Fair"))
                .mapToDouble(Double::valueOf).toArray();
            final Histogram histogram = Histogram.percentilesHistogram(input, 100);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(histogram));
        } catch (Exception e) {
            logger.error("failed handling 'titanicFairPricePercentilesHistogram'", e);
            throw new RuntimeException(e);
        }
    }
}
