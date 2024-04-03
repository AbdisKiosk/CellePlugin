package dk.setups.celle.gui.state;

import lombok.Getter;
import org.bukkit.entity.Player;

public class GUIState {

    @Getter
    @Placeholder("player")
    private final Player player;

    public GUIState(Player player) {
        this.player = player;
    }

}
