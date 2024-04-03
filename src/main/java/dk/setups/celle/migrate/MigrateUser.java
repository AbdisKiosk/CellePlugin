package dk.setups.celle.migrate;

import lombok.Data;
import org.bukkit.Bukkit;

import java.util.UUID;

@Data
public class MigrateUser {

    private UUID uuid;
    private String name;

    public MigrateUser(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public static MigrateUser fromUUID(UUID uuid) {
        return new MigrateUser(uuid, Bukkit.getOfflinePlayer(uuid).getName());
    }
}
