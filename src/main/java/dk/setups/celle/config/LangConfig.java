package dk.setups.celle.config;

import eu.okaeri.configs.yaml.snakeyaml.YamlSnakeYamlConfigurer;
import eu.okaeri.i18n.configs.LocaleConfig;
import eu.okaeri.platform.core.annotation.Messages;
import lombok.Getter;


@Getter
@SuppressWarnings("FieldMayBeFinal")
@Messages(path = "lang", defaultLocale = "dk", suffix = ".yml", provider = YamlSnakeYamlConfigurer.class)
public class LangConfig extends LocaleConfig {

    /**
     * Global Kommandoer
     */
    private String commandSystemUsageTemplate = "&eKorrekt brug af /{label}:\n{entries}";
    private String commandSystemUsageEntry = "&r - /{usage}";
    private String commandSystemUsageEntryDescription = "   &7{description}.";
    private String commandSystemPermissionsError = "&cIngen tilladelse {permission}!";
    private String commandSystemCommandError = "&cFejl: {message}";
    private String commandSystemUnknownError = "&cUkendt fejl! Reference ID: {id}";
    private String commandSystemConsoleOnlyError = "&cDenne kommando er kun for konsollen.";
    private String commandSystemPlayerOnlyError = "&cDenne kommando er kun for spillere.";

    /**
     * Celle Kommandoer
     */
    private String commandCellLabel = "celle";
    private String commandCellAlias = "ce";
    private String commandCellDescription = "Kommandoer til at håndtere dine celler.";

    private String commandCellUnrentAlias = "slet ?";
    private String commandCellUnrentDescription = "Sletter lejen af en celle.";
    private String commandCellUnrentUsage = "ce slet [celle]";
    private String commandCellUnrentNotRented = "&cDu har ikke lejet cellen &4{cell.name}&c.";
    private String commandCellUnrentSuccess = "&aDu har nu slettet lejen af cellen &2{cell.name}&a.";

    private String commandCellMemberAddAlias = "add * ?";
    private String commandCellMemberAddDescription = "Tilføjer en spiller til en celle.";
    private String commandCellMemberAddUsage = "ce add <spiller> [celle]";
    private String commandCellMemberAddSuccess = "&aSpilleren &2{target} &aer blevet tilføjet til cellen &2{cell.name}&a.";
    private String commandCellMemberAddAlreadyMember = "&4{target} &aer allerede medlem af &2{cell.name}&a.";
    private String commandCellMemberAddNotOwner = "&cDu ejer ikke &4{cell.name}&c.";
    private String commandCellMemberAddCannotBeMember = "&cSpilleren §4{target} &cmå ikke være medlem af cellen &4{cell.name}&c.";

    private String commandCellMemberRemoveAlias = "remove * ?";
    private String commandCellMemberRemoveDescription = "Fjerner en spiller fra en celle.";
    private String commandCellMemberRemoveUsage = "ce remove <spiller> [celle]";
    private String commandCellMemberRemoveSuccess = "&aSpilleren &2{target} &aer blevet fjernet fra cellen &2{cell.name}&a.";
    private String commandCellMemberRemoveNotMember = "&cSpilleren &4{target}&cer ikke medlem af &4{cell.name}&c.";
    private String commandCellMemberRemoveNotOwner = "&cDu ejer ikke &4{cell.name}&c.";

    private String commandCellInfoAlias = "info ?";
    private String commandCellInfoDescription = "Viser information om en celle.";
    private String commandCellInfoUsage = "ce info [celle]";
    private String commandCellInfoOtherNoPermission = "&cDu ejer ikke &4{cell.name}&c.";
    private String commandCellInfoNotRented =
            "&aGruppe: &2{cell.group.name}" +
            "\n&aRegion: &2{cell.region.name}" +
            "\n&aTid tilbage: &cIkke udlejet." +
            "\n&aUdløber: &cIkke udlejet." +
            "\n&aMedlemmer: &cIkke udlejet";
    private String commandCellInfoHeader = "&aInformation om cellen &2{cell.name}&a:";
    private String commandCellInfoMemberFormat = "{member}, ";
    private String commandCellInfoMemberFormatLast = "{member}";
    private String commandCellInfoNoMembers = "&cIngen...";
    private String commandCellInfoMessage = "&aEjer: &2{cell.owner.name}" +
            "\n&aGruppe: &2{cell.group.name}" +
            "\n&aRegion: &2{cell.region.name}" +
            "\n&aTid tilbage: &2{cell.time.left-long}" +
            "\n&aUdløber: &2{cell.time.format-long}" +
            "\n&aMedlemmer: &2{members}";

