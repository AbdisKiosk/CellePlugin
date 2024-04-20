package dk.setups.celle.util;

import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.RegionGroup;
import com.sk89q.worldguard.protection.flags.RegionGroupFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.setups.celle.cell.Cell;
import dk.setups.celle.cell.CellRegion;
import dk.setups.celle.config.Config;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Component
public class WorldGuardUtils {

    private @Inject Config config;

    public ProtectedRegion create(String name, CuboidRegion region) throws IllegalArgumentException {
        World world = Bukkit.getWorld(region.getWorld().getName());
        if(getRegionByName(world, name).isPresent()) {
            throw new IllegalArgumentException("Region already exists");
        }

        ProtectedCuboidRegion protectedRegion = new ProtectedCuboidRegion(name,
                region.getMinimumPoint().toBlockPoint(), region.getMaximumPoint().toBlockPoint());

        WorldGuardPlugin.inst().getRegionManager(world).addRegion(protectedRegion);
        return protectedRegion;
    }

    public void delete(CellRegion region) {
        WorldGuardPlugin.inst()
                .getRegionManager(Bukkit.getWorld(region.getRegionWorld())).removeRegion(region.getName());
    }

    public Set<ProtectedRegion> getRegionsAt(Location location) {
        return WorldGuardPlugin.inst()
                .getRegionManager(location.getWorld())
                .getApplicableRegions(location)
                .getRegions();
    }

    public Collection<ProtectedRegion> getRegionsIn(World world, ProtectedRegion region) {
        if(region.getId().equals("__global__")) {
            return WorldGuardPlugin.inst()
                    .getRegionManager(world)
                    .getRegions()
                    .values();
        }

         Collection<ProtectedRegion> all = WorldGuardPlugin.inst()
                .getRegionManager(world)
                .getRegions()
                .values();

         return region.getIntersectingRegions(all);
    }

    public Optional<ProtectedRegion> getRegionByName(World world, String name) {
        return Optional.ofNullable(WorldGuardPlugin.inst()
                .getRegionManager(world)
                .getRegion(name));
    }

    public void updateRegion(Cell cell) throws IllegalArgumentException {
        ProtectedRegion region = getRegion(cell.getRegion());

        updatePermissions(cell, region);
        updateFlags(region);
    }

    private ProtectedRegion getRegion(CellRegion cellRegion) throws IllegalArgumentException {
        ProtectedRegion region = WorldGuardPlugin.inst()
                .getRegionManager(Bukkit.getWorld(cellRegion.getRegionWorld()))
                .getRegion(cellRegion.getName());

        if(region == null) {
            throw new IllegalArgumentException("Region not found");
        }

        return region;
    }

    private void updatePermissions(Cell cell, ProtectedRegion region) {
        DefaultDomain members = new DefaultDomain();
        DefaultDomain owners = new DefaultDomain();

        if(cell.isRented()) {
            cell.getMembers().forEach(member -> owners.addPlayer(member.getUser().getUuid()));
            owners.addPlayer(cell.getOwner().getUuid());
        }

        owners.addGroup("cell.build");
        members.addGroup("cell.interact");

        region.setMembers(members);
        region.setOwners(owners);

        region.getFlags().clear();
    }

    private void updateFlags(ProtectedRegion region) {
        region.setFlag(DefaultFlag.WATER_FLOW, StateFlag.State.DENY);

        setMembersFlag(region, DefaultFlag.USE);
        setMembersFlag(region, DefaultFlag.INTERACT);
        setMembersFlag(region, DefaultFlag.CHEST_ACCESS);
        setOwnersFlag(region, DefaultFlag.BLOCK_PLACE);
        setOwnersFlag(region, DefaultFlag.BLOCK_BREAK);
        setOwnersFlag(region, DefaultFlag.BUILD);
    }

    private void setOwnersFlag(ProtectedRegion region, StateFlag flag) {
        region.setFlag(flag, StateFlag.State.ALLOW);
        RegionGroupFlag groupOnly = flag.getRegionGroupFlag();
        region.setFlag(groupOnly, RegionGroup.OWNERS);
    }

    private void setMembersFlag(ProtectedRegion region, StateFlag flag) {
        region.setFlag(flag, StateFlag.State.ALLOW);
        RegionGroupFlag groupOnly = flag.getRegionGroupFlag();
        region.setFlag(groupOnly, RegionGroup.MEMBERS);
    }

    public Optional<ProtectedRegion> getHighestPriority(Location location) {
        return WorldGuardPlugin.inst()
                .getRegionManager(location.getWorld())
                .getApplicableRegions(location)
                .getRegions().stream()
                .reduce((a, b) -> a.getPriority() > b.getPriority() ? a : b);
    }

    public boolean canBuild(Player player, Block block) {
        return WorldGuardPlugin.inst().canBuild(player, block);
    }
}