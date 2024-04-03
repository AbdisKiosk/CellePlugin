package dk.setups.celle.command.types;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import dk.setups.celle.util.WorldGuardUtils;
import eu.okaeri.commands.meta.ArgumentMeta;
import eu.okaeri.commands.service.CommandData;
import eu.okaeri.commands.service.Invocation;
import eu.okaeri.commands.type.resolver.BasicTypeResolver;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.entity.Player;

public class ProtectedRegionResolver extends BasicTypeResolver<ProtectedRegion> {

    private @Inject WorldGuardUtils worldGuard;

    @Override
    public boolean supports(@NonNull Class<?> type) {
        return type.equals(ProtectedRegion.class);
    }

    @Override
    public ProtectedRegion resolve(@NonNull Invocation invocation, @NonNull CommandData data, @NonNull ArgumentMeta argumentMeta, @NonNull String text) {
        Object sender = data.get("sender");
        if(!(sender instanceof Player)) {
            return null;
        }
        return worldGuard.getRegionByName(((Player) sender).getWorld(), text).orElse(null);
    }
}
