package dk.setups.celle.gui.config.item;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class ConfigGUIItem {

    private List<Integer> slots;
    private ItemStack item;

    public ConfigGUIItem(List<Integer> slots, ItemStack item) {
        this.slots = slots;
        this.item = item;
    }

    public List<Integer> getSlots() {
        return Collections.unmodifiableList(this.slots);
    }

    public ItemStack getItem() {
        return item.clone();
    }
}
