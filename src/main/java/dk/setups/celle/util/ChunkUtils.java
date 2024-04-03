package dk.setups.celle.util;

import org.bukkit.Location;

public class ChunkUtils {

    public static long toLong(Location location) {
        return toLong(location.getBlockX() >> 4, location.getBlockZ() >> 4);
    }

    public static long toLong(int x, int y) {
        return (long) x & 0xFFFFFFFFL | ((long) y & 0xFFFFFFFFL) << 32;
    }

    public static int asChunkX(int x) {
        return x >> 4;
    }

    public static int asChunkY(int y) {
        return y >> 4;
    }

}