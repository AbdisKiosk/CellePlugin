package dk.setups.celle.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@Configuration(path = "defaults.yml", provider = YamlSnakeYamlConfigurer.class)
@SuppressWarnings("FieldMayBeFinal")
@Header({
        "Herinde kan du ændre standardindstillingerne for cellegrupper.",
        "Hvis du ændrer noget herinde, og en cellegruppe ikke har en værdi sat i forvejen, vil denne værdi blive brugt.",
        "",
        "Hvis du vil ændre en værdi for en specifik cellegruppe, skal du bruge ingame-kommandoerne."
})
public class DefaultConfig extends OkaeriConfig {

    @Getter
    private transient static DefaultConfig instance;

    public DefaultConfig() {
        instance = this;
    }

    private double rentPrice = 1000.0;
    private String rentPermission = "fange";
    private long cellRentMillis = 1000 * 60 * 60 * 24;
    private long maxRentMillis = 1000 * 60 * 60 * 24 * 10;
    private int maxCellsPerPlayer = 9999;

    private List<String> unrentedSignLines = Arrays.asList(
            "&2&lLEDIG",
            "&a{cell.name}",
            "&a${cell.group.price.format-short}",
            "&aKlik for at leje"
    );

    private List<String> rentedNonMemberSignLines = Arrays.asList(
            "&4&lSOLGT",
            "&c{cell.name}",
            "&c{cell.owner.name}",
            "&c{cell.time.left-short}"
    );

    private List<String> rentedMemberSignLines = Arrays.asList(
            "&5&lMEDLEM",
            "&d{cell.name}",
            "&d{cell.owner.name}",
            "&d{cell.time.left-short}"
    );

    private List<String> rentedOwnerSignLines = Arrays.asList(
            "&3&lEJET",
            "&b{cell.name}",
            "&b{cell.owner.name}",
            "&b{cell.time.left-short}"
    );
}