package backend.academy.statistics;

import backend.academy.parser.LogRecord;
import com.google.common.math.Quantiles;
import java.util.ArrayList;
import java.util.List;

public class BasicMetrics implements Metric {
    private int totalRequests = 0;
    private long totalResponseSize = 0;
    private final List<Integer> responseSizes = new ArrayList<>();

    @Override
    public void update(LogRecord log) {
        totalRequests++;
        int responseSize = log.bodyBytesSent();
        totalResponseSize += responseSize;
        responseSizes.add(responseSize);
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public double getAverageResponseSize() {
        return totalRequests == 0 ? 0 : (double) totalResponseSize / totalRequests;
    }

    public double getPercentile(int percentile) {
        if (responseSizes.isEmpty()) {
            return 0;
        }
        return Quantiles.percentiles().index(percentile).compute(responseSizes);
    }
}
