package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.util.function.Predicate;

/**
 * Интерфейс для создания предикатов фильтрации логов
 * <br>
 * Этот интерфейс определяет метод для создания предиката на основе переданного значения
 *
 * @param <T> тип значения, на основе которого создается предикат
 */
public interface LogFilterStrategy<T> {

    /**
     * Создает предикат для фильтрации лога
     *
     * @param value значение для фильтрации
     * @return предикат, проверяющий, соответствует ли лог заданному значению
     */
    Predicate<LogRecord> createPredicate(T value);
}
