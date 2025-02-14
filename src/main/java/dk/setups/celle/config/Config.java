package dk.setups.celle.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import eu.okaeri.configs.annotation.Header;
import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import eu.okaeri.platform.core.annotation.Configuration;
import lombok.Getter;

import java.time.DayOfWeek;
import java.time.Month;
import java.util.*;

@SuppressWarnings("FieldMayBeFinal")
@Getter
@Configuration(path = "config.yml", provider = YamlSnakeYamlConfigurer.class)
@Header({
        "Her kan du ændre på forskellige ting i pluginen.",
        "Hvis du ændrer noget her, skal du køre /cea reload, før ændringerne træder i kræft.",
        "",
        "Du kan godt bruge danske bogstaver."
})
public class Config extends OkaeriConfig {

        @Comment("Hvor mange celler kan en spiller højest eje? Den tjekker permissionsne i rækkefølge, så hvis en spiller har donator, vil den bruge den værdi.")
    private Map<String, Integer> maxCellsPerPlayer = getDefaultMaxCellsPerPlayer();
    private Map<String, Integer> getDefaultMaxCellsPerPlayer() {
        Map<String, Integer> maxCellsPerPlayer = new LinkedHashMap<>();
        maxCellsPerPlayer.put("donator", 2);
        maxCellsPerPlayer.put("default", 1);
        return maxCellsPerPlayer;
    }

    @Comment({
            "Timeformat configuration",
            "Her kan du ændre hvordan tid skal se ud i forskellige beskeder. Du kan bruge følgende keys til at formatere:",
            " * SECOND",
            " * MINUTE",
            " * HOUR",
            " * DAY",
            " * MONTH",
            " * YEAR"
    })

    private Map<TimeKey, List<String>> timeFormatConcise = getDefaultTimeFormatConcise();
    private Map<TimeKey, List<String>> getDefaultTimeFormatConcise() {
        Map<TimeKey, List<String>> map = new LinkedHashMap<>();
        map.put(TimeKey.SECOND, Arrays.asList("s", "s"));
        map.put(TimeKey.MINUTE, Arrays.asList("m", "m"));
        map.put(TimeKey.HOUR, Arrays.asList("t", "t"));
        map.put(TimeKey.DAY, Arrays.asList("d", "d"));
        map.put(TimeKey.MONTH, Arrays.asList("md", "md"));
        map.put(TimeKey.YEAR, Arrays.asList("år", "år"));
        return map;
    }
    @Comment({"Hvad skal der stå mellem de forskellige tidsenheder? Eksempelvis 1d 2t 3m. Hvis du vil have mellemrum, skal du bare skrive et mellemrum.",
             "Den første er ental, hvor den anden er flertal."
    })
    private String timeFormatConciseSeparator = " ";
    private Map<TimeKey, List<String>> timeFormatLong = getDefaultTimeFormatLong();
    @Comment("Hvad skal der stå mellem antallet og tidsenheden? Eksempelvis vil det være '1 dag 1 minut' normalt.")
    private String timeFormatLongSeparator = " ";
    private Map<TimeKey, List<String>> getDefaultTimeFormatLong() {
        Map<TimeKey, List<String>> map = new LinkedHashMap<>();
        map.put(TimeKey.SECOND, Arrays.asList("sekund", "sekunder"));
        map.put(TimeKey.MINUTE, Arrays.asList("minut", "minutter"));
        map.put(TimeKey.HOUR, Arrays.asList("time", "timer"));
        map.put(TimeKey.DAY, Arrays.asList("dag", "dage"));
        map.put(TimeKey.MONTH, Arrays.asList("måned", "måneder"));
        map.put(TimeKey.YEAR, Arrays.asList("år", "år"));
        return map;
    }

    @Comment("Bruger java simple date formatter formatted, som du kan læse mere om her (se tabellen): https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html")
    private String dateFormatShort = "dd:MM, HH:mm";
    @Comment("Erstatter automatisk {day}, {day-number}, {month}, {year}, {hour} og {minute} med de rigtige værdier. Du kan også ændre måneds- og dagsnavne under 'dateFormatLongMonthNames' og 'dateFormatLongDayNames'.")
    private String dateFormatLong = "{day} d. {day-number}. {month}, {year}, {hour}:{minute}";
    private Map<Month, String> dateFormatLongMonthNames = getDefaultMonthNames();
    private Map<Month, String> getDefaultMonthNames() {
        Map<Month, String> map = new LinkedHashMap<>();
        map.put(Month.JANUARY, "januar");
        map.put(Month.FEBRUARY, "februar");
        map.put(Month.MARCH, "marts");
        map.put(Month.APRIL, "april");
        map.put(Month.MAY, "maj");
        map.put(Month.JUNE, "juni");
        map.put(Month.JULY, "juli");
        map.put(Month.AUGUST, "august");
        map.put(Month.SEPTEMBER, "september");
        map.put(Month.OCTOBER, "oktober");
        map.put(Month.NOVEMBER, "november");
        map.put(Month.DECEMBER, "december");
        return map;
    }
    private Map<DayOfWeek, String> dateFormatLongDayNames = getDefaultDayNames();
    private Map<DayOfWeek, String> getDefaultDayNames() {
        Map<DayOfWeek, String> map = new HashMap<>();
        map.put(DayOfWeek.MONDAY, "mandag");
        map.put(DayOfWeek.TUESDAY, "tirsdag");
        map.put(DayOfWeek.WEDNESDAY, "onsdag");
        map.put(DayOfWeek.THURSDAY, "torsdag");
        map.put(DayOfWeek.FRIDAY, "fredag");
        map.put(DayOfWeek.SATURDAY, "lørdag");
        map.put(DayOfWeek.SUNDAY, "søndag");
        return map;
    }


    public enum TimeKey {
        SECOND(1_000),
        MINUTE(60_000),
        HOUR(3_600_000),
        DAY(86_400_000),
        MONTH(2_592_000_000L),
        YEAR(31_536_000_000L);

        @Getter
        private final long ms;

        TimeKey(long ms) {
            this.ms = ms;
        }
    }

    @Comment({
            "Hvor lang tid skal der gå, før skiltene opdateres?",
            "Dette er i ticks, så 20 ticks er 1 sekund.",
            "",
            "Det koster overraskende lidt at opdatere skilte, da det bliver gjort smart, så tøv ikke med at sæt det lavt. Derudover bliver det kørt async.",
            "Dette påvirker IKKE hvornår cellerne udløber."
    })
    private int updateSignsDelayTick = 20;

    @Comment({
            "Hvor lang skal cooldown være på forskellige ting? Dette påvirker IKKE kommadoer, men kun nogle ting, som eksempelvis skilt.",
            "Det kan være brugbart at sætte op, for at undgå fordele ved folk, der bruger autoclicker på skilte.",
            "Det er i millisekunder, så 1000 er 1 sekund."
    })
    private int cooldownMs = 250;

    private boolean ironDoorOpen = true;
    private boolean teleportOutOfCell = true;
}
