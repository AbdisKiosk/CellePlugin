package dk.setups.celle;

import dev.triumphteam.gui.guis.Gui;
import dk.setups.celle.api.PlaceholderAPIExpansion;
import dk.setups.celle.cell.*;
import dk.setups.celle.command.completions.CellCompletion;
import dk.setups.celle.command.completions.CellGroupCompletion;
import dk.setups.celle.command.completions.OwnedCellCompletion;
import dk.setups.celle.command.completions.PermittedCellCompletion;
import dk.setups.celle.command.types.CellTypeResolver;
import dk.setups.celle.command.types.CellUserTypeResolver;
import dk.setups.celle.command.types.GroupTypeResolver;
import dk.setups.celle.command.types.ProtectedRegionResolver;
import dk.setups.celle.config.Config;
import dk.setups.celle.config.LangConfig;
import dk.setups.celle.database.CellGroupStore;
import dk.setups.celle.database.CellStore;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.gui.cell.CellAdminGUI;
import dk.setups.celle.gui.cell.logs.CellLogsGUI;
import dk.setups.celle.gui.region.CellsInRegionGUI;
import dk.setups.celle.task.TaskUpdateCells;
import dk.setups.celle.util.PlaceholderResolvers;
import dk.setups.celle.util.WorldGuardUtils;
import eu.okaeri.commands.Commands;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.serdes.SerdesBukkit;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import eu.okaeri.i18n.configs.LocaleConfig;
import eu.okaeri.i18n.minecraft.adventure.AdventureMessage;
import eu.okaeri.injector.Injector;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.platform.bukkit.OkaeriBukkitPlugin;
import eu.okaeri.platform.core.annotation.Bean;
import eu.okaeri.platform.core.annotation.Scan;
import eu.okaeri.platform.core.config.EmptyConfig;
import eu.okaeri.platform.core.i18n.message.MessageAssembler;
import eu.okaeri.platform.core.plan.ExecutionPhase;
import eu.okaeri.platform.core.plan.Planned;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

@Scan(deep = true, exclusions = "dk.setups.libs.celle")
public class CellePlugin extends OkaeriBukkitPlugin {

    @Bean
    public EmptyConfig loadLocaleConfig(LocaleConfig localeConfig) {
        return ConfigManager.create(EmptyConfig.class, (it) -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(new File(this.getDataFolder(), "lang"), "dk.yml"));
            it.withRemoveOrphans(false);
            it.saveDefaults();
            it.load(false);
        });
    }

    @Planned(ExecutionPhase.PRE_SETUP)
    public void saveLocaleConfig() {
        ConfigManager.create(LangConfig.class, (it) -> {
            it.withConfigurer(new YamlSnakeYamlConfigurer(), new SerdesBukkit());
            it.withBindFile(new File(new File(this.getDataFolder(), "lang"), "dk.yml"));
            it.withRemoveOrphans(false);
            it.saveDefaults();
            it.load(false);
        });
    }

    @Planned(ExecutionPhase.POST_SETUP)
    public void registerCommands(Injector injector, Commands commands) {
        commands.registerType(injector.createInstance(GroupTypeResolver.class));
        commands.registerType(injector.createInstance(CellTypeResolver.class));
        commands.registerType(injector.createInstance(CellUserTypeResolver.class));
        commands.registerType(injector.createInstance(ProtectedRegionResolver.class));

        commands.registerCompletion("cells", injector.createInstance(CellCompletion.class));
        commands.registerCompletion("cells:owned", injector.createInstance(OwnedCellCompletion.class));
        commands.registerCompletion("cells:permitted", injector.createInstance(PermittedCellCompletion.class));

        commands.registerCompletion("groups", injector.createInstance(CellGroupCompletion.class));
    }

    @Planned(ExecutionPhase.POST_SETUP)
    public void injectGuis(Injector injector, CellAdminGUI cellAdminGUI, CellLogsGUI cellLogsGUI,
                           CellsInRegionGUI cellsInRegionGUI) {
        injector.injectFields(cellAdminGUI);
        injector.injectFields(cellLogsGUI);
        injector.injectFields(cellsInRegionGUI);
    }

    @Planned(ExecutionPhase.PRE_SETUP)
    private void configureAdventureSupport(Injector injector) {
        injector.registerInjectable("messageAssembler", (MessageAssembler) AdventureMessage::of);
    }

    @Planned(ExecutionPhase.POST_SETUP)
    public void setupTasks(Injector injector, Config config) {
        injector.createInstance(TaskUpdateCells.class).runTaskTimerAsynchronously(this, 0L, config.getUpdateSignsDelayTick());
    }

    @Planned(ExecutionPhase.POST_SETUP)
    public void updateRegions(WorldGuardUtils utils, StoreManager stores, Logger logger) {
        for(Cell cell : stores.getCellStore().getAll()) {
            try {
                utils.updateRegion(cell);
            } catch(Exception ex) {
                logger.log(Level.FINE, "Failed to update for cell: " + cell.getName(), ex);
            }
        }
    }

    @Planned(ExecutionPhase.POST_SETUP)
    public void registerPlaceholders(StoreManager storeManager) {
        PlaceholderAPIExpansion placeholderAPIExpansion =
                new PlaceholderAPIExpansion(storeManager.getGroupStore(), storeManager.getCellStore(),
                        storeManager.getUserStore());
        placeholderAPIExpansion.register();
    }

    private PlaceholderResolvers resolvers;

    @Planned(ExecutionPhase.SETUP)
    public void registerPlaceholderParsers(Placeholders placeholders) {
        resolvers = new PlaceholderResolvers();
        resolvers.registerPlaceholderParsers(placeholders);
    }

    @Planned(ExecutionPhase.POST_SETUP)
    public void registerDependedPlaceholders(Injector injector) {
        injector.injectFields(resolvers);
    }


    @Planned(ExecutionPhase.SHUTDOWN)
    public void shutdownStores(StoreManager stores) throws Exception {
        stores.disconnect();
    }

    @Planned(ExecutionPhase.SHUTDOWN)
    public void shutdownTriumphGUI() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            InventoryView inventory = player.getOpenInventory();
            if(inventory == null) {
                continue;
            }
            if(inventory.getTopInventory() == null) {
                continue;
            }
            Inventory top = inventory.getTopInventory();
            if(top.getHolder() instanceof Gui) {
                player.closeInventory();
            }
        }
    }
}