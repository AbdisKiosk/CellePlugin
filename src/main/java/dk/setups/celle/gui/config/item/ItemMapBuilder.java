package dk.setups.celle.gui.config.item;

import java.util.LinkedHashMap;

public class ItemMapBuilder {

    private final LinkedHashMap<String, ConfigGUIItem> items = new LinkedHashMap<>();

    public ItemMapBuilder addItem(String name, ConfigGUIItem item) {
        items.put(name, item);
        return this;
    }

    public LinkedHashMap<String, ConfigGUIItem> build() {
        return items;
    }
}
