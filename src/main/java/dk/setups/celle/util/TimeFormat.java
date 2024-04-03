package dk.setups.celle.util;

import dk.setups.celle.config.Config;
import eu.okaeri.i18n.I18n;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import eu.okaeri.platform.bukkit.i18n.BI18n;
import eu.okaeri.platform.bukkit.i18n.message.BukkitMessageDispatcher;
import eu.okaeri.platform.core.annotation.Component;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class TimeFormat {

    private @Inject Config config;

    public String formatConsiseLeft(Date date) {
        return formatConsiseLeft(date.getTime() - System.currentTimeMillis());
    }

    public String formatConsiseLeft(long millisLeft) {
        Map<Config.TimeKey, List<String>> abbrs = config.getTimeFormatConcise();
        StringBuilder result = new StringBuilder();

        List<Config.TimeKey> keys = new ArrayList<>(abbrs.keySet());

        //jeg undskylder
        for(int i = keys.size() - 1; i >= 0; i--) {
            Config.TimeKey unit = keys.get(i);
            int value = (int) (millisLeft / unit.getMs());
            if(value > 0) {
                if(result.length() > 0) {
                    result.append(config.getTimeFormatConciseSeparator());
                }
                result.append(value).append(abbrs.get(unit).get(getIndex(value)));
                millisLeft -= value * unit.getMs();
            }
        }

        return result.toString();
    }

    public String formatLongLeft(Date date) {
        return formatLongLeft(date.getTime() - System.currentTimeMillis());
    }

    public String formatLongLeft(long millisLeft) {
        Map<Config.TimeKey, List<String>> abbrs = config.getTimeFormatLong();
        List<Config.TimeKey> keys = new ArrayList<>(abbrs.keySet());

        Config.TimeKey abbr = keys.get(0);
        for(Config.TimeKey unit : keys) {
            long value = millisLeft / unit.getMs();
            if(value > 0 && (abbr == null || abbr.getMs() < unit.getMs())) {
                abbr = unit;
            }
        }

        int value = (int) (millisLeft / abbr.getMs());
        return value + config.getTimeFormatLongSeparator() + abbrs.get(abbr).get(getIndex(value));
    }

    private int getIndex(int value) {
        return Math.min(value - 1, 1);
    }

    public String formatDateShort(Date date) {
        return new SimpleDateFormat(config.getDateFormatShort()).format(date);
    }

    public String formatDateLong(Date date) {
        CompiledMessage message = CompiledMessage.of(config.getDateFormatLong());

        return PlaceholderContext.of(message)
                .with("day-number", formatTo2Chars(date.getDate()))
                .with("month-number", formatTo2Chars(date.getMonth()))
                .with("year", date.getYear() + 1900)
                .with("hour", formatTo2Chars(date.getHours()))
                .with("minute", formatTo2Chars(date.getMinutes()))
                .with("second", formatTo2Chars(date.getSeconds()))
                .with("day", formatDay(date.getDay() + 1))
                .with("month", formatMonth(date.getMonth() + 1))
                .apply();
    }

    private String formatTo2Chars(int value) {
        return value < 10 ? "0" + value : String.valueOf(value);
    }

    public void addFastFormats(PlaceholderContext context, long timeleftMs, Date rentedUntil) {
        context.with("timeleft_concise", formatConsiseLeft(timeleftMs))
                .with("timeleft_long", formatLongLeft(timeleftMs))
                .with("rented_until_concise", formatDateShort(rentedUntil));
    }

    public void addFormats(PlaceholderContext context, long timeleftMs, Date rentedUntil) {
        context.with("timeleft_concise", formatConsiseLeft(timeleftMs))
                .with("timeleft_long", formatLongLeft(timeleftMs))
                .with("rented_until_concise", formatDateShort(rentedUntil))
                .with("rented_until_long", formatDateLong(rentedUntil));
    }

    public void addFormats(BukkitMessageDispatcher dispatcher, long timeleftMs, Date rentedUntil) {
        dispatcher
                .with("timeleft_concise", formatConsiseLeft(timeleftMs))
                .with("timeleft_long", formatLongLeft(timeleftMs))
                .with("rented_until_concise", formatDateShort(rentedUntil))
                .with("rented_until_long", formatDateLong(rentedUntil));
    }

    public String formatDay(int dayInWeek) {
        return config.getDateFormatLongDayNames().get(DayOfWeek.of(dayInWeek));
    }

    public String formatMonth(int monthInYear) {
        return config.getDateFormatLongMonthNames().get(Month.of(monthInYear));
    }

}