package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;

/**
 * Интерфейс для представления метрики, которая может быть обновлена на основе записи лога
 * <hr>
 * Классы, реализующие этот интерфейс, должны предоставлять механизм обновления своих данных
 * на основе переданной записи лога
 */
public interface Metric {

    /**
     * Обновляет метрику на основе переданного лога
     *
     * @param log запись лога
     */
    void update(LogRecord log);
}
