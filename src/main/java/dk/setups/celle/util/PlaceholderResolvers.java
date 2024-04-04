package dk.setups.celle.util;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.setups.celle.cell.*;
import dk.setups.celle.cell.log.CellLog;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.placeholders.Placeholders;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceholderResolvers {

    private @Inject TimeFormat time;
    private @Inject NumberFormat number;

    public void registerPlaceholderParsers(Placeholders placeholders) {
        placeholders.registerPlaceholder(Cell.class, (c, __, ___) -> c.getName());
        placeholders.registerPlaceholder(Cell.class, "name", (c, __, ___) -> c.getName());
        placeholders.registerPlaceholder(Cell.class, "owner", (c, __, ___) -> c.getOwner());
        placeholders.registerPlaceholder(Cell.class, "group", (c, __, ___) -> c.getGroup());
        placeholders.registerPlaceholder(Cell.class, "region", (c, __, ___) -> c.getRegion());
        placeholders.registerPlaceholder(Cell.class, "members", (c, __, ___) -> c.getMembers().stream()
                .map(CellMember::getUser).map(CellUser::getName).collect(Collectors.joining(", ")));
        placeholders.registerPlaceholder(Cell.class, "time", (c, __, ___) -> c.getRentedUntil());
        placeholders.registerPlaceholder(Cell.class, "rent", (c, __, ___) -> c.getRentedUntil());
        placeholders.registerPlaceholder(Cell.class, "members", (c, __, ___) -> formatMembers(c.getMembers()));


        placeholders.registerPlaceholder(CellRegion.class, (c, __, ___) -> c.getName());
        placeholders.registerPlaceholder(CellRegion.class, "name", (c, __, ___) -> c.getName());

        placeholders.registerPlaceholder(CellUser.class, (c, __, ___) -> c.getName());
        placeholders.registerPlaceholder(CellUser.class, "name", (c, __, ___) -> c.getName());
        placeholders.registerPlaceholder(CellUser.class, "uuid", (c, __, ___) -> c.getUuid().toString());

        placeholders.registerPlaceholder(CellMember.class, (c, __, ___) -> c.getUser().getName());
        placeholders.registerPlaceholder(CellMember.class, "name", (c, __, ___) -> c.getUser().getName());
        placeholders.registerPlaceholder(CellMember.class, "uuid", (c, __, ___) -> c.getUser().getUuid().toString());

        placeholders.registerPlaceholder(CellGroup.class, (c, __, ___) -> c.getName());
        placeholders.registerPlaceholder(CellGroup.class, "name", (c, __, ___) -> c.getName());
        placeholders.registerPlaceholder(CellGroup.class, "price", (c, __, ___) -> c.getRentPrice());
        placeholders.registerPlaceholder(CellGroup.class, "permission", (c, __, ___) -> c.getRentPermission());
        placeholders.registerPlaceholder(CellGroup.class, "maxrenttime", (c, __, ___) -> c.getRentTimeMillis());
        placeholders.registerPlaceholder(CellGroup.class, "renttime", (c, __, ___) -> c.getRentTimeMillis());
        placeholders.registerPlaceholder(CellGroup.class, "maxrentedcells", (c, __, ___) -> c.getMaxRentedCells());
        placeholders.registerPlaceholder(CellGroup.class, "unrentedSignLines-1", (c, __, ___) -> c.getUnrentedSignLines().get(0));
        placeholders.registerPlaceholder(CellGroup.class, "unrentedSignLines-2", (c, __, ___) -> c.getUnrentedSignLines().get(1));
        placeholders.registerPlaceholder(CellGroup.class, "unrentedSignLines-3", (c, __, ___) -> c.getUnrentedSignLines().get(2));
        placeholders.registerPlaceholder(CellGroup.class, "unrentedSignLines-4", (c, __, ___) -> c.getUnrentedSignLines().get(3));
        placeholders.registerPlaceholder(CellGroup.class, "rentedNonMemberSignLines-1", (c, __, ___) -> c.getRentedNonMemberSignLines().get(0));
        placeholders.registerPlaceholder(CellGroup.class, "rentedNonMemberSignLines-2", (c, __, ___) -> c.getRentedNonMemberSignLines().get(1));
        placeholders.registerPlaceholder(CellGroup.class, "rentedNonMemberSignLines-3", (c, __, ___) -> c.getRentedNonMemberSignLines().get(2));
        placeholders.registerPlaceholder(CellGroup.class, "rentedNonMemberSignLines-4", (c, __, ___) -> c.getRentedNonMemberSignLines().get(3));
        placeholders.registerPlaceholder(CellGroup.class, "rentedMemberSignLines-1", (c, __, ___) -> c.getRentedMemberSignLines().get(0));
        placeholders.registerPlaceholder(CellGroup.class, "rentedMemberSignLines-2", (c, __, ___) -> c.getRentedMemberSignLines().get(1));
        placeholders.registerPlaceholder(CellGroup.class, "rentedMemberSignLines-3", (c, __, ___) -> c.getRentedMemberSignLines().get(2));
        placeholders.registerPlaceholder(CellGroup.class, "rentedMemberSignLines-4", (c, __, ___) -> c.getRentedMemberSignLines().get(3));
        placeholders.registerPlaceholder(CellGroup.class, "rentedOwnerSignLines-1", (c, __, ___) -> c.getRentedOwnerSignLines().get(0));
        placeholders.registerPlaceholder(CellGroup.class, "rentedOwnerSignLines-2", (c, __, ___) -> c.getRentedOwnerSignLines().get(1));
        placeholders.registerPlaceholder(CellGroup.class, "rentedOwnerSignLines-3", (c, __, ___) -> c.getRentedOwnerSignLines().get(2));
        placeholders.registerPlaceholder(CellGroup.class, "rentedOwnerSignLines-4", (c, __, ___) -> c.getRentedOwnerSignLines().get(3));


        placeholders.registerPlaceholder(CellLog.class, (c, __, ___) -> c.getAction().name());
        placeholders.registerPlaceholder(CellLog.class, "action", (c, __, ___) -> c.getAction().name());
        placeholders.registerPlaceholder(CellLog.class, "cell", (c, __, ___) -> c.getCell());
        placeholders.registerPlaceholder(CellLog.class, "actor", (c, __, ___) -> c.getActor());
        placeholders.registerPlaceholder(CellLog.class, "target", (c, __, ___) -> c.getTarget());
        placeholders.registerPlaceholder(CellLog.class, "time", (c, __, ___) -> c.getTimestamp());

        placeholders.registerPlaceholder(Double.class, "format-long", (c, __, ___) -> number.formatSimple(c));
        placeholders.registerPlaceholder(Double.class, "format-short", (c, __, ___) -> number.formatAbbreviated(c));

        placeholders.registerPlaceholder(Date.class, "format-long", (c, __, ___) -> time.formatDateLong(c));
        placeholders.registerPlaceholder(Date.class, "format-short", (c, __, ___) -> time.formatDateShort(c));
        placeholders.registerPlaceholder(Date.class, "left-long", (c, __, ___) -> time.formatLongLeft(c));
        placeholders.registerPlaceholder(Date.class, "left-short", (c, __, ___) -> time.formatConsiseLeft(c));

        placeholders.registerPlaceholder(ProtectedRegion.class, "name", (c, __, ___) -> c.getId());
    }

    private String formatMembers(Collection<CellMember> members) {
        if(members.isEmpty()) {
            return null;
        }
        List<String> names = members.stream()
                .map(CellMember::getUser)
                .map(CellUser::getName)
                .collect(Collectors.toList());

        return String.join(", ", names);
    }
}
