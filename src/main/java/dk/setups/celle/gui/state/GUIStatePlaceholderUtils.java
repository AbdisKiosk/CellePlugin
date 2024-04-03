package dk.setups.celle.gui.state;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Component
public class GUIStatePlaceholderUtils {

    private @Inject Logger logger;

    private final Map<Class<?>, Set<Field>> placeholderFields = new ConcurrentHashMap<>();
    private final Map<Field, Placeholder> placeholders = new ConcurrentHashMap<>();

    public Map<String, Object> getPlaceholderValues(GUIState guiState) {
        Map<String, Object> placeholderValues = new HashMap<>();
        Set<Field> fields = this.getPlaceholderFields(guiState);
        for(Field field : fields) {
            try {
                placeholderValues.put(getPlaceholder(field).value(), field.get(guiState));
            } catch (IllegalAccessException e) {
                logger.log(Level.SEVERE, "Failed to get placeholder value", e);
            }
        }

        return placeholderValues;
    }

    protected Set<Field> getPlaceholderFields(GUIState guiState) {
        return this.placeholderFields.computeIfAbsent(guiState.getClass(), (clazz) -> {
            Set<Field> fields = new HashSet<>();
            Class<?> currentClass = clazz;
            while (currentClass != null) {
                fields.addAll(Arrays.stream(currentClass.getDeclaredFields())
                        .filter((f) -> f.isAnnotationPresent(Placeholder.class))
                        .collect(Collectors.toSet()));
                currentClass = currentClass.getSuperclass();
            }
            fields.forEach((f) -> f.setAccessible(true));

            return fields;
        });
    }

    protected Placeholder getPlaceholder(Field field) {
        return this.placeholders.computeIfAbsent(field, (f) -> f.getAnnotation(Placeholder.class));
    }
}
