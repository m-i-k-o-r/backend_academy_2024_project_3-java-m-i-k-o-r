package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс для анализа частоты использования различных HTTP статусов в логах
 * <hr>
 * Этот класс реализует интерфейс {@link Metric} и отслеживает количество
 * появлений каждого HTTP статуса в логах
 * <hr>
 * Методы предоставляют доступ к частоте использования статусов в
 * виде отсортированной карты
 */
public class ResponseMetric implements Metric {
    /** Карта, отслеживающая количество запросов для каждого HTTP статуса */
    private final Map<Integer, Integer> statusCodeFrequency = new HashMap<>();

    @Override
    public void update(LogRecord log) {
        statusCodeFrequency.merge(log.status(), 1, Integer::sum);
    }

    /**
     * Возвращает частоту использования HTTP статусов в виде отсортированной карты
     *
     * @return отсортированная карта статусов и их частот в порядке убывания частоты
     */
    public Map<Integer, Integer> getStatusCodeFrequency() {
        return statusCodeFrequency.entrySet()
            .stream()
            .sorted(Map.Entry.<Integer, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ));
    }
}
