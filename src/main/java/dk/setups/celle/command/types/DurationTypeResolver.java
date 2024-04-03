package dk.setups.celle.command.types;

import eu.okaeri.commands.meta.ArgumentMeta;
import eu.okaeri.commands.service.CommandData;
import eu.okaeri.commands.service.Invocation;
import eu.okaeri.commands.type.resolver.BasicTypeResolver;
import lombok.NonNull;

import java.time.Duration;

public class DurationTypeResolver extends BasicTypeResolver<Duration> {

    @Override
    public boolean supports(@NonNull Class<?> type) {
        return type.equals(Duration.class);
    }

    @Override
    public Duration resolve(@NonNull Invocation invocation, @NonNull CommandData data, @NonNull ArgumentMeta argumentMeta, @NonNull String time) {
        Duration duration = Duration.ZERO;
        String number = "";

        for (int i = 0; i < time.length(); i++) {
            char c = time.charAt(i);
            if (Character.isDigit(c)) {
                number += c;
            } else {
                if (c == 'd') {
                    duration = duration.plusDays(Integer.parseInt(number));
                } else if (c == 'h') {
                    duration = duration.plusHours(Integer.parseInt(number));
                } else if (c == 'm') {
                    duration = duration.plusMinutes(Integer.parseInt(number));
                }
                number = "";
            }
        }
        return duration;
    }
}