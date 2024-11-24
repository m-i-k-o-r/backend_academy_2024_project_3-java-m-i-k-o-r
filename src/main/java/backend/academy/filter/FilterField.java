package backend.academy.filter;

import lombok.Getter;

@Getter
public enum FilterField {
    FROM("Начальная дата"),
    TO("Конечная дата"),
    ADDRESS("IP адрес"),
    METHOD("HTTP метод"),
    STATUS("HTTP статус"),
    AGENT("Клиентское устройство");

    private final String label;

    FilterField(String label) {
        this.label = label;
    }
}
