package dk.setups.celle.gui.group;

import dk.setups.celle.cell.CellGroup;
import dk.setups.celle.gui.state.GUIState;
import dk.setups.celle.gui.state.Placeholder;
import lombok.Getter;
import org.bukkit.entity.Player;

public class CellGroupGUIState extends GUIState {

    @Getter
    @Placeholder("group")
    private final CellGroup group;

    public CellGroupGUIState(Player player, CellGroup group) {
        super(player);
        this.group = group;
    }
}
