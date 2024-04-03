package dk.setups.celle.gui.config;

import dev.triumphteam.gui.components.GuiAction;
import dk.setups.celle.gui.config.item.ConfigGUIItem;
import eu.okaeri.configs.OkaeriConfig;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class GUIConfiguration extends OkaeriConfig {

    public abstract String getTitle();

    public abstract int getRows();

    public abstract LinkedHashMap<String, ConfigGUIItem> getItems();

}