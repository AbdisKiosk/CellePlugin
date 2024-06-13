package dk.setups.celle.listener;

import dk.setups.celle.util.PlayerSignDisallow;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

@Component
public class DeathListener implements Listener {

    private @Inject PlayerSignDisallow disallow;

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        disallow.disallow(event.getEntity().getUniqueId());
    }

}