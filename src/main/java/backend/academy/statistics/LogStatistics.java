package backend.academy.statistics;

import backend.academy.filter.LogFilter;
import backend.academy.model.LogRecord;
import backend.academy.statistics.metrics.BasicMetrics;
import backend.academy.statistics.metrics.Metric;
import backend.academy.statistics.metrics.RequestMetrics;
import backend.academy.statistics.metrics.ResponseMetric;
import backend.academy.statistics.metrics.TimeMetric;
import backend.academy.statistics.metrics.UserActivityMetric;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

/**
 * Класс для сбора и обновления статистики логов
 * <hr>
 * Этот класс применяет фильтры к логам, собирает метрики и предоставляет их для анализа
 */
@Getter
public class LogStatistics {
    private final String source;
    private final Map<Class<? extends Metric>, Metric> metrics = new HashMap<>();
    private final LogFilter filters;

    /**
     * Конструктор для инициализации статистики
     *
     * @param filters фильтры для фильтрации логов
     * @param source  источник логов
     */
    public LogStatistics(LogFilter filters, String source) {
        this.filters = filters;
        this.source = source;

        metrics.put(BasicMetrics.class, new BasicMetrics());
        metrics.put(ResponseMetric.class, new ResponseMetric());
        metrics.put(RequestMetrics.class, new RequestMetrics());
        metrics.put(TimeMetric.class, new TimeMetric());
        metrics.put(UserActivityMetric.class, new UserActivityMetric());
    }

    /**
     * Обновляет метрики на основе нового лога
     *
     * @param entry запись лога
     */
    public void update(LogRecord entry) {
        if (!filters.matches(entry)) {
            return;
        }
        for (Metric metric : metrics.values()) {
            metric.update(entry);
        }
    }

    /**
     * Возвращает конкретную метрику по её классу
     *
     * @param <T>         тип метрики
     * @param metricClass класс метрики
     * @return метрика указанного типа
     */
    public <T extends Metric> T getMetric(Class<T> metricClass) {
        return metricClass.cast(metrics.get(metricClass));
    }
}
