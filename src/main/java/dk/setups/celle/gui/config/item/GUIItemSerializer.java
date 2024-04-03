package dk.setups.celle.gui.config.item;

import com.google.common.collect.Multimap;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.*;

public class GUIItemSerializer implements ObjectSerializer<ConfigGUIItem> {

    @Override
    public boolean supports(@NonNull Class<? super ConfigGUIItem> type) {
        return type.equals(ConfigGUIItem.class);
    }

    @Override
    public void serialize(@NonNull ConfigGUIItem object, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        data.add("item", object.getItem());
        data.add("slots", object.getSlots());
    }

    @Override
    @SuppressWarnings("unchecked")
    public ConfigGUIItem deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        ItemStack item = data.get("item", ItemStack.class);
        List<Integer> slots;
        try {
            slots = (List<Integer>) data.get("slots", ArrayList.class);
        } catch(ClassCastException ex) {
            throw new IllegalArgumentException("Invalid slots type: " + data.get("slots", Object.class).toString());
        }
        return new ConfigGUIItem(slots, item);
    }
}