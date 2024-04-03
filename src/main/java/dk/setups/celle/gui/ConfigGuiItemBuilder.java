package dk.setups.celle.gui;

import dk.setups.celle.gui.config.item.ConfigGUIItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ConfigGuiItemBuilder {

    private ItemStack item = new ItemStack(Material.AIR);
    private final List<Integer> slots = new ArrayList<>();

    public ConfigGuiItemBuilder setItem(ItemStack item) {
        this.item = item;
        return this;
    }

    public ConfigGuiItemBuilder setSlots(List<Integer> slots) {
        this.slots.addAll(slots);
        return this;
    }

    public ConfigGuiItemBuilder setSlots(IntStream... streams) {
        for (IntStream stream : streams) {
            stream.forEach(this.slots::add);
        }
        return this;
    }

    public ConfigGuiItemBuilder setSlots(int... slots) {
        for (int slot : slots) {
            this.slots.add(slot);
        }
        return this;
    }

    public ConfigGuiItemBuilder setSlot(int row, int slot) {
        this.slots.add(row * 9 + slot);
        return this;
    }

    public ConfigGUIItem build() {
        return new ConfigGUIItem(this.slots, this.item);
    }
}
