package backend.academy.statistics.metrics;

import backend.academy.model.LogRecord;
import com.google.common.math.Quantiles;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

/**
 * Класс для сбора и анализа базовых метрик HTTP-запросов
 * <hr>
 * Этот класс реализует интерфейс {@link Metric} и отслеживает следующие показатели:
 * <ul>
 *     <li>Общее количество запросов</li>
 *     <li>Средний размер тела ответа</li>
 *     <li>95% перцентиль размера ответа сервера</li>
 *     <li>50% перцентиль размера ответа сервера</li>
 * </ul>
 * <hr>
 * Данные обновляются при каждом вызове метода {@link #update(LogRecord)},
 * а расчет процентилей выполняется на основе {@link #responseSizes},
 * то есть на основе накопленных данных о размерах ответов
 */
public class BasicMetrics implements Metric {

    /** Общее количество запросов */
    @Getter
    private int totalRequests = 0;

    /** Средний размер тела ответа */
    @Getter
    private double averageResponseSize = 0;

    private final List<Integer> responseSizes = new ArrayList<>();

    @Override
    public void update(LogRecord log) {
        totalRequests++;
        int responseSize = log.bodyBytesSent();
        averageResponseSize += (responseSize - averageResponseSize) / totalRequests;
        responseSizes.add(responseSize);
    }

    /**
     * Возвращает указанное процентильное значение размера тела ответа
     *
     * @param percentile процентиль (0-100), который необходимо рассчитать
     * @return рассчитанное значение процентиля
     */
    public double getPercentile(int percentile) {
        if (responseSizes.isEmpty()) {
            return 0;
        }
        return Quantiles.percentiles().index(percentile).compute(responseSizes);
    }
}
