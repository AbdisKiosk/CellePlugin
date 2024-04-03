package dk.setups.celle.migrate;

import dk.setups.celle.cell.CellGroup;
import dk.setups.celle.util.cell.CellFactory;
import lombok.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@EqualsAndHashCode
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class MigrateCellGroup {

    private String name;
    private double rentPrice;
    private String rentPermission;
    private long rentTimeMillis;
    private long maxRentTimeMillis;
    private String[] unrentedSignLines;
    private String[] rentedNonMemberSignLines;
    private String[] rentedMemberSignLines;
    private String[] rentedOwnerSignLines;


    public MigrateCellGroup(double rentPrice, String rentPermission) {
        this.rentPrice = rentPrice;
        this.rentPermission = rentPermission;
    }

    public CellGroup toGroup(String defaultName, CellFactory factory) {
        CellGroup group = factory.createGroup(defaultName);
        if(rentPrice != 0) {
            group.setRentPrice(rentPrice);
        }
        if(rentTimeMillis != 0) {
            group.setRentTimeMillis(rentTimeMillis);
        }
        if(maxRentTimeMillis != 0) {
            group.setMaxRentTimeMillis(maxRentTimeMillis);
        }
        if(unrentedSignLines != null) {
            group.setUnrentedSignLines(Arrays.stream(unrentedSignLines).collect(Collectors.toList()));
        }
        if(rentedNonMemberSignLines != null) {
            group.setRentedNonMemberSignLines(Arrays.stream(rentedNonMemberSignLines).collect(Collectors.toList()));
        }
        if(rentedMemberSignLines != null) {
            group.setRentedMemberSignLines(Arrays.stream(rentedMemberSignLines).collect(Collectors.toList()));
        }
        if(rentedOwnerSignLines != null) {
            group.setRentedOwnerSignLines(Arrays.stream(rentedOwnerSignLines).collect(Collectors.toList()));
        }
        if(this.name != null) {
            group.setName(name);
        }
        return group;
    }
}
