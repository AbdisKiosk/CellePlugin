package dk.setups.celle.gui.pagination;

import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.GuiItem;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class PaginationProvider<T> {

    private final BaseGui gui;
    private final GUIElementProvider<T> elementProvider;
    private final List<Integer> slots;
    private int page = 1;
    private final Function<Optional<T>, GuiItem> itemMapper;

    public PaginationProvider(BaseGui gui, GUIElementProvider<T> elementProvider, List<Integer> slots,
                              Function<Optional<T>, GuiItem> itemMapper) {
        this.gui = gui;
        this.elementProvider = elementProvider;
        this.slots = slots;
        this.itemMapper = itemMapper;
    }

    public void update() {
        int pageSize = slots.size();
        List<T> elements = elementProvider.getElements(page, pageSize);
        for (int i = 0; i < pageSize; i++) {
            int slot = slots.get(i);
            if(elements.size() > i) {
                updateItem(slot, elements.get(i));
                continue;
            }
            updateItem(slot, null);
        }
        gui.update();
    }

    public void setPage(int page) {
        this.page = page;
        update();
    }

    public void updateItem(int slot, T element) {
        gui.setItem(slot, itemMapper.apply(Optional.ofNullable(element)));
    }

}