package dk.setups.celle.command.completions;

import dk.setups.celle.database.StoreManager;
import eu.okaeri.commands.handler.completion.NamedCompletionHandler;
import eu.okaeri.commands.meta.ArgumentMeta;
import eu.okaeri.commands.meta.CompletionMeta;
import eu.okaeri.commands.service.CommandData;
import eu.okaeri.commands.service.Invocation;
import eu.okaeri.injector.annotation.Inject;
import lombok.NonNull;

import java.util.List;
import java.util.stream.Collectors;

public class CellGroupCompletion implements NamedCompletionHandler {

    private @Inject StoreManager stores;

    @Override
    public List<String> complete(@NonNull CompletionMeta completionData, @NonNull ArgumentMeta argument, @NonNull Invocation invocation, @NonNull CommandData data) {
        return stores.getGroupStore().getAll().stream()
                .map(group -> group.getName().toLowerCase())
                .collect(Collectors.toList());
    }
}
