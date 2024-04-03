package dk.setups.celle.listener;

import dk.setups.celle.database.StoreManager;
import dk.setups.celle.util.PlayerSignDisallow;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

@Component
public class JoinListener implements Listener {

    private @Inject Plugin plugin;
    private @Inject StoreManager stores;
    private @Inject PlayerSignDisallow disallow;

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        disallow.disallow(event.getPlayer().getUniqueId());
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () ->  {
            stores.getUserStore().update(event.getPlayer());
        });
    }
}
