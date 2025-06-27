package dk.setups.celle.cell;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.misc.BaseDaoEnabled;
import com.j256.ormlite.table.DatabaseTable;
import dk.setups.celle.config.DefaultConfig;
import dk.setups.celle.database.persisters.StringListPersister;
import eu.okaeri.placeholders.message.CompiledMessage;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data @EqualsAndHashCode(callSuper = false)
@DatabaseTable(tableName = CellGroup.TABLE_NAME)
public class CellGroup {

    public static final String TABLE_NAME = "cell_groups";

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(canBeNull = false, uniqueIndex = true, columnName = "name")
    private String name;
    @DatabaseField(canBeNull = true, columnName = "rent_price")
    private Double rentPrice;
    @DatabaseField(canBeNull = true, columnName = "rent_time_millis")
    private Long rentTimeMillis;
    @DatabaseField(canBeNull = true, columnName = "max_rent_time_millis")
    private Long maxRentTimeMillis;
    @DatabaseField(canBeNull = true, columnName = "max_rented_cells")
    private Integer maxRentedCells;
    @DatabaseField(persisterClass = StringListPersister.class, canBeNull = true, columnName = "unrented_sign_lines")
    private List<String> unrentedSignLines;
    @DatabaseField(persisterClass = StringListPersister.class, canBeNull = true, columnName = "rented_non_member_sign_lines")
    private List<String> rentedNonMemberSignLines;
    @DatabaseField(persisterClass = StringListPersister.class, canBeNull = true, columnName = "rented_member_sign_lines")
    private List<String> rentedMemberSignLines;
    @DatabaseField(persisterClass = StringListPersister.class, canBeNull = true, columnName = "rented_owner_sign_lines")
    private List<String> rentedOwnerSignLines;
    @ForeignCollectionField(eager = false, maxEagerLevel = 4)
    private ForeignCollection<Cell> cells;

    public CellGroup(String name) {
        this.name = name;
    }

    public CellGroup(String name, double rentPrice, long rentTimeMillis, long maxRentTimeMillis,
                     int maxRentedCells,
                     List<String> unrentedSignLines, List<String> rentedNonMemberSignLines,
                     List<String> rentedMemberSignLines, List<String> rentedOwnerSignLines) {
        this.name = name.toLowerCase();
        this.rentPrice = rentPrice;
        this.rentTimeMillis = rentTimeMillis;
        this.maxRentTimeMillis = maxRentTimeMillis;
        this.maxRentedCells = maxRentedCells;
        this.unrentedSignLines = unrentedSignLines;
        this.rentedNonMemberSignLines = rentedNonMemberSignLines;
        this.rentedMemberSignLines = rentedMemberSignLines;
        this.rentedOwnerSignLines = rentedOwnerSignLines;
    }

    CellGroup() {
    }

    public Double getRentPrice() {
        return Optional.ofNullable(this.rentPrice).orElse(getDefaults().getRentPrice());
    }

    public String getRentPermission() {
        return "cell.rent." + getName().toLowerCase();
    }

    public String getExtendPermission() {
        return "cell.extend." + getName().toLowerCase();
    }

    public String getMemberPermission() {
        return "cell.member." + getName().toLowerCase();
    }

    public Long getRentTimeMillis() {
        return Optional.ofNullable(this.rentTimeMillis).orElse(getDefaults().getCellRentMillis());
    }

    public Long getMaxRentTimeMillis() {
        return Optional.ofNullable(this.maxRentTimeMillis).orElse(getDefaults().getMaxRentMillis());
    }

    public Integer getMaxRentedCells() {
        return Optional.ofNullable(this.maxRentedCells).orElse(getDefaults().getMaxCellsPerPlayer());
    }

    public List<String> getUnrentedSignLines() {
        return Optional.ofNullable(this.unrentedSignLines).orElse(getDefaults().getUnrentedSignLines());
    }

    public List<String> getRentedNonMemberSignLines() {
        return Optional.ofNullable(this.rentedNonMemberSignLines).orElse(getDefaults().getRentedNonMemberSignLines());
    }

    public List<String> getRentedMemberSignLines() {
        return Optional.ofNullable(this.rentedMemberSignLines).orElse(getDefaults().getRentedMemberSignLines());
    }

    public List<String> getRentedOwnerSignLines() {
        return Optional.ofNullable(this.rentedOwnerSignLines).orElse(getDefaults().getRentedOwnerSignLines());
    }

    public void setUnrentedSignLine(int line, String text) {
        List<String> lines = getOrCopy(this.unrentedSignLines, getDefaults().getUnrentedSignLines());
        lines.set(line, ChatColor.translateAlternateColorCodes('&', text));
        this.unrentedSignLines = lines;
    }

    public void setRentedNonMemberSignLine(int line, String text) {
        List<String> lines = getOrCopy(this.rentedNonMemberSignLines, getDefaults().getRentedNonMemberSignLines());
        lines.set(line, ChatColor.translateAlternateColorCodes('&', text));
        this.rentedNonMemberSignLines = lines;
    }

    public void setRentedMemberSignLine(int line, String text) {
        List<String> lines = getOrCopy(this.rentedMemberSignLines, getDefaults().getRentedMemberSignLines());
        lines.set(line, ChatColor.translateAlternateColorCodes('&', text));
        this.rentedMemberSignLines = lines;
    }

    public void setRentedOwnerSignLine(int line, String text) {
        List<String> lines = getOrCopy(this.rentedOwnerSignLines, getDefaults().getRentedOwnerSignLines());
        lines.set(line, ChatColor.translateAlternateColorCodes('&', text));
        this.rentedOwnerSignLines = lines;
    }


    private List<String> getOrCopy(List<String> list, List<String> def) {
        return list == null ? new ArrayList<>(def) : list;
    }

    protected static DefaultConfig getDefaults() {
        return DefaultConfig.getInstance();
    }

}