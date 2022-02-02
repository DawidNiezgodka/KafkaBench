package util;

import java.text.DecimalFormat;

public class Formatter {

    private static final DecimalFormat FORMATTER = new DecimalFormat("#.##");

    private Formatter() {}

    public static double round(double d) {
        return Double.parseDouble(FORMATTER.format(d));
    }
}
