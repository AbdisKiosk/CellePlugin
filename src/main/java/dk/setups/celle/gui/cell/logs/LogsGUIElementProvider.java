package dk.setups.celle.gui.cell.logs;

import dk.setups.celle.cell.log.CellLog;
import dk.setups.celle.cell.log.CellLogFilter;
import dk.setups.celle.database.CellLogStore;
import dk.setups.celle.gui.pagination.GUIElementProvider;

import java.util.List;

public class LogsGUIElementProvider implements GUIElementProvider<CellLog> {

    private final CellLogStore store;
    private final CellLogFilter filter;

    public LogsGUIElementProvider(CellLogStore store, CellLogFilter filter) {
        this.store = store;
        this.filter = filter;
    }

    @Override
    public List<CellLog> getElements(int page, int pageSize) {
        int skip = (page - 1) * pageSize;
        return store.getLogs(filter, pageSize, skip);
    }
}
