package dk.setups.celle.listener;

import dk.setups.celle.config.Config;
import dk.setups.celle.util.WorldGuardUtils;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;

@Component
public class IronDoorListener implements Listener {

    private @Inject Config config;
    private @Inject WorldGuardUtils worldGuard;

    @EventHandler
    public void onIronDoorOpen(PlayerInteractEvent event) {
        Block clicked = event.getClickedBlock();

        if(!config.isIronDoorOpen()) {
            return;
        }
        if(!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            return;
        }

        if(isUpperHalf(clicked)) {
            clicked = clicked.getRelative(0, -1, 0);
        }

        if(!worldGuard.canBuild(event.getPlayer(), clicked)) {
            return;
        }
        if(!(clicked.getState().getData() instanceof Openable)) {
            return;
        }
        if(!isIronDoor(clicked)) {
            return;
        }

        setOpened(clicked.getState());
    }

    private boolean isIronDoor(Block block) {
        return block.getType().equals(Material.IRON_DOOR) || block.getType().equals(Material.IRON_DOOR_BLOCK);
    }

    private boolean setOpened(BlockState state) {
        Openable door = (Openable) state.getData();

        boolean setOpen = !door.isOpen();
        door.setOpen(setOpen);

        state.setData((MaterialData) door);
        state.update();

        return setOpen;
    }

    @SuppressWarnings("deprecation")
    private boolean isUpperHalf(Block door) {
        return door.getState().getData().getData() >= 8;
    }

    private boolean isValidDoor(Block block) {
        if(block == null) {
            return false;
        }

        if(!block.getType().equals(Material.IRON_DOOR) || !block.getType().equals(Material.IRON_DOOR_BLOCK)) {
            return false;
        }

        return block.getState().getData() instanceof Openable;
    }
}
