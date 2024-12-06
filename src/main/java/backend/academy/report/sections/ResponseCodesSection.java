package backend.academy.report.sections;

import backend.academy.statistics.LogStatistics;
import backend.academy.statistics.metrics.ResponseMetric;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import static backend.academy.utils.Formatter.formatNum;

/**
 * Класс для отображения секции с информацией о кодах ответа HTTP
 * <br>
 * Используется для генерации отчета, содержащего таблицу с частотным распределением
 * кодов ответа, их описаниями (именами) и количеством запросов
 * <br>
 * Данные извлекаются из метрики {@link ResponseMetric}.
 */
public class ResponseCodesSection extends Section {
    @Override
    protected String getHeader() {
        return "Коды ответа";
    }

    /**
     * Возвращает заголовки столбцов таблицы секции
     * <hr>
     * Таблица содержит следующие столбцы:
     * <ul>
     *     <li><b>код</b> - числовой HTTP код ответа</li>
     *     <li><b>имя</b> - текстовое описание кода ответа</li>
     *     <li><b>количество</b> - число запросов с этим кодом</li>
     * </ul>
     */
    @Override
    protected List<String> getTableHeaders() {
        return List.of("Код", "Имя", "Количество");
    }

    /**
     * Подготавливает строки таблицы с информацией о кодах ответа HTTP
     * <hr>
     * Информация включает код ответа, его имя и количество появлений в логах,
     * полученных из {@link ResponseMetric#getStatusCodeFrequency()}
     * <br>
     * Редкие или неизвестные коды группируются под названием "Less common"
     * <hr>
     * Если данных о кодах ответа нет, возвращается строка-заглушка ("-", "-", "-")
     */
    @Override
    protected List<List<String>> prepareRows(LogStatistics statistics) {
        ResponseMetric metric = statistics.getMetric(ResponseMetric.class);
        Map<Integer, Integer> statusCodeFrequency = metric.getStatusCodeFrequency();

        if (statusCodeFrequency.isEmpty()) {
            return List.of(List.of("-", "-", "-"));
        }

        List<List<String>> rows = new ArrayList<>();
        int countUnknown = 0;

        for (var entry : statusCodeFrequency.entrySet()) {
            int code = entry.getKey();
            int count = entry.getValue();
            String codeName = HttpStatus.getName(code);

            if (HttpStatus.RARE_STATUS.equals(codeName)) {
                countUnknown += count;
            } else {
                rows.add(List.of(String.valueOf(code), codeName, formatNum(count)));
            }
        }

        if (countUnknown > 0) {
            rows.add(List.of(HttpStatus.RARE_STATUS, "-", formatNum(countUnknown)));
        }

        return rows;
    }

    /**
     * Вспомогательный класс для работы с кодами ответа HTTP
     * <br>
     * Содержит отображение стандартных HTTP кодов в их текстовые описания
     */
    private static final class HttpStatus {
        private static final Map<Integer, String> STATUS_NAMES = Map.ofEntries(
            Map.entry(200, "OK"),
            Map.entry(201, "Created"),
            Map.entry(301, "Moved Permanently"),
            Map.entry(302, "Found"),
            Map.entry(304, "Not Modified"),
            Map.entry(400, "Bad Request"),
            Map.entry(401, "Unauthorized"),
            Map.entry(403, "Forbidden"),
            Map.entry(404, "Not Found"),
            Map.entry(500, "Internal Server Error"),
            Map.entry(503, "Service Unavailable")
        );

        public static final String RARE_STATUS = "Less common";

        /**
         * Возвращает текстовое описание для заданного HTTP кода
         *
         * @param code HTTP код ответа
         * @return текстовое описание или "Less common", если код неизвестен
         */
        public static String getName(int code) {
            return STATUS_NAMES.getOrDefault(code, RARE_STATUS);
        }
    }
}
