package dk.setups.celle.gui.cell.logs;

import dk.setups.celle.cell.log.CellLogFilter;
import dk.setups.celle.gui.pagination.PaginatedGUIState;
import lombok.Getter;
import org.bukkit.entity.Player;

public class CellLogsGUIState extends PaginatedGUIState {

    @Getter
    private final CellLogFilter filter;

    public CellLogsGUIState(Player player, CellLogFilter filter) {
        super(player);
        this.filter = filter;
    }
}
