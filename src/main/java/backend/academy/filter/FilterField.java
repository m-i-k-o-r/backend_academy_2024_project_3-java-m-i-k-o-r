package backend.academy.filter;

import lombok.Getter;

@Getter
public enum FilterField {
    FROM("Начальная дата"),
    TO("Конечная дата");

    private final String label;

    FilterField(String label) {
        this.label = label;
    }
}
