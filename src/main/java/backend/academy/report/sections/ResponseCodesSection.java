package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.ResponseMetric;
import backend.academy.utils.Formatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ResponseCodesSection extends Section {
    @Override
    protected String getHeader() {
        return "Коды ответа";
    }

    @Override
    protected List<String> getTableHeaders() {
        return List.of("Код", "Имя", "Количество");
    }

    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        ResponseMetric metric = statistics.getMetric(ResponseMetric.class);
        Map<Integer, Integer> statusCodeFrequency = metric.getStatusCodeFrequency();

        List<List<String>> rows = new ArrayList<>();
        int countUnknown = 0;

        for (var entry : statusCodeFrequency.entrySet()) {
            int code = entry.getKey();
            int count = entry.getValue();
            String codeName = HttpStatus.getName(code);

            if (HttpStatus.RARE_STATUS.equals(codeName)) {
                countUnknown += count;
            } else {
                rows.add(List.of(String.valueOf(code), codeName, Formatter.formatNum(count)));
            }
        }

        if (countUnknown > 0) {
            rows.add(List.of(HttpStatus.RARE_STATUS, "-", Formatter.formatNum(countUnknown)));
        }

        return rows;
    }

    private static final class HttpStatus {
        private static final Map<Integer, String> STATUS_NAMES = Map.ofEntries(
            Map.entry(200, "OK"),
            Map.entry(201, "Created"),
            Map.entry(301, "Moved Permanently"),
            Map.entry(302, "Found"),
            Map.entry(304, "Not Modified"),
            Map.entry(400, "Bad Request"),
            Map.entry(401, "Unauthorized"),
            Map.entry(403, "Forbidden"),
            Map.entry(404, "Not Found"),
            Map.entry(500, "Internal Server Error"),
            Map.entry(503, "Service Unavailable")
        );

        public static final String RARE_STATUS = "Less common";

        public static String getName(int code) {
            return STATUS_NAMES.getOrDefault(code, RARE_STATUS);
        }
    }
}
