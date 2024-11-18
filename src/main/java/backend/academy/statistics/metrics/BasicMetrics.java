package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;
import com.google.common.math.Quantiles;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

public class BasicMetrics implements Metric {
    @Getter
    private int totalRequests = 0;

    @Getter
    private double averageResponseSize = 0;

    private final List<Integer> responseSizes = new ArrayList<>();

    @Override
    public void update(LogRecord log) {
        totalRequests++;
        int responseSize = log.bodyBytesSent();
        averageResponseSize += (responseSize - averageResponseSize) / totalRequests;
        responseSizes.add(responseSize);
    }

    public double getPercentile(int percentile) {
        if (responseSizes.isEmpty()) {
            return 0;
        }
        return Quantiles.percentiles().index(percentile).compute(responseSizes);
    }
}
