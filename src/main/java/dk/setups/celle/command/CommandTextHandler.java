package dk.setups.celle.command;

import dk.setups.celle.CellePlugin;
import dk.setups.celle.config.LangConfig;
import eu.okaeri.commands.service.CommandData;
import eu.okaeri.commands.service.Invocation;
import eu.okaeri.platform.bukkit.i18n.BI18n;
import eu.okaeri.platform.minecraft.commands.I18nCommandsTextHandler;
import lombok.NonNull;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandTextHandler extends I18nCommandsTextHandler {

    private static final Pattern STATIC_KEY_PATTERN = Pattern.compile("#\\{([^}]+)}");

    private LangConfig lang;

    public CommandTextHandler(LangConfig lang) {
        super(new LinkedHashMap<>());
        this.lang = lang;
    }

    private void lazyLoadI18n() {
        this.i18n.computeIfAbsent("lang", (key) -> JavaPlugin
                .getPlugin(CellePlugin.class).getInjector()
                .get("lang", BI18n.class)
                .orElseThrow(() -> new IllegalStateException("Missing BI18n instance")));
    }

    @Override
    public String resolve(@NonNull String text) {
        this.lazyLoadI18n();
        Set<String> contextKeys = findKeys(STATIC_KEY_PATTERN, text);

        for(String key : contextKeys) {
            String value = lang.get(key, String.class);
            if(isValid(value, key)) {
                text = text.replace("#{" + key + "}", value);
                break;
            }
        }

        return text;
    }

    private static Set<String> findKeys(Pattern pattern, String text) {
        if (!text.contains("#") && !text.contains("$")) {
            return Collections.emptySet();
        } else {
            Set<String> keys = new HashSet();
            Matcher matcher = pattern.matcher(text);

            while(matcher.find()) {
                keys.add(matcher.group(1));
            }

            return keys;
        }
    }

    private static boolean isValid(String value, @NonNull String key) {
        return (value != null) && !("<" + key + ">").equals(value);
    }

    @Override
    public String resolve(@NonNull CommandData data, @NonNull Invocation invocation, @NonNull String text) {
        this.lazyLoadI18n();
        return super.resolve(data, invocation, text);
    }
}