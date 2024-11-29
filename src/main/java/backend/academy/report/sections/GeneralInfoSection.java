package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.BasicMetrics;
import java.util.List;
import static backend.academy.utils.Constants.PERCENTILE_50;
import static backend.academy.utils.Constants.PERCENTILE_95;
import static backend.academy.utils.Formatter.formatLink;
import static backend.academy.utils.Formatter.formatNum;

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
            List.of("Файл", formatLink(statistics.source())),
            List.of("Количество запросов", formatNum(metric.totalRequests())),
            List.of("Средний размер ответа", formatNum(metric.averageResponseSize()) + " b"),
            List.of("95p размера ответа", formatNum(metric.getPercentile(PERCENTILE_95)) + " b"),
            List.of("50p размера ответа", formatNum(metric.getPercentile(PERCENTILE_50)) + " b")
        );
    }
}
