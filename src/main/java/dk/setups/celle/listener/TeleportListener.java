package dk.setups.celle.listener;

import dk.setups.celle.util.PlayerSignDisallow;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

@Component
public class TeleportListener implements Listener {

    private @Inject PlayerSignDisallow disallow;

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        disallow.disallow(event.getPlayer().getUniqueId());
    }
}
