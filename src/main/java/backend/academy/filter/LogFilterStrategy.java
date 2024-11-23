package backend.academy.filter;

import backend.academy.model.LogRecord;
import java.util.function.Predicate;

public interface LogFilterStrategy<T> {
    Predicate<LogRecord> createPredicate(T value);
}
