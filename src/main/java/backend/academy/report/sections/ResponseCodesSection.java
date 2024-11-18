package backend.academy.report.sections;

import backend.academy.report.Formatter;
import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.ResponseMetric;
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

            if (codeName.equals("Unknown")) {
                countUnknown += count;
            } else {
                rows.add(List.of(String.valueOf(code), codeName, Formatter.formatNum(count)));
            }
        }

        if (countUnknown > 0) {
            rows.add(List.of("Unknown", "-", Formatter.formatNum(countUnknown)));
        }

        return rows;
    }

    private static class HttpStatus {
        private static final Map<Integer, String> STATUS_NAMES = Map.of(
            200, "OK",
            201, "Created",
            301, "Moved Permanently",
            302, "Found",
            400, "Bad Request",
            401, "Unauthorized",
            403, "Forbidden",
            404, "Not Found",
            500, "Internal Server Error",
            503, "Service Unavailable"
        );

        public static String getName(int code) {
            return STATUS_NAMES.getOrDefault(code, "Unknown");
        }
    }
}