    private String commandCellListSelfAlias = "list;find";
    private String commandCellListSelfUsage = "ce find <spiller>";
    private String commandCellListSelfDescription = "Viser en liste over de celler, du kan bruge.";
    private String commandCellListSelfNoCells = "&cDu har ingen celler.";
    private String commandCellListSelfHeader = "&aDine celler: ";
    private String commandCellListSelfCell = "&2{cell.group.name} &a- &2{cell.name} &a- &2{cell.region.name} &a- &2{cell.owner.name} &a- &2{cell.time.format-long}";

    private String commandCellListOtherAlias = "list *;find *";
    private String commandCellListOtherUsage = "ce list <spiller>";
    private String commandCellListOtherDescription = "Viser en liste over de celler, en spiller ejer.";
    private String commandCellListOtherNoCells = "&cSpilleren har ingen celler.";
    private String commandCellListOtherHeader = "&aSpillerens celler: ";
    private String commandCellListOtherCell = "&2{cell.group.name} &a- &2{cell.name} &a- &2{cell.region.name} &a- &2{cell.owner.name} &a- &2{cell.time.format-long}";

    private String commandCellTeleportAlias = "tp *;teleport *";
    private String commandCellTeleportDescription = "Teleporter dig til en celle.";
    private String commandCellTeleportUsage = "ce tp <celle>";
    private String commandCellTeleportNotMember = "&cDu er ikke medlem af &4{cell.name}&c.";
    private String commandCellTeleportNoTeleport = "&cDer er ingen teleport til &4{cell.name}&c.";
    /**
     * Celle Admin Kommandoer
     */

    private String commandCeaLabel = "celladmin";
    private String commandCeaAlias = "cea";
    private String commandCeaDescription = "Kommandoer til at administrere celler.";

    private String commandCeaCreateAutoAlias = "create auto * *";
    private String commandCeaCreateAutoDescription = "Opretter en celle ud fra det skilt, du kigger på";
    private String commandCeaCreateAutoUsage = "cea create auto <cellegruppe> <cellenavn>";
    private String commandCeaCreateAutoNotLookingAtSign = "&cDu skal kigge på et skilt for at oprette en celle.";
    private String commandCeaCreateAutoNoSelection = "&cDu har ingen WorldEdit-selektion.";
    private String commandCeaCreateAutoRegionAlreadyExists = "&cDer findes allerede en region med navnet &2{region}&a.";

    private String commandCeaCreateCellAlias = "create cell * * *";
    private String commandCeaCreateCellDescription = "Opretter en celle ud fra en region og et navn";
    private String commandCeaCreateCellUsage = "cea create cell <cellegruppe> <region> <cellenavn>";
    private String commandCeaCreateCellAlreadyExists = "&cDer findes allerede en celle med navnet &4{name}&c.";
    private String commandCeaCreateCellCreated = "§aCellen &2{cell.name} &aer blevet oprettet i regionen &2{cell.region.name} &aog gruppen &2{cell.group.name}&a.";

    private String commandCeaCellSetSignAlias = "cell set sign *";
    private String commandCeaCellSetSignDescription = "Sætter skiltet på en celle til det skilt, du kigger på.";
    private String commandCeaCellSetSignUsage = "cea cell set sign <celle>";
    private String commandCeaCellSetSignNotLookingAtSign = "&cDu skal kigge på et skilt.";
    private String commandCeaCellSetSignSuccess = "&aSatte skiltet for &2{cell.name}&a.";

    private String commandCeaCellDeleteSignAlias = "cell delete sign";
    private String commandCeaCellDeleteSignDescription = "Sletter det skilte, som du kigger på";
    private String commandCeaCellDeleteSignUsage = "cea cell delete sign";
    private String commandCeaCellDeleteSignNotLookingAtSign = "&cDu skal kigge på et skilt.";
    private String commandCeaCellDeleteSignSuccess = "&aFjernede skiltet.";
    private String commandCeaCellDeleteSignFailure = "§cDer er ikke et skilt på denne lokation.";

