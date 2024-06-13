package dk.setups.celle.listener;

import dk.setups.celle.util.PlayerSignDisallow;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerRespawnEvent;

@Component
public class RespawnListener {

    private @Inject PlayerSignDisallow disallow;

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        disallow.disallow(event.getPlayer().getUniqueId());
    }

}