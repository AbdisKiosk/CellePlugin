package dk.setups.celle.migrate;

import dk.setups.celle.cell.*;
import dk.setups.celle.database.UserStore;
import dk.setups.celle.sign.CellSign;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@AllArgsConstructor @Getter @Setter @NoArgsConstructor
public class MigrateCell {

    private MigrateCellGroup group;
    private String name;
    private UUID owner;
    private Date rentedUntil;
    private CellRegion region;
    private Location teleport;
    private Location sign;
    private Set<UUID> members = new HashSet<>();

    public MigrateCell(String name, CellRegion region, MigrateCellGroup group) {
        this.name = name;
        this.group = group;
        this.region = region;
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public Cell toCell(CellGroup group, UserStore users) {
        Cell cell = new Cell(name, group, region);
        if(owner != null) {
            cell.setOwner(users.get(owner, Bukkit.getOfflinePlayer(owner).getName()));
        }
        if(rentedUntil != null) {
            cell.setRentedUntil(rentedUntil);
        }
        if(teleport != null) {
            cell.setTeleport(new CellTeleport(teleport.getBlockX(), teleport.getBlockY(), teleport.getBlockZ(), teleport.getYaw(), teleport.getPitch(), teleport.getWorld().getName()));
        }
        if(sign != null) {
            cell.setSign(new CellSign(sign.getBlockX(), sign.getBlockY(), sign.getBlockZ(), sign.getWorld().getName()));
        }
        users.getStores().getCellStore().persist(cell);
        Cell fetched = users.getStores().getCellStore().getFromName(cell.getName()).get();
        members.forEach(uuid -> fetched.addMember(users.get(uuid, Bukkit.getOfflinePlayer(uuid).getName())));
        return cell;
    }
}