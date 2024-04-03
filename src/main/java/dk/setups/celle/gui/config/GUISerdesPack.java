package dk.setups.celle.gui.config;

import dk.setups.celle.gui.config.item.GUIItemSerializer;
import dk.setups.celle.gui.config.item.ItemStackSerializer;
import eu.okaeri.configs.serdes.OkaeriSerdesPack;
import eu.okaeri.configs.serdes.SerdesRegistry;
import lombok.NonNull;
import org.bukkit.inventory.ItemStack;

public class GUISerdesPack implements OkaeriSerdesPack {
    @Override
    public void register(@NonNull SerdesRegistry registry) {
        registry.registerExclusive(ItemStack.class, new ItemStackSerializer());
        registry.register(new GUIItemSerializer());
    }
}