    private String commandCeaCellSetTeleportAlias = "cell set teleport *";
    private String commandCeaCellSetTeleportDescription = "Sætter cellens teleporterings-lokationen til den placering";
    private String commandCeaCellSetTeleportUsage = "cea set teleport <celle>";
    private String commandCeaCellSetTeleportSuccess = "Satte teleporterings-lokationen til {x}, {y}, {z}";

    private String commandCeaCreateGroupAlias = "create group *";
    private String commandCeaCreateGroupDescription = "Opretter en cellegruppe.";
    private String commandCeaCreateGroupUsage = "cea create group <navn>";
    private String commandCeaCreateGroupAlreadyExists = "§cGruppen &4{group.name} &cfindes allerede.";
    private String commandCeaCreateGroupCreated = "&aGruppen &2{group.name} &aer blevet oprettet.";

    private String commandCeaDeleteCellAlias = "delete cell *";
    private String commandCeaDeleteCellDescription = "Sletter en celle.";
    private String commandCeaDeleteCellUsage = "cea delete cell <navn>";
    private String commandCeaDeleteCellSuccess = "§aCellen &2{cell.name} &aer blevet slettet.";

    private String commandCeaUnrentAlias = "unrent *";
    private String commandCeaUnrentDescription = "Unrenter cellen";
    private String commandCeaUnrentUsage = "cea unrent <navn>";
    private String commandCeaUnrentSuccess = "§aCellen &2{cell.name} &aer blevet unrentet.";

    private String commandCeaUnrentAllAlias = "unrent all *";
    private String commandCeaUnrentAllDescription = "Unrenter alle spillernes celler";
    private String commandCeaUnrentAllUsage = "cea unrent all <spiller>";
    private String commandCeaUnrentAllSuccess = "§aAlle celler for spilleren &2{player} &aer blevet unrentet.";

    private String commandCeaExtendCellAlias = "extend cell *";
    private String commandCeaExtendCellDescription = "Forlænger lejen af en celle.";
    private String commandCeaExtendCellUsage = "cea extend cell <navn>";
    private String commandCeaExtendCellNotRented = "§cCellen &4{cell.name} &cer ikke udlejet.";
    private String commandCeaExtendCellSuccess = "§aCellen &2{cell.name} &aer blevet forlænget.";

    private String commandCeaDeleteGroupAlias = "delete group * *";
    private String commandCeaDeleteGroupDescription = "Sletter en gruppe, og putter cellerne fra grouppen i en anden gruppe";
    private String commandCeaDeleteGroupUsage = "cea delete group <navn> <ny gruppe>";
    private String commandCeaDeleteGroupFoundCellsInGroup = "§aFandt §2{count} §aceller i gruppen.";
    private String commandCeaDeleteGroupSuccess = "§aGruppen &2{group.name} &aer blevet slettet, og {count} celler er blevet flyttet til gruppen &2{newGroup.name}&a.";

    private String commandCeaLogsPlayerAlias = "logs player *";
    private String commandCeaLogsPlayerDescription = "Viser loggen for en spiller.";
    private String commandCeaLogsPlayerUsage = "cea logs player <spiller>";

    private String commandCeaLogsCellAlias = "logs cell *";
    private String commandCeaLogsCellDescription = "Viser loggen for en celle.";
    private String commandCeaLogsCellUsage = "cea logs cell <celle>";

    private String commandCeaGroupSetSignLineAlias = "group set sign * * * *...";
    private String commandCeaGroupSetSignLineDescription = "Sætter en linje af skiltet på en cellegruppe.";
    private String commandCeaGroupSetSignLineUsage = "cea set sign <gruppe> <state> <linje> <tekst>";
    private String commandCeaGroupSetSignLineNotCorrectLine = "&cLinjen skal være mellem 1 og 4.";
    private String commandCeaGroupSetSignLineNotCorrectState = "&cDu kan bruge følgende tilstande: &4unrented, rented-non-member, rented-member, rented-owner&c.";
    private String commandCeaGroupSetSignLineSuccess = "§aLinje &2{line} &aaf skiltet for gruppen &2{group.name} &aer blevet sat til &2{text}&a.";

