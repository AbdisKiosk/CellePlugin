package dk.setups.celle.util;

import dk.setups.celle.config.Config;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.OfflinePlayer;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CooldownUtil {

    private @Inject Config config;

    private final Map<UUID, Long> playerByAdded = new ConcurrentHashMap<>();

    public boolean isOnCooldown(OfflinePlayer player) {
        return playerByAdded.getOrDefault(player.getUniqueId(), 0L) > System.currentTimeMillis();
    }

    public void setCooldown(OfflinePlayer player) {
        setCooldown(player, config.getCooldownMs());
    }

    public void setCooldown(OfflinePlayer player, long cooldown) {
        playerByAdded.put(player.getUniqueId(), System.currentTimeMillis() + cooldown);
    }

    public boolean checkCooldown(OfflinePlayer player) {
        if(isOnCooldown(player)) {
            return true;
        }
        setCooldown(player);
        return false;
    }

}
