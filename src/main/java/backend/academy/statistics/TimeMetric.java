package backend.academy.statistics;

import backend.academy.parser.LogRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TimeMetric implements Metric {
    private final Map<LocalDateTime, Integer> hourlyRequestCount = new HashMap<>();
    private final Map<LocalDate, Integer> dailyRequestCount = new HashMap<>();
    private final Map<LocalDateTime, Integer> hourlyErrorCount = new HashMap<>();

    @Override
    public void update(LogRecord log) {
        LocalDateTime hour = log.timeLocal().truncatedTo(ChronoUnit.HOURS);
        LocalDate day = hour.toLocalDate();

        hourlyRequestCount.merge(hour, 1, Integer::sum);

        dailyRequestCount.merge(day, 1, Integer::sum);

        if (isErrorStatus(log.status())) {
            hourlyErrorCount.merge(hour, 1, Integer::sum);
        }
    }

    private boolean isErrorStatus(int status) {
        return (status >= 400 && status < 600);
    }

    public LocalDate getDayWithMostRequests() {
        return dailyRequestCount.entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    private Map<LocalDateTime, Integer> getHourlyCountByDate(Map<LocalDateTime, Integer> countMap, LocalDate date) {
        return countMap.entrySet()
            .stream()
            .filter(entry -> entry.getKey().toLocalDate().equals(date))
            .sorted(Map.Entry.comparingByKey())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ));
    }

    public Map<LocalDateTime, Integer> getHourlyRequestCountByDate(LocalDate date) {
        return getHourlyCountByDate(hourlyRequestCount, date);
    }

    public Map<LocalDateTime, Integer> getHourlyErrorCountByDate(LocalDate date) {
        return getHourlyCountByDate(hourlyErrorCount, date);
    }
}