    private String commandCeaGroupSetRentPriceAlias = "group set permission * *";
    private String commandCeaGroupSetRentPriceDescription = "Sætter prisen for at leje en celle i en gruppe.";
    private String commandCeaGroupSetRentPriceUsage = "cea set group permission <gruppe> <pris>";
    private String commandCeaGroupSetRentPriceSuccess = "§aPrisen for at leje en celle i gruppen &2{group.name} &aer blevet sat til &2{price}&a.";

    private String commandCeaGroupSetMaxRentTimeAlias = "group set maxrenttime * *";
    private String commandCeaGroupSetMaxRentTimeDescription = "Sætter den maksimale leje tid for en celle i en gruppe.";
    private String commandCeaGroupSetMaxRentTimeUsage = "cea set group maxrenttime <gruppe> <tid, eks: 2d>";
    private String commandCeaGroupSetMaxRentTimeSuccess = "§aDen maksimale leje tid for en celle i gruppen &2{group.name} &aer blevet sat til &2{time}&a.";

    private String commandCeaGroupSetRentTimeAlias = "group set renttime * *";
    private String commandCeaGroupSetRentTimeDescription = "Sætter leje tiden for en celle i en gruppe.";
    private String commandCeaGroupSetRentTimeUsage = "cea set group renttime <gruppe> <tid, eks: 2d>";
    private String commandCeaGroupSetRentTimeSuccess = "§aLeje tiden for en celle i gruppen &2{group.name} &aer blevet sat til &2{time}&a.";

    private String commandCeaGroupSetMaxRentedCellsAlias = "group set maxrentedcells * *";
    private String commandCeaGroupSetMaxRentedCellsDescription = "Sætter det maksimale antal celler, en spiller kan leje i den bestemte gruppe";
    private String commandCeaGroupSetMaxRentedCellsUsage = "cea set group maxrentedcells <gruppe> <antal>";
    private String commandCeaGroupSetMaxRentedCellsSuccess = "§aDet maksimale antal celler, en spiller kan leje i gruppen &2{group.name} &aer blevet sat til &2{count}&a.";

    private String commandCeaGroupInfoAlias = "group info *";
    private String commandCeaGroupInfoDescription = "Viser information om en cellegruppe.";
    private String commandCeaGroupInfoUsage = "cea group info <gruppe>";
    private String commandCeaGroupInfoMessage = "&aInformation om gruppen &2{group.name}&a:\n" +
            "&aPris: &2{group.rentPrice}\n" +
            "&aPermission: &2{group.permission}\n" +
            "&aLeje tid: &2{group.renttime}\n" +
            "&aMaks. leje tid: &2{group.maxrenttime}\n" +
            "&aMaks. antal celler: &2{group.maxrentedcells}\n" +
            "&aSkilt linjer:\n" +
            "&2Unrented: \n" +
            "  &a- &r{group.unrentedSignLines-1}\n" +
            "  &a- &r{group.unrentedSignLines-2}\n" +
            "  &a- &r{group.unrentedSignLines-3}\n" +
            "  &a- &r{group.unrentedSignLines-4}\n" +
            "&2Rented non-member: \n" +
            "  &a- &r{group.rentedNonMemberSignLines-1}\n" +
            "  &a- &r{group.rentedNonMemberSignLines-2}\n" +
            "  &a- &r{group.rentedNonMemberSignLines-3}\n" +
            "  &a- &r{group.rentedNonMemberSignLines-4}\n" +
            "&2Rented member: \n" +
            "  &a- &r{group.rentedMemberSignLines-1}\n" +
            "  &a- &r{group.rentedMemberSignLines-2}\n" +
            "  &a- &r{group.rentedMemberSignLines-3}\n" +
            "  &a- &r{group.rentedMemberSignLines-4}\n" +
            "&2Rented owner: \n" +
            "  &a- &r{group.rentedOwnerSignLines-1}\n" +
            "  &a- &r{group.rentedOwnerSignLines-2}\n" +
            "  &a- &r{group.rentedOwnerSignLines-3}\n" +
            "  &a- &r{group.rentedOwnerSignLines-4}";

