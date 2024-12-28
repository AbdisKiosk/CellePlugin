package dk.setups.celle.util;

import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PlayerSignDisallow {

    private final Map<UUID, Long> disallowed = new ConcurrentHashMap<>();

    public Collection<? extends Player> filter(Collection<? extends Player> players) {
        long time = System.currentTimeMillis();
        players = new HashSet<>(players);
        players.removeIf(player -> this.disallowed.getOrDefault(player.getUniqueId(), 0L) > time);
        return players;
    }

    public void disallow(UUID player) {
        long time = System.currentTimeMillis();
        long FIVE_SECONDS = 5000;
        this.disallowed.put(player, time + FIVE_SECONDS);
    }
}