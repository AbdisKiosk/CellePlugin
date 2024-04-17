package dk.setups.celle.gui.region;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import dk.setups.celle.cell.Cell;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.gui.ConfigGuiItemBuilder;
import dk.setups.celle.gui.ConfigurableGUI;
import dk.setups.celle.gui.GUIPlaceholderUtils;
import dk.setups.celle.gui.config.GUISerdesPack;
import dk.setups.celle.gui.config.item.ConfigGUIItem;
import dk.setups.celle.gui.config.item.ItemMapBuilder;
import dk.setups.celle.gui.state.GUIState;
import dk.setups.celle.util.WorldGuardUtils;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Configuration(path = "guis/cellsinregion.yml", provider = YamlSnakeYamlConfigurer.class, serdes = GUISerdesPack.class)
@SuppressWarnings({"FieldMayBeFinal", "deprecation"})
public class CellsInRegionGUI extends ConfigurableGUI<CellsInRegionGUIState> {

    @Getter
    private String title = "Celler i {region.name}";
    @Getter
    private int rows = 6;
    @Getter
    private LinkedHashMap<String, ConfigGUIItem> items = new ItemMapBuilder()
            .addItem("decoration", new ConfigGuiItemBuilder()
                    .setItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 15))
                    .setSlots(IntStream.range(0, 9), IntStream.range(45, 54))
                    .build())
            .build();

    private List<Integer> cellItemSlots = IntStream.range(9, 45).boxed().collect(Collectors.toList());
    private ItemStack cellItem = ItemBuilder.from(Material.IRON_DOOR)
            .setName("§7{cell.name}")
            .setLore(" §7§l» §7Pris: §a${cell.group.price.format-long}")
            .build();

    private transient @Inject StoreManager store;
    @Getter
    private transient @Inject GUIPlaceholderUtils placeholderUtils;
    private transient @Inject WorldGuardUtils worldGuard;



    @Override
    protected void addItems(CellsInRegionGUIState state, BaseGui gui) {
        super.addItems(state, gui);
        Collection<ProtectedRegion> childRegions = worldGuard.getRegionsIn(state.getPlayer().getWorld(), state.getRegion());
        List<Cell> unrented = store.getCellStore().getCellsInRegions(childRegions)
                .stream()
                .filter(c -> !c.isRented())
                .collect(Collectors.toList());

        for(int i = 0; i < unrented.size(); i++) {
            if(i > cellItemSlots.size()) {
                return;
            }
            gui.setItem(cellItemSlots.get(i), new GuiItem(createCellItem(unrented.get(i), state)));
        }
    }

    protected ItemStack createCellItem(Cell cell, GUIState state) {
        return placeholderUtils.withPlaceholders(state, Collections.singletonMap("cell", cell), cellItem.clone());
    }

    @Override
    public void addClickEvents(Map<String, GuiAction<InventoryClickEvent>> events, CellsInRegionGUIState state) {}
}
