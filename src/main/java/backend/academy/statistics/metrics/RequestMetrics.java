package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс для сбора и анализа метрик HTTP-запросов
 * <hr>
 * Этот класс реализует интерфейс {@link Metric} и отслеживает следующие показатели:
 * <ul>
 *     <li>Частота использования запрашиваемых путей запросов</li>
 *     <li>Частота использования HTTP-методов запросов</li>
 * </ul>
 * <hr>
 * Методы предоставляют доступ к наиболее часто используемым путям и методам
 * с возможностью сортировки по частоте
 */
public class RequestMetrics implements Metric {

    /** Частота использования запрашиваемых путей запросов */
    private final Map<String, Integer> pathFrequency = new HashMap<>();

    /** Частота использования HTTP-методов запросов */
    private final Map<String, Integer> methodFrequency = new HashMap<>();

    @Override
    public void update(LogRecord log) {
        pathFrequency.merge(log.request().path(), 1, Integer::sum);
        methodFrequency.merge(log.request().method(), 1, Integer::sum);
    }

    /**
     * Возвращает список наиболее часто запрашиваемых путей
     *
     * @param limit максимальное количество путей, которые необходимо вернуть
     * @return отсортированная карта путей и их частот в порядке убывания частоты
     */
    public Map<String, Integer> getMostRequestedPaths(int limit) {
        return pathFrequency.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .limit(limit)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ));
    }

    /**
     * Возвращает частоту использования HTTP-методов запросов
     *
     * @return отсортированная карта методов и их частот в порядке убывания частоты
     */
    public Map<String, Integer> getMethodFrequency() {
        return methodFrequency.entrySet()
            .stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ));
    }
}
