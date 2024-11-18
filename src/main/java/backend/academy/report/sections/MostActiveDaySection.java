package backend.academy.report.sections;

import backend.academy.report.Formatter;
import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.TimeMetric;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MostActiveDaySection extends Section {
    @Override
    protected String getHeader() {
        return "Дневная активность";
    }

    @Override
    protected List<String> getTableHeaders() {
        return List.of("Время", "Req", "Request (chart)", "Err", "Error (chart)", "Err (%)");
    }

    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        TimeMetric metric = statistics.getMetric(TimeMetric.class);

        LocalDate mostActiveDay = metric.getDayWithMostRequests();
        if (mostActiveDay == null) {
            return List.of();
        }

        Map<LocalDateTime, Integer> hourlyRequests = metric.getHourlyRequestCountByDate(mostActiveDay);
        Map<LocalDateTime, Integer> hourlyErrors = metric.getHourlyErrorCountByDate(mostActiveDay);

        int maxRequests = hourlyRequests.values().stream().max(Integer::compareTo).orElse(1);

        List<List<String>> rows = new ArrayList<>();
        for (LocalDateTime time : hourlyRequests.keySet()) {
            int requests = hourlyRequests.getOrDefault(time, 0);
            int errors = hourlyErrors.getOrDefault(time, 0);

            String requestBar = generateBar(requests, maxRequests);
            String errorBar = generateBar(errors, maxRequests);
            int errorPercent = requests != 0
                ? (errors * 100) / requests
                : 0;

            rows.add(List.of(
                time.format(DateTimeFormatter.ofPattern("HH:mm")),
                String.valueOf(requests),
                requestBar,
                String.valueOf(errors),
                errorBar,
                String.format("%d%%", errorPercent)
            ));
        }

        return rows;
    }

    private String generateBar(int value, int max) {
        if (max == 0) {
            return "";
        }
        int length = (int) Math.round((double) value / max * 20);
        return "█".repeat(length) + "░".repeat(20 - length);
    }

    @Override
    protected List<List<String>> prepareInfo(LogStatistics statistics) {
        TimeMetric metric = statistics.getMetric(TimeMetric.class);
        LocalDate mostActiveDay = metric.getDayWithMostRequests();
        if (mostActiveDay == null) {
            return List.of();
        }

        return List.of(
            List.of("День с наибольшей активностью", mostActiveDay.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy"))),
            List.of("Корреляция", Formatter.formatNum(metric.calculateCorrelation(mostActiveDay)))
        );
    }
}
