package dk.setups.celle.command;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellMember;
import dk.setups.celle.cell.CellUser;
import dk.setups.celle.config.LangConfig;
import dk.setups.celle.database.StoreManager;
import dk.setups.celle.util.cell.CellAPI;
import dk.setups.celle.util.TimeFormat;
import dk.setups.celle.util.WorldGuardUtils;
import dk.setups.celle.util.cell.EventSuccess;
import eu.okaeri.commands.Commands;
import eu.okaeri.commands.annotation.*;
import eu.okaeri.commands.bukkit.annotation.Async;
import eu.okaeri.commands.bukkit.annotation.Permission;
import eu.okaeri.commands.service.CommandService;
import eu.okaeri.commands.service.Option;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.bukkit.annotation.Chain;
import eu.okaeri.platform.bukkit.i18n.BI18n;
import eu.okaeri.platform.bukkit.i18n.message.BukkitMessageDispatcher;
import eu.okaeri.tasker.core.chain.TaskerChain;
import org.bukkit.entity.Player;

import java.util.*;

@Async
@Command(label = "#{commandCellLabel}", description = "${commandCellDescription}", aliases = {"#{commandCellAlias}"})
public class CellCommand implements CommandService {

    private @Inject("lang") BI18n i18n;
    private @Inject LangConfig lang;
    private @Inject StoreManager stores;
    private @Inject CellAPI api;
    private @Inject TimeFormat timeFormat;
    private @Inject WorldGuardUtils worldGuard;
    private @Inject Commands commands;

    @Permission("cell.command.unrent")
    @Completion(arg = "cell", value = "@cells:owned")
    @Executor(pattern = {"#{commandCellUnrentAlias}"}, description = "${commandCellUnrentDescription}", usage = "${commandCellUnrentUsage}")
    public void unrent(@Context Player player, @Arg("cell") Option<Cell> cellOption) {
        Cell cell = cellOption.orElseGet(() -> api.getCellAtLocation(player.getLocation()).orElse(null));
        if(cell == null) {
            player.sendMessage("§cDu skal skrive en celle");
            return;
        }

        if(!cell.isOwner(stores.getUserStore().get(player)) && !player.hasPermission("cell.command.unrent.other")) {
            i18n.get(lang.getCommandCellUnrentNotRented()).with("cell", cell).sendTo(player);
            return;
        }

        if(api.unrentCell(cell, stores.getUserStore().get(player)) == EventSuccess.CANCELLED) {
            return;
        }

        i18n.get(lang.getCommandCellUnrentSuccess()).with("cell", cell).sendTo(player);
    }

    @Permission("cell.command.member.add")
    @Completion(arg = "cell", value = "@cells:owned") @Completion(arg = "target", value = "@bukkit:player:name")
    @Executor(pattern = {"#{commandCellMemberAddAlias}"}, description = "${commandCellMemberAddDescription}", usage = "${commandCellMemberAddUsage}")
    public void addMember(@Context Player sender, @Arg Player target, @Arg("cell") Option<Cell> cellOption) {
        Cell cell = cellOption.orElseGet(() -> api.getCellAtLocation(sender.getLocation()).orElse(null));
        if(cell == null) {
            sender.sendMessage("§cDu skal skrive en celle");
            return;
        }
        if(!sender.hasPermission("cell.command.member.add.other") && !cell.isOwner(stores.getUserStore().get(sender))) {
            i18n.get(lang.getCommandCellMemberAddNotOwner()).with("cell", cell).sendTo(sender);
            return;
        }

        if(!target.hasPermission(cell.getGroup().getMemberPermission())
                && !target.hasPermission(cell.getGroup().getRentPermission())) {
            i18n.get(lang.getCommandCellMemberAddCannotBeMember())
                    .with("target", target)
                    .with("cell", cell)
                    .sendTo(sender);
            return;
        }

        if(cell.isPermitted(target)) {
            i18n.get(lang.getCommandCellMemberAddAlreadyMember())
                    .with("target", target)
                    .with("cell", cell)
                    .sendTo(sender);
            return;
        }

        if(api.addMember(cell, stores.getUserStore().get(sender), target) == EventSuccess.CANCELLED) {
            return;
        }

        i18n.get(lang.getCommandCellMemberAddSuccess())
                .with("target", target)
                .with("cell", cell)
                .sendTo(sender);
    }

