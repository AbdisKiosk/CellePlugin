package dk.setups.celle.migrate.staxi;

import dk.setups.celle.cell.*;
import dk.setups.celle.migrate.MigrateCell;
import dk.setups.celle.migrate.MigrateCellGroup;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Getter @AllArgsConstructor @NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StaxiCell {

    private String name;
    private String permission;
    private double price;
    private Date rentedUntil;
    private UUID owner;
    private Location signAt;
    private Location teleport;
    private Set<UUID> members;

    public static StaxiCell from(YamlConfiguration yaml) {
        StaxiCell cell = new StaxiCell();
        cell.permission = yaml.getString("Permission");
        cell.price = yaml.getDouble("Pris");
        if(yaml.contains("Ejer") && !yaml.getString("Ejer").contains("Ingen")) {
            cell.rentedUntil = parseDate(yaml.getString("Dato"));
            cell.owner = UUID.fromString(yaml.getString("Ejer"));
        }
        cell.signAt = (Location) yaml.get("Sign");
        cell.teleport = (Location) yaml.get("Teleport");
        if(yaml.contains("Members")) {
            cell.members = yaml.getConfigurationSection("Members").getKeys(false)
                    .stream()
                    .map(UUID::fromString)
                    .collect(Collectors.toSet());
        }
        return cell;
    }

    private static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch(Exception e) {
            return new Date();
        }
    }

    public MigrateCell toCell(String name) {
        MigrateCellGroup group = new MigrateCellGroup(price, permission);
        group.setName(permission);
        String defaultWorld = Bukkit.getWorlds().get(0).getName();
        String worldName = signAt != null ? signAt.getWorld().getName() : defaultWorld;
        MigrateCell cell = new MigrateCell(name, new CellRegion(name.toLowerCase(), worldName), group);
        if(signAt != null) {
            cell.setSign(signAt);
        }
        if(rentedUntil != null) {
            cell.setRentedUntil(rentedUntil);
        }
        if(owner != null) {
            cell.setOwner(owner);
        }
        if(teleport != null) {
            cell.setTeleport(teleport);
        }
        if(members != null) {
            members.forEach(cell::addMember);
        }

        return cell;
    }
}