    private String commandCeaSignGUICreateAlias = "sign create gui *";
    private String commandCeaSignGUICreateDescription = "Opretter et skilt, som viser en GUI med ledige celler.";
    private String commandCeaSignGUICreateUsage = "cea sign create gui <region>";
    private String commandCeaSignGuiCreateNotLookingAtSign = "&cDu skal kigge på et skilt.";
    private String commandCeaSignGuiCreateSignAlreadyExists = "&cDer findes allerede et skilt på den placering.";
    private String commandCeaSignGuiCreateRegionNotFound = "&cDer findes ikke en region med navnet &4{region}&c.";
    private String commandCeaSignGuiCreateSuccess = "&aSkiltet er et blevet oprettet.";

    private String commandCeaSignGUIDeleteAlias = "sign delete gui";
    private String commandCeaSignGUIDeleteDescription = "Sletter et skilt, som viser en GUI med ledige celler.";
    private String commandCeaSignGUIDeleteUsage = "cea sign delete gui";
    private String commandCeaSignGuiDeleteNotLookingAtSign = "&cDu skal kigge på et skilt.";
    private String commandCeaSignGuiDeleteSignNotFound = "&cDer findes ikke et skilt på den placering.";
    private String commandCeaSignGuiDeleteSuccess = "&aSkiltet er blevet slettet.";

    private String commandCeaReloadAlias = "reload";
    private String commandCeaReloadDescription = "Genindlæser pluginnet.";
    private String commandCeaReloadUsage = "cea reload";
    private String commandCeaReloadSuccess = "&aPluginnet er blevet genindlæst på &2{elapsed}ms&a." +
            "\n&aIkke alle ændringer er blevet reloaded, så du bliver måske nødt til at genstarte serveren.";

    private String commandCeaCommandEachAlias = "commandeach *...";
    private String commandCeaCommandEachDescription = "Kører kommandoen for alle celler";
    private String commandCeaCommandEachUsage = "cea commandeach <kommando>";
    private String commandCeaCommandEachSentCommand = "§aSkrev §2{command}";

    private String commandCeaTeleportOtherAlias = "teleport * *";
    private String commandCeaTeleportOtherDescription = "Teleporterer spilleren til cellen";
    private String commandCeaTeleportOtherUsage = "cea teleport <spiller> <celle>";
    private String commandCeaTeleportOtherNoTeleport = "&cCellen har ikke nogen teleport-lokation.";
    private String commandCeaTeleportOtherSuccess = "§aTeleportede §2{target.name}§a til §2{cell.name}§a.";

    /**
     * Cell Click Events
     */
    private String clickCellInfoHeader = "&aInformation om cellen &2{cell.name}&a:";
    private String clickCellInfoMemberFormat = "{member}, ";
    private String clickCellInfoMemberFormatLast = "{member}";
    private String clickCellInfoNoMembers = "&cIngen...";
    private String clickCellInfoMessage = "&aEjer: &2{cell.owner.name}" +
                                        "\n&aGruppe: &2{cell.group.name}" +
                                        "\n&aRegion: &2{cell.region.name}" +
                                        "\n&aTid tilbage: &2{cell.time.left-long}" +
                                        "\n&aUdløber: &2{cell.time.format-long}" +
                                        "\n&aMedlemmer: &2{members}";

    private String cellAttemptRentAlreadyRented = "&cCellen &4{cell.name} &cer allerede udlejet.";
    private String cellAttemptRentMaxRented = "&cDu har allerede udlejet det maksimale antal celler i gruppen &4{group}&c.";
    private String cellAttemptRentNoPermission = "&cDu har ikke adgang til at leje cellen &4{cell.name} &ci gruppen &4{group}&c.";
    private String cellAttemptRentNotEnoughMoney = "&cDu har ikke nok penge til at leje &4{cell.name}&c.";
    private String cellAttemptRentSuccess = "&aDu har nu lejet cellen &2{cell.name}&a.";

    private String cellAttemptExtendNotOwned = "&cDu ejer ikke cellen &4{cell.name}&c.";
    private String cellAttemptExtendNoPermission = "§cDu har ikke adgang til at forlænge §4{cell.name}§c.";
    private String cellAttemptExtendFullyExtended = "&cDu kan ikke forlænge lejen af &4{cell.name} &cmere.";
    private String cellAttemptExtendNotEnoughMoney = "&cDu har ikke nok penge til at forlænge lejen af &4{cell.name}&c.";
    private String cellAttemptExtendSuccess = "&aDu har nu forlænget lejen af &2{cell.name}&a.";
}