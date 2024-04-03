package dk.setups.celle.util;  import org.bukkit.Location; import org.bukkit.entity.Player;

import java.util.*;

public class NearbyPlayerMap {

    private final Map<Long, Collection<Player>> chunkToPlayer = new HashMap<>();

    public NearbyPlayerMap() {}

    public static NearbyPlayerMap from(Collection<? extends Player> players) {
        NearbyPlayerMap map = new NearbyPlayerMap();
        players.forEach(map::add);
        return map;
    }

    public void add(Player player) {
        chunkToPlayer.compute(ChunkUtils.toLong(player.getLocation()), (k, v) -> {
            if (v == null) {
                v = new HashSet<>();
            }
            v.add(player);
            return v;
        });
    }


    public Collection<? extends Player> getNearbyPlayers(Location location) {
        int CHUNK_VIEW_DISTANCE = 2;
        int chunkX = ChunkUtils.asChunkX(location.getBlockX());
        int chunkZ = ChunkUtils.asChunkY(location.getBlockZ());

        Collection<Player> players = new HashSet<>();
        for(int x = chunkX - CHUNK_VIEW_DISTANCE; x <= chunkX + CHUNK_VIEW_DISTANCE; x++) {
            for(int z = chunkZ - CHUNK_VIEW_DISTANCE; z <= chunkZ + CHUNK_VIEW_DISTANCE; z++) {
                players.addAll(chunkToPlayer.getOrDefault(ChunkUtils.toLong(x, z), Collections.emptySet()));
            }
        }

        return players;
    }
}