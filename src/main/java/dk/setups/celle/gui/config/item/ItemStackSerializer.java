package dk.setups.celle.gui.config.item;

import com.google.common.collect.Multimap;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import eu.okaeri.configs.schema.GenericsDeclaration;
import eu.okaeri.configs.serdes.DeserializationData;
import eu.okaeri.configs.serdes.ObjectSerializer;
import eu.okaeri.configs.serdes.SerializationData;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class ItemStackSerializer implements ObjectSerializer<ItemStack> {

    @Override
    public boolean supports(@NonNull Class<? super ItemStack> type) {
        return type.equals(ItemStack.class);
    }

    @Override
    public void serialize(@NonNull ItemStack item, @NonNull SerializationData data, @NonNull GenericsDeclaration generics) {
        if(item.getItemMeta() != null) {
            addSharedMeta(item.getItemMeta(), data, generics);
        }
        Optional<String> texture = getSkullTexture(item);
        if(texture.isPresent()) {
            data.add("texture", texture.get());
        } else {
            addItemData(item, data);
        }
    }

    private void addItemData(ItemStack item, SerializationData data) {
        data.add("material", item.getType().name());
        if(item.getDurability() != 0) {
            data.add("durability", item.getDurability());
        }
        if(item.getAmount() != 1) {
            data.add("amount", item.getAmount());
        }
    }

    private void addSharedMeta(ItemMeta meta, SerializationData data, GenericsDeclaration generics) {
        if(meta.hasDisplayName()) {
            data.add("displayName", meta.getDisplayName());
        }
        if(meta.hasLore()) {
            data.add("lore", meta.getLore());
        }
        if(meta.hasEnchants()) {
            data.add("enchants", meta.getEnchants());
        }
        if(!meta.getItemFlags().isEmpty()) {
            data.add("itemFlags", new ArrayList<>(meta.getItemFlags()));
        }
    }

    @SneakyThrows
    private Optional<String> getSkullTexture(ItemStack item) {
        if(item.getItemMeta() instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            Object profile = getProfile(meta);
            try {
                return Optional.of(getTexture(profile));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return Optional.empty();
    }

    @SneakyThrows
    private Object getProfile(SkullMeta meta) {
        Field profileField = meta.getClass().getDeclaredField("profile");
        profileField.setAccessible(true);
        return profileField.get(meta);
    }

    @SneakyThrows
    private String getTexture(Object profile) {
        Field propertiesField = profile.getClass().getDeclaredField("properties");
        propertiesField.setAccessible(true);
        Multimap<String, Object> properties = (Multimap<String, Object>) propertiesField.get(profile);

        Collection<Object> textures = properties.get("textures");
        if (textures == null || textures.isEmpty()) {
            return null;
        }

        Object textureProperty = textures.iterator().next();
        Field valueField = textureProperty.getClass().getDeclaredField("value");
        valueField.setAccessible(true);
        return (String) valueField.get(textureProperty);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ItemStack deserialize(@NonNull DeserializationData data, @NonNull GenericsDeclaration generics) {
        ItemStack item;
        if(data.containsKey("texture")) {
            item = getSkull(data);
        } else {
            item = getItem(data);
        }

        if(item.getItemMeta() != null) {
            item.setItemMeta(getMeta(item.getItemMeta(), data));
        }
        return item;
    }

    private ItemMeta getMeta(ItemMeta meta, DeserializationData data) {
        if(data.containsKey("displayName")) {
            meta.setDisplayName(data.get("displayName", String.class));
        }
        if(data.containsKey("lore")) {
            meta.setLore(data.get("lore", ArrayList.class));
        }
        if(data.containsKey("enchants")) {
            for(Object enchant : data.get("enchants", ArrayList.class)) {
                if(!(enchant instanceof String)) {
                    throw new IllegalArgumentException("Invalid enchant: " + enchant);
                }
                try {
                    meta.addEnchant(Enchantment.getByName(enchant.toString()), 1, true);
                } catch(Exception ex) {
                    throw new IllegalArgumentException("Invalid enchant: " + enchant);
                }
            }
        }
        if(data.containsKey("itemFlags")) {
            for(Object flag : data.get("itemFlags", ArrayList.class)) {
                if(!(flag instanceof String)) {
                    throw new IllegalArgumentException("Invalid item flag: " + flag);
                }
                try {
                    meta.addItemFlags(ItemFlag.valueOf(flag.toString()));
                } catch(Exception ex) {
                    throw new IllegalArgumentException("Invalid item flag: " + flag);
                }
            }
        }

        return meta;
    }

    private ItemStack getSkull(DeserializationData data) {
        String texture = data.get("texture", String.class);
        if(texture == null) {
            throw new IllegalArgumentException("Missing texture");
        }

        return ItemBuilder.skull().texture(texture).build();
    }

    private ItemStack getItem(DeserializationData data) {
        String material = data.get("material", String.class);
        int durability;
        int amount;
        if(data.containsKey("durability")) {
            durability = data.get("durability", Integer.class);
        } else {
            durability = 0;
        }
        if(data.containsKey("amount")) {
            amount = data.get("amount", Integer.class);
        } else {
            amount = 1;
        }

        try {
            return ItemBuilder.from(new ItemStack(Material.getMaterial(material), amount, (short) durability)).build();
        } catch(Exception ex) {
            throw new IllegalArgumentException("Invalid material: " + material);
        }
    }
}