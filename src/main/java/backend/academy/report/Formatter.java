package backend.academy.report;

import java.text.DecimalFormat;

public class Formatter {
    public static String formatNum(Number value) {
        double doubleValue = value.doubleValue();
        DecimalFormat df = new DecimalFormat("###,###.##");
        return df.format(doubleValue);
    }

    public static String formatCode(String string) {
        return String.format("`%s`", string);
    }

    public static String formatLink(String string) {
        return formatCode(string.substring(string.lastIndexOf("/") + 1).replace("%20", " "));
    }
}
