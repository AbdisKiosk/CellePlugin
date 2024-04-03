package dk.setups.celle.gui;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dk.setups.celle.gui.state.GUIState;
import dk.setups.celle.gui.state.GUIStatePlaceholderUtils;
import eu.okaeri.i18n.minecraft.adventure.AdventureMessage;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.message.CompiledMessage;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class GUIPlaceholderUtils {

    private @Inject GUIStatePlaceholderUtils guiStateUtils;
    private @Inject Placeholders placeholders;

    public ItemStack withPlaceholders(GUIState state, Map<String, Object> placeholders, ItemStack item) {
        Map<String, Object> all = new HashMap<>(guiStateUtils.getPlaceholderValues(state));
        all.putAll(placeholders);
        return withPlaceholders(all, item);
    }

    public net.kyori.adventure.text.Component withPlaceholders(GUIState state, String text) {
        return withPlaceholders(guiStateUtils.getPlaceholderValues(state), text);
    }

    public ItemStack withPlaceholders(GUIState state, ItemStack item) {
        return withPlaceholders(guiStateUtils.getPlaceholderValues(state), item);
    }

    public List<net.kyori.adventure.text.Component> withPlaceholders(GUIState state, List<String> text) {
        return withPlaceholders(guiStateUtils.getPlaceholderValues(state), text);
    }

    public ItemStack withPlaceholders(Map<String, Object> placeholders, ItemStack item) {
        ItemMeta meta =  item.getItemMeta();
        ItemBuilder builder = ItemBuilder.from(item);
        if(meta.hasDisplayName()) {
            builder.name(withPlaceholders(placeholders, meta.getDisplayName()));
        }
        if(meta.hasLore()) {
            builder.lore(withPlaceholders(placeholders, meta.getLore()));
        }
        return builder.build();
    }

    public List<net.kyori.adventure.text.Component> withPlaceholders(Map<String, Object> placeholders, List<String> text) {
        return text.stream().map(line -> withPlaceholders(placeholders, line)).collect(Collectors.toList());
    }

    public net.kyori.adventure.text.Component withPlaceholders(Map<String, Object> placeholders, String text) {
        return AdventureMessage.of(this.placeholders, CompiledMessage.of(text))
                .with(placeholders)
                .component();
    }

    /*protected <T> PlaceholderResolver<T> getResolver(Map.Entry<String, T> entry) {
        return placeholdersFactory.getResolver(entry.getValue(), entry.getKey());
    }*/
}
