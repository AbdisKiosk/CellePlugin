package dk.setups.celle.api;


import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellGroup;
import dk.setups.celle.database.CellGroupStore;
import dk.setups.celle.database.CellStore;
import dk.setups.celle.database.UserStore;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.stream.Collectors;

public class PlaceholderAPIExpansion extends PlaceholderExpansion {

    private final CellGroupStore cellGroupStore;
    private final CellStore cellStore;
    private final UserStore userStore;

    public PlaceholderAPIExpansion(CellGroupStore cellGroupStore, CellStore cellStore, UserStore userStore) {
        this.cellGroupStore = cellGroupStore;
        this.cellStore = cellStore;
        this.userStore = userStore;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "celler";
    }

    @Override
    public @NotNull String getAuthor() {
        return "skibdi";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        if(player != null) {
            if(params.equals("owned")) {
                return cellStore.getOwnedCells(userStore.get(player)).stream()
                        .map(Cell::getName)
                        .collect(Collectors.joining(","));
            }
        }

        String[] splitParams = params.split("_");
        if(splitParams.length != 2) {
            return null;
        }
        String arg = splitParams[0];
        if(!arg.equals("available") && !arg.equals("all")) {
            return null;
        }

        String groupName = splitParams[1];
        CellGroup group = cellGroupStore.get("name", groupName).orElse(null);
        if(group == null) {
            return null;
        }

        return group.getCells().stream()
                .filter(cell -> !arg.equals("available") || !cell.isRented())
                .map(Cell::getName)
                .collect(Collectors.joining(","));
    }

}
