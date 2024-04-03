package dk.setups.celle.util;

import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.injector.annotation.PostConstruct;
import eu.okaeri.platform.core.annotation.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.logging.Logger;

@Component
public class VaultUtils {

    private @Inject Logger logger;
    private Economy economy;

    @PostConstruct
    public void init(Plugin plugin) {
        RegisteredServiceProvider<Economy> economyProvider =
                plugin.getServer().getServicesManager().getRegistration(Economy.class);

        if(economyProvider == null) {
            logger.severe("Intet økonomiplugin fundet. Slår pluginnet fra.");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        economy = economyProvider.getProvider();
    }

    public boolean tryTakeMoney(Player player, double amount) {
        if(economy.getBalance(player) < amount) {
            return false;
        }
        return economy.withdrawPlayer(player, amount).transactionSuccess();
    }

    public void addMoney(Player player, double amount) {
        economy.depositPlayer(player, amount);
    }
}
