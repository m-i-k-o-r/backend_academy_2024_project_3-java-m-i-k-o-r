package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import static backend.academy.utils.Constants.HTTP_ERROR_STATUS_MAX;
import static backend.academy.utils.Constants.HTTP_ERROR_STATUS_MIN;

/**
 * Класс для анализа временных метрик запросов и ошибок
 * <hr>
 * Этот класс реализует интерфейс {@link Metric} и отслеживает
 * количество запросов и ошибок по часам и дням
 * <hr>
 * Методы предоставляют возможность анализировать данные по запросам и ошибкам,
 * а также вычислять корреляцию между ними
 */
public class TimeMetric implements Metric {

    /** Карта количества запросов по часам */
    private final Map<LocalDateTime, Integer> hourlyRequestCount = new HashMap<>();

    /** Карта количества запросов по дням */
    private final Map<LocalDate, Integer> dailyRequestCount = new HashMap<>();

    /** Карта количества ошибок по часам */
    private final Map<LocalDateTime, Integer> hourlyErrorCount = new HashMap<>();

    @Override
    public void update(LogRecord log) {
        LocalDateTime hour = log.timeLocal().truncatedTo(ChronoUnit.HOURS);
        LocalDate day = hour.toLocalDate();

        hourlyRequestCount.merge(hour, 1, Integer::sum);

        dailyRequestCount.merge(day, 1, Integer::sum);

        if (isErrorStatus(log.status())) {
            hourlyErrorCount.merge(hour, 1, Integer::sum);
        } else {
            hourlyErrorCount.merge(hour, 0, Integer::sum);
        }
    }

    /**
     * Проверяет, является ли статус HTTP ошибочным
     *
     * @param status код статуса HTTP
     * @return true, если статус является ошибочным, иначе false
     */
    private boolean isErrorStatus(int status) {
        return status >= HTTP_ERROR_STATUS_MIN && status <= HTTP_ERROR_STATUS_MAX;
    }

    /**
     * Возвращает день с наибольшим количеством запросов
     *
     * @return дата с максимальным количеством запросов или null, если данных нет
     */
    public LocalDate getDayWithMostRequests() {
        return dailyRequestCount.entrySet()
            .stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse(null);
    }

    /**
     * Возвращает карту с почасовыми данными для указанной даты
     *
     * @param countMap карта данных
     * @param date     дата для фильтрации
     * @return отсортированная карта данных за указанный день
     */
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

    /**
     * Возвращает количество запросов по часам для указанной даты
     *
     * @param date дата для анализа
     * @return карта количества запросов по часам
     */
    public Map<LocalDateTime, Integer> getHourlyRequestCountByDate(LocalDate date) {
        return getHourlyCountByDate(hourlyRequestCount, date);
    }

    /**
     * Возвращает количество ошибок по часам для указанной даты
     *
     * @param date дата для анализа
     * @return карта количества ошибок по часам
     */
    public Map<LocalDateTime, Integer> getHourlyErrorCountByDate(LocalDate date) {
        return getHourlyCountByDate(hourlyErrorCount, date);
    }

    /**
     * Вычисляет корреляцию между количеством запросов и количеством ошибок за указанный день
     *
     * @param date дата для анализа
     * @return коэффициент корреляции
     */
    public double calculateCorrelation(LocalDate date) {
        Map<LocalDateTime, Integer> requestMap = getHourlyRequestCountByDate(date);
        Map<LocalDateTime, Integer> errorMap = getHourlyErrorCountByDate(date);

        double[] requestArray = requestMap.values().stream()
            .mapToDouble(Integer::doubleValue)
            .toArray();

        double[] errorArray = errorMap.values().stream()
            .mapToDouble(Integer::doubleValue)
            .toArray();

        if (requestArray.length != errorArray.length || requestArray.length == 0) {
            return 0;
        }

        return new PearsonsCorrelation().correlation(requestArray, errorArray);
    }
}
