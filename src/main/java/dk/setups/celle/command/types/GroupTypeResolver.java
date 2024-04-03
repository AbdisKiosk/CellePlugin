package dk.setups.celle.command.types;

import dk.setups.celle.cell.CellGroup;
import dk.setups.celle.database.StoreManager;
import eu.okaeri.commands.meta.ArgumentMeta;
import eu.okaeri.commands.service.CommandData;
import eu.okaeri.commands.service.Invocation;
import eu.okaeri.commands.type.resolver.BasicTypeResolver;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

public class GroupTypeResolver extends BasicTypeResolver<CellGroup> {

    private @Inject StoreManager stores;

    @Override
    public boolean supports(@NonNull Class<?> type) {
        return type.equals(CellGroup.class);
    }

    @Override
    public CellGroup resolve(@NonNull Invocation invocation, @NonNull CommandData data, @NonNull ArgumentMeta argumentMeta, @NonNull String text) {
        return stores.getGroupStore().getFromName(text)
                .orElse(null);
    }
}
