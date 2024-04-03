package dk.setups.celle.command.types;

import dk.setups.celle.cell.CellUser;
import dk.setups.celle.database.StoreManager;
import eu.okaeri.commands.meta.ArgumentMeta;
import eu.okaeri.commands.service.CommandData;
import eu.okaeri.commands.service.Invocation;
import eu.okaeri.commands.type.resolver.BasicTypeResolver;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CellUserTypeResolver extends BasicTypeResolver<CellUser> {

    private @Inject StoreManager stores;

    @Override
    public boolean supports(@NonNull Class<?> type) {
        return type.equals(CellUser.class);
    }

    @Override
    public CellUser resolve(@NonNull Invocation invocation, @NonNull CommandData data, @NonNull ArgumentMeta argumentMeta, @NonNull String text) {
        Player player = Bukkit.getPlayer(text);
        if (player != null) {
            return stores.getUserStore().get(player);
        }
        return stores.getUserStore().get(text).orElse(null);
    }
}
