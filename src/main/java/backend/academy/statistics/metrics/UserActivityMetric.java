package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Класс для анализа активности пользователей на основе логов
 * <hr>
 * Этот класс реализует интерфейс {@link Metric} и отслеживает количество запросов,
 * сделанных каждым пользователем
 * <hr>
 * Методы предоставляют возможность получить общее количество уникальных пользователей
 * и список самых активных пользователей с указанием количества их запросов
 */
public class UserActivityMetric implements Metric {

    /** Карта, хранящая количество запросов для каждого пользователя по его удалённому адресу */
    private final Map<String, Integer> userRequestCount = new HashMap<>();

    @Override
    public void update(LogRecord log) {
        userRequestCount.merge(log.remoteAddr(), 1, Integer::sum);
    }

    /**
     * Возвращает общее количество уникальных пользователей
     *
     * @return количество уникальных пользователей
     */
    public int getCount() {
        return userRequestCount.size();
    }

    /**
     * Возвращает список самых активных пользователей с их количеством запросов
     *
     * @param limit максимальное количество пользователей, которые необходимо вернуть
     * @return отсортированная карта пользователей и количества их запросов в порядке убывания активности
     */
    public Map<String, Integer> getTopActiveUsers(int limit) {
        return userRequestCount.entrySet()
            .stream()
            .sorted((entry1, entry2) -> Integer.compare(entry2.getValue(), entry1.getValue()))
            .limit(limit)
            .collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ));
    }
}
