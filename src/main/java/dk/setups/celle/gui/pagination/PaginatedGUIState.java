package dk.setups.celle.gui.pagination;

import dk.setups.celle.gui.state.GUIState;
import dk.setups.celle.gui.state.Placeholder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

public class PaginatedGUIState extends GUIState {

    @Getter
    @Setter
    @Placeholder("page")
    private int page = 1;

    @Getter
    @Setter
    private PaginationProvider<?> paginationProvider;

    public PaginatedGUIState(Player player) {
        super(player);
    }
}
