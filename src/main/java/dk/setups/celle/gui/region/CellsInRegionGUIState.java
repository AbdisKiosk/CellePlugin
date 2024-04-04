package dk.setups.celle.gui.region;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.setups.celle.gui.state.GUIState;
import dk.setups.celle.gui.state.Placeholder;
import lombok.Getter;
import org.bukkit.entity.Player;

public class CellsInRegionGUIState extends GUIState {

    @Getter
    @Placeholder("region")
    private final ProtectedRegion region;

    public CellsInRegionGUIState(Player player, ProtectedRegion region) {
        super(player);
        this.region = region;
    }
}
