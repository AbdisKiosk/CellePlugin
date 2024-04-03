package dk.setups.celle.command;

import dk.setups.celle.database.StoreManager;
import dk.setups.celle.migrate.CellMigrator;
import dk.setups.celle.migrate.staxi.StaxiCellMigrator;
import dk.setups.celle.util.cell.CellFactory;
import eu.okaeri.commands.annotation.Arg;
import eu.okaeri.commands.annotation.Command;
import eu.okaeri.commands.annotation.Context;
import eu.okaeri.commands.annotation.Executor;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.injector.annotation.Inject;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Async
@Command(label = "celle-migrate")
@Permission("cell.admin.migrate")
public class MigrateCommand implements CommandService {
    private @Inject StoreManager stores;
    private @Inject CellFactory factory;

    private ExecutorService asyncExecutor = Executors.newFixedThreadPool(128);

    @Executor
    public void staxi(@Context CommandSender sender, @Arg String path) {
        sender.sendMessage("Migrerer " + path + " fra Staxi format");
        CellMigrator migrator = new StaxiCellMigrator(stores, factory, getSubFolder(path));
        migrator.migrate();
        sender.sendMessage("Migrerede " + path + " fra Staxi format");
    }


    private File getSubFolder(String path) {
        return new File(new File(Bukkit.getWorldContainer(), "plugins"), path);
    }
}
