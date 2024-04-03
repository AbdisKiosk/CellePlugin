package dk.setups.celle.util;

import dk.setups.celle.config.Config;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;

import java.util.Locale;

@Component
public class NumberFormat {

    private @Inject Config config;
    private static final java.text.NumberFormat format = java.text.NumberFormat.getInstance(Locale.GERMAN);
    static {
        format.setMinimumFractionDigits(0);
    }

    public String formatSimple(double value) {
        return format.format(value);
    }

    public String formatAbbreviated(double value) {
        if(value < 1000) {
            return formatSimple(value);
        }
        if(value < 1000000) {
            return format.format(value / 1000) + "K";
        }
        if(value < 1000000000) {
            return format.format(value / 1000000) + "M";
        }
        return format.format(value / 1000000000) + "B";
    }
}
