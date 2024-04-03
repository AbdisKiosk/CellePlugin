package dk.setups.celle.util;

import dk.setups.celle.cell.Cell;
import dk.setups.celle.config.DefaultConfig;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.placeholders.Placeholders;
import eu.okaeri.placeholders.context.PlaceholderContext;
import eu.okaeri.placeholders.message.CompiledMessage;
import eu.okaeri.platform.core.annotation.Component;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@Component
@SuppressWarnings("unused")
public class SignContentCreator {

    private @Inject TimeFormat timeFormat;
    private @Inject NumberFormat numberFormat;
    private @Inject DefaultConfig defaults;
    private @Inject Placeholders placeholders;

    public String[] getSignContent(Cell cell, Player target) {
        if(cell.isRented()) {
            if(cell.isOwner(target.getUniqueId())) {
                return getOwnerContent(cell, target);
            }
            if(cell.isPermitted(target)) {
                return getMemberContent(cell, target);
            }
            return getNonMemberContent(cell, target);
        }
        return getUnrentedContent(cell, target);
    }

    private String[] getOwnerContent(Cell cell, Player target) {
        return applyPlaceholders(compile(cell.getGroup().getRentedOwnerSignLines()), cell, target);
    }

    private String[] getMemberContent(Cell cell, Player target) {
        return applyPlaceholders(compile(cell.getGroup().getRentedMemberSignLines()), cell, target);
    }

    private String[] getNonMemberContent(Cell cell, Player target) {
        return applyPlaceholders(compile(cell.getGroup().getRentedNonMemberSignLines()), cell, target);
    }

    private String[] getUnrentedContent(Cell cell, Player target) {
        return applyPlaceholders(compile(cell.getGroup().getUnrentedSignLines()), cell, target);
    }

    private String[] applyPlaceholders(CompiledMessage[] compiled, Cell cell, Player target) {
        String[] lines = new String[4];

        lines[0] = applyPlaceholders(compiled[0], cell, target);
        lines[1] = applyPlaceholders(compiled[1], cell, target);
        lines[2] = applyPlaceholders(compiled[2], cell, target);
        lines[3] = applyPlaceholders(compiled[3], cell, target);

        return lines;
    }

    private String applyPlaceholders(CompiledMessage message, Cell cell, Player target) {
        return PlaceholderContext.of(placeholders, message)
                .with("cell", cell)
                .apply();

    }


    private CompiledMessage[] compile(List<String> strings) {
        CompiledMessage[] messages = new CompiledMessage[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            messages[i] = CompiledMessage.of(ChatColor.translateAlternateColorCodes('&', strings.get(i)));
        }
        return messages;
    }
}