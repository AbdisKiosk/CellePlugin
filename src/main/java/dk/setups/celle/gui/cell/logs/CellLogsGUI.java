package dk.setups.celle.gui.cell.logs;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiAction;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import dk.setups.celle.cell.log.CellLog;
import dk.setups.celle.cell.log.CellLogFilter;
import dk.setups.celle.command.CellAdminCommand;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.gui.ConfigGuiItemBuilder;
import dk.setups.celle.gui.ConfigurableGUI;
import dk.setups.celle.gui.GUIPlaceholderUtils;
import dk.setups.celle.gui.config.GUISerdesPack;
import dk.setups.celle.gui.config.item.ConfigGUIItem;
import dk.setups.celle.gui.config.item.ItemMapBuilder;
import dk.setups.celle.gui.pagination.PaginationProvider;
import dk.setups.celle.gui.state.GUIState;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@SuppressWarnings({"FieldMayBeFinal", "deprecation"})
@Configuration(path = "guis/celllogs.yml", provider = YamlSnakeYamlConfigurer.class, serdes = GUISerdesPack.class)
public class CellLogsGUI extends ConfigurableGUI<CellLogsGUIState> {

    private String title = "Celle logs";
    private int rows = 6;
    private List<Integer> paginationSlots = IntStream.range(9, 45).boxed().collect(Collectors.toList());

    private ConfigGUIItem logItem = new ConfigGuiItemBuilder()
            .setItem(ItemBuilder.from(Material.PAPER)
                    .setName("§7{log.time.format-long}")
                    .setLore(
                            "",
                            " §7§l» §7Log: §f{log.action}",
                            " §7§l» §7Celle: §f{log.cell.name}",
                            " §7§l» §7Spiller: §f{log.actor.name|§cIngen...}",
                            " §7§l» §7Target: §f{log.target.name|§cIngen...}"
                    )
                    .build())
            .setSlots(Collections.emptyList())
            .build();

    private ConfigGUIItem emptyItem = new ConfigGuiItemBuilder()
            .setItem(ItemBuilder.from(Material.BARRIER)
                    .setName("§cIngen logs")
                    .setLore(
                            "",
                            " §7§l» §cDer ikke flere logs."
                    )
                    .build())
            .setSlots(Collections.emptyList())
            .build();

    private LinkedHashMap<String, ConfigGUIItem> items = new ItemMapBuilder()
            .addItem("decoration", new ConfigGuiItemBuilder()
                    .setItem(ItemBuilder.from(Material.STAINED_GLASS_PANE).color(Color.BLUE).build())
                    .setSlots(IntStream.range(0, 9), IntStream.range(45, 54))
                    .build())
            .addItem("nextpage", new ConfigGuiItemBuilder()
                    .setItem(ItemBuilder.from(Material.ARROW)
                        .setName("§aNæste side")
                        .setLore(
                                "",
                                " §7§l» §fKlik for at se næste side."
                        )
                        .build())
                    .setSlot(5, 6)
                    .build())
            .addItem("prevpage", new ConfigGuiItemBuilder()
                    .setItem(ItemBuilder.from(Material.ARROW)
                            .setName("§aForrige side")
                            .setLore(
                                    "",
                                    " §7§l» §fKlik for at se forrige side."
                            )
                            .build())
                    .setSlot(5, 2)
                    .build())
            .build();

    private transient @Inject GUIPlaceholderUtils placeholderUtils;
    private transient @Inject Placeholders placeholders;
    private transient @Inject StoreManager stores;

    @Override
    protected void addItems(CellLogsGUIState state, BaseGui gui) {
        super.addItems(state, gui);

        state.setPaginationProvider(
                new PaginationProvider<>(gui, new LogsGUIElementProvider(stores.getLogStore(), state.getFilter()),
                        paginationSlots, log -> generateLogItem(state, log)));

        state.getPaginationProvider().update();
    }

    @Override
    public void addClickEvents(Map<String, GuiAction<InventoryClickEvent>> events, CellLogsGUIState state) {
        events.put("nextpage", event -> nextPage(state));
        events.put("prevpage", event -> prevPage(state));
    }

    protected void prevPage(CellLogsGUIState state) {
        state.getPaginationProvider().setPage(getPage(state, -1));
        state.setPage(getPage(state, -1));
    }

    protected void nextPage(CellLogsGUIState state) {
        state.getPaginationProvider().setPage(getPage(state, 1));
        state.setPage(getPage(state, 1));
    }

    protected int getPage(CellLogsGUIState state, int offset) {
        return Math.max(1, offset + state.getPage());
    }

    public GuiItem generateLogItem(CellLogsGUIState state, Optional<CellLog> optionalLog) {
        return optionalLog.map(cellLog -> generateLogItem(state, cellLog)).orElseGet(() -> generateEmptyItem(state));
    }

    public GuiItem generateLogItem(CellLogsGUIState state, CellLog log) {
        ItemStack item = placeholderUtils.withPlaceholders(state, Collections.singletonMap("log", log), logItem.getItem());

        return new GuiItem(item);
    }

    public GuiItem generateEmptyItem(CellLogsGUIState state) {
        ItemStack item = placeholderUtils.withPlaceholders(state, emptyItem.getItem());

        return new GuiItem(item);
    }
}
