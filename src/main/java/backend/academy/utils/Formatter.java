package backend.academy.utils;

import java.text.DecimalFormat;
import java.util.Arrays;
import lombok.experimental.UtilityClass;

@UtilityClass
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
        return formatCode(string.substring(string.lastIndexOf('/') + 1).replace("%20", " "));
    }

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

    public static int calculatePercentage(int numerator, int denominator) {
        return denominator != 0 ? (numerator * PERCENTAGE_MULTIPLIER) / denominator : 0;
    }

    public static String generateBar(int value, int max) {
        if (max == 0) {
            return "";
        }
        int percentage = calculatePercentage(value, max);
        int length = (int) Math.round((double) percentage / PERCENTAGE_MULTIPLIER * LENGTH_BAR);
        return "█".repeat(length) + "░".repeat(LENGTH_BAR - length);
    }
}
