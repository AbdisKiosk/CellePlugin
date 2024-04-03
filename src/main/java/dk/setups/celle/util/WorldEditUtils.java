package dk.setups.celle.util;

import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.regions.Region;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.entity.Player;

import java.util.Optional;

@Component
public class WorldEditUtils {

    public Optional<CuboidRegion> getSelection(Player player) {
        LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(player.getName());
        if(session == null) {
            return Optional.empty();
        }
        Region selection;
        try {
            selection = session.getSelection(session.getSelectionWorld());
        } catch(IncompleteRegionException | NullPointerException __) {
            return Optional.empty();
        }
        if(!(selection instanceof CuboidRegion)) {
            return Optional.empty();
        }
        return Optional.of((CuboidRegion) selection);
    }
}
