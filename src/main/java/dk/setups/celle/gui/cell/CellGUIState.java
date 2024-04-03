package dk.setups.celle.gui.cell;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.gui.state.GUIState;
import dk.setups.celle.gui.state.Placeholder;
import lombok.Getter;
import org.bukkit.entity.Player;

public class CellGUIState extends GUIState {

    @Getter
    @Placeholder("cell")
    private final Cell cell;

    public CellGUIState(Player player, Cell cell) {
        super(player);
        this.cell = cell;
    }
}
