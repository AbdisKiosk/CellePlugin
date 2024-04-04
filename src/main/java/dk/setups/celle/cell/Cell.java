package dk.setups.celle.cell;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import dk.setups.celle.sign.CellSign;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.bukkit.OfflinePlayer;

import java.util.Date;
import java.util.UUID;

@Getter @Setter @ToString
@EqualsAndHashCode(exclude = "members", callSuper = false)
@DatabaseTable(tableName = Cell.TABLE_NAME)
public class Cell {

    public static final String TABLE_NAME = "cells";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false, columnName = "name", unique = true)
    private String name;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true, columnName = "cell_group_id")
    private CellGroup group;
    @DatabaseField(canBeNull = true, foreign = true, foreignAutoCreate = true, foreignAutoRefresh = true, index = true, columnName = "owner_id")
    private CellUser owner;
    @ForeignCollectionField(eager = true)
    private ForeignCollection<CellMember> members;
    @DatabaseField(canBeNull = true, columnName = "rented_until")
    private Date rentedUntil;
    @DatabaseField(canBeNull = false, unique = true, columnName = "region_id", foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private CellRegion region;
    @DatabaseField(canBeNull = true, uniqueIndex = true, columnName = "sign_id", foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private CellSign sign;
    @DatabaseField(canBeNull = true, uniqueIndex = true, columnName = "teleport_id", foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
    private CellTeleport teleport;

    public Cell(String name, CellGroup group, CellRegion region) {
        this.name = name.toLowerCase();
        this.group = group;
        this.region = region;
    }

    public void rent(CellUser owner, Date until) {
        this.owner = owner;
        this.rentedUntil = until;

        members.clear();
    }

    public boolean isRented() {
        return rentedUntil != null && rentedUntil.after(new Date());
    }

    public void extend() {
        this.rentedUntil = getExpireIfExtended();
    }

    public void addMember(CellUser user) {
        if(members == null) {
            return;
        }
        members.add(new CellMember(user));
    }

    public void removeMember(CellUser user) {
        if(members == null) {
            return;
        }
        members.removeIf(member -> member.getUser().equals(user));
    }

    public boolean isPermitted(CellUser user) {
        return isPermitted(user.getUuid());
    }

    public boolean isPermitted(OfflinePlayer player) {
        return isPermitted(player.getUniqueId());
    }

    public boolean isPermitted(UUID uuid) {
        if(!isRented()) {
            return false;
        }
        if(isOwner(uuid)) {
            return true;
        }
        return members != null && members.stream().anyMatch(member -> member.getUser().getUuid().equals(uuid));
    }

    public void clearMembers() {
        if(members == null) {
            return;
        }
        members.clear();
    }

    public void unrent() {
        this.owner = null;
        this.rentedUntil = null;
        clearMembers();
    }

    public boolean isOwner(CellUser user) {
        return isOwner(user.getUuid());
    }

    public boolean isOwner(UUID uuid) {
        return owner != null && isRented() && owner.getUuid().equals(uuid);
    }

    public boolean canExtend() {
        return  rentedUntil == null
                || group.getMaxRentTimeMillis() > getExpireIfExtended().getTime() - System.currentTimeMillis();
    }

    public long getTimeLeftMs() {
        return rentedUntil.getTime() - System.currentTimeMillis();
    }

    public Date getExpireIfExtended() {
        return new Date(rentedUntil.getTime() + group.getRentTimeMillis());
    }

    Cell() {
    }
}