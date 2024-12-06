package backend.academy.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import lombok.experimental.UtilityClass;

/**
 * Утилитарный класс, предоставляющий методы для форматирования чисел, строк,
 * заголовков и генерации прогресс-баров
 * <hr>
 * Этот класс включает статические методы для форматирования и обработки данных различными способами, такими как:
 * <ul>
 *     <li>Форматирование чисел с разделением групп и десятичными знаками</li>
 *     <li>Создание строк в стиле кода</li>
 *     <li>Извлечение и форматирование ссылок</li>
 *     <li>Форматирование заголовков для отображения</li>
 *     <li>Вычисление процентов и создание визуальных прогресс-баров</li>
 * </ul>
 */
@UtilityClass
public class Formatter {

    /**
     * Форматирует число с использованием заданного десятичного формата
     *
     * @param value число, которое нужно отформатировать
     * @return строковое представление числа (например, "1,234.56")
     */
    public static String formatNum(Number value) {
        double doubleValue = value.doubleValue();
        DecimalFormat df = new DecimalFormat("###,###.##");
        return df.format(doubleValue);
    }

    /**
     * Оборачивает строку в обратные кавычки для стилизации как встроенный код
     *
     * @param string строка
     * @return строка, обёрнутая в обратные кавычки
     */
    public static String formatCode(String string) {
        return String.format("`%s`", string);
    }

    /**
     * Извлекает последнюю часть пути, заменяет "%20" на пробелы и форматирует её как встроенный код,
     * используя {@link #formatCode(String)}
     *
     * @param string путь, который требуется отформатировать
     * @return отформатированная строка
     */
    public static String formatLink(String string) {
        return formatCode(string.substring(string.lastIndexOf('/') + 1).replace("%20", " "));
    }

    /**
     * Форматирует набор заголовков в отображаемую строку
     *
     * @param headers массив заголовков (первый элемент это основной заголовок, остальные - значения)
     * @return отформатированная строка (например, "**Заголовок**: значение_1, значение_2")
     */
    public static String formatHeaders(String... headers) {
        if (headers.length >= 2) {
            String value = String.join(", ", Arrays.copyOfRange(headers, 1, headers.length));
            return String.format("**%s**: %s%n%n", headers[0], value);
        } else {
            return String.format("**%s**: -%n%n", headers[0]);
        }
    }

    private static final int LENGTH_BAR = 20;
    private static final int PERCENTAGE_MULTIPLIER = 100;

    /**
     * Вычисляет значение процента
     *
     * @param numerator   часть
     * @param denominator целое
     * @return значение процента
     */
    public static int calculatePercentage(int numerator, int denominator) {
        return denominator != 0 ? (numerator * PERCENTAGE_MULTIPLIER) / denominator : 0;
    }

    /**
     * Генерирует прогресс-бар, основываясь на текущее и максимальное значения
     *
     * @param value текущее значение
     * @param max   максимальное значение
     * @return строка, представляющая прогресс-бар (например, "████████░░░░░░░░░░░░")
     */
    public static String generateBar(int value, int max) {
        if (max == 0) {
            return "";
        }
        int percentage = calculatePercentage(value, max);
        int length = (int) Math.round((double) percentage / PERCENTAGE_MULTIPLIER * LENGTH_BAR);
        return "█".repeat(length) + "░".repeat(LENGTH_BAR - length);
    }
}
