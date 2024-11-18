package backend.academy.report.sections;

import backend.academy.report.Formatter;
import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.BasicMetrics;
import java.util.List;

public class GeneralInfoSection extends Section {
    @Override
    protected String getHeader() {
        return "Общая информация";
    }

    @Override
    protected List<String> getTableHeaders() {
        return List.of("Метрика", "Значение");
    }

    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        BasicMetrics metric = statistics.getMetric(BasicMetrics.class);
        return List.of(
            List.of("Файл", Formatter.formatLink(statistics.source())),
            List.of("Количество запросов", Formatter.formatNum(metric.totalRequests())),
            List.of("Средний размер ответа", Formatter.formatNum(metric.averageResponseSize()) + " b"),
            List.of("95p размера ответа", Formatter.formatNum(metric.getPercentile(95)) + " b"),
            List.of("50p размера ответа", Formatter.formatNum(metric.getPercentile(50)) + " b")
        );
    }
}