    @Permission("cell.command.member.remove")
    @Completion(arg = "cell", value = "@cells:owned")
    @Executor(pattern = {"#{commandCellMemberRemoveAlias}"}, description = "${commandCellMemberRemoveDescription}", usage = "${commandCellMemberRemoveUsage}")
    public void removeMember(@Context Player sender, @Arg CellUser target, @Arg("cell") Option<Cell> cellOption) {
        Cell cell = cellOption.orElseGet(() -> api.getCellAtLocation(sender.getLocation()).orElse(null));
        if(cell == null) {
            sender.sendMessage("§cDu skal skrive en celle");
            return;
        }
        if(!sender.hasPermission("cell.command.member.remove.other") && !cell.isOwner(stores.getUserStore().get(sender))) {
            i18n.get(lang.getCommandCellMemberRemoveNotOwner())
                    .with("cell", cell)
                    .sendTo(sender);
            return;
        }

        if(!cell.isPermitted(target)) {
            i18n.get(lang.getCommandCellMemberRemoveNotMember())
                    .with("target", target)
                    .with("cell", cell)
                    .sendTo(sender);
            return;
        }

        if(api.removeMember(cell, stores.getUserStore().get(sender), target) == EventSuccess.CANCELLED) {
            return;
        }

        i18n.get(lang.getCommandCellMemberRemoveSuccess())
                .with("target", target)
                .with("cell", cell)
                .sendTo(sender);
    }

    @Permission("cell.command.info")
    @Completion(arg = "cell", value = "@cells:permitted")
    @Executor(pattern = {"#{commandCellInfoAlias}"}, description = "${commandCellInfoDescription}", usage = "${commandCellInfoUsage}")
    public void info(@Context Player player, @Arg("cell") Option<Cell> cellOption) {
        Cell cell = cellOption.orElseGet(() -> api.getCellAtLocation(player.getLocation()).orElse(null));
        if(cell == null) {
            player.sendMessage("§cDu skal skrive en celle");
            return;
        }

        CellUser user = stores.getUserStore().get(player);
        if(!cell.isOwner(user) && !cell.isPermitted(user)) {
            if(!player.hasPermission("cell.command.info.other")) {
                i18n.get(lang.getCommandCellInfoOtherNoPermission()).with("cell", cell).sendTo(player);
                return;
            }
        }
        i18n.get(lang.getCommandCellInfoHeader()).with("cell", cell).sendTo(player);

        if(!cell.isRented()) {
            i18n.get(lang.getCommandCellInfoNotRented())
                    .with("cell", cell)
                    .sendTo(player);
            return;
        }


        BukkitMessageDispatcher msg = i18n.get(lang.getCommandCellInfoMessage())
                .with("cell", cell);

        msg.sendTo(player);
    }

    @Permission("cell.command.list")
    @Executor(pattern = {"#{commandCellListSelfAlias}"}, description = "${commandCellListSelfDescription}", usage = "${commandCellListSelfUsage}")
    public void list(@Context Player player) {
        CellUser user = stores.getUserStore().get(player);
        Set<Cell> cells = new HashSet<>(stores.getCellStore().getPermittedCells(user));

        if(cells.isEmpty()) {
            i18n.get(lang.getCommandCellListSelfNoCells()).sendTo(player);
            return;
        }

        i18n.get(lang.getCommandCellListSelfHeader()).sendTo(player);
        cells.forEach(cell -> i18n.get(lang.getCommandCellListSelfCell()).with("cell", cell).sendTo(player));
    }

    @Permission("cell.command.list.other")
    @Completion(arg = "target", value = "@bukkit:player:name")
    @Executor(pattern = {"#{commandCellListOtherAlias}"}, description = "${commandCellListOtherDescription}", usage = "${commandCellListOtherUsage}")
    public void listOther(@Context Player player, @Arg CellUser target) {
        Collection<Cell> cells = stores.getCellStore().getPermittedCells(target);
        if(cells.isEmpty()) {
            i18n.get(lang.getCommandCellListOtherNoCells()).with("player", target.getName()).sendTo(player);
            return;
        }

        i18n.get(lang.getCommandCellListOtherHeader()).with("player", target.getName()).sendTo(player);
        cells.forEach(cell -> i18n.get(lang.getCommandCellListOtherCell()).with("cell", cell).sendTo(player));
    }

    @Executor(pattern = {"#{commandCellTeleportAlias}"}, description = "${commandCellTeleportDescription}", usage = "${commandCellTeleportUsage}")
    @Permission("cell.command.teleport")
    @Completion(arg = "cell", value = "@cells:permitted")
    public void teleport(@Context Player player, @Arg Cell cell, @Chain TaskerChain<?> chain) {
        if(!cell.isPermitted(player) && !player.hasPermission("cell.command.teleport.other")) {
            i18n.get(lang.getCommandCellTeleportNotMember()).with("cell", cell).sendTo(player);
            return;
        }

        if(cell.getTeleport() == null) {
            i18n.get(lang.getCommandCellTeleportNoTeleport()).with("cell", cell).sendTo(player);
            return;
        }

        chain.sync(() -> player.teleport(cell.getTeleport().asBukkit())).execute();
    }

}