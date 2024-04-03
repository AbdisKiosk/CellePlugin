package dk.setups.celle.util.cell;

import dk.setups.celle.cell.CellGroup;
import dk.setups.celle.config.Config;
import dk.setups.celle.config.DefaultConfig;
import eu.okaeri.injector.annotation.Inject;
import eu.okaeri.platform.core.annotation.Component;

@Component
public class CellFactory {

    @SuppressWarnings("unused")
    private @Inject("config") Config config;
    @SuppressWarnings("unused")
    private @Inject("defaultConfig") DefaultConfig defaults;

    public CellGroup createGroup(String name) {
        return new CellGroup(name);
    }
}
