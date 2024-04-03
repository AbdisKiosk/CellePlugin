package dk.setups.celle.gui.pagination;

import java.util.List;

public interface GUIElementProvider<T> {

    List<T> getElements(int page, int pageSize);

}
