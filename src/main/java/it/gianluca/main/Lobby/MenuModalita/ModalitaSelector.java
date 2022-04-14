package it.gianluca.main.Lobby.MenuModalita;
import it.gianluca.main.Survival.SurvivalManager;
import it.gianluca.main.Utils.ItemCreator.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class ModalitaSelector {
    public static Inventory menu;
    public static void crea(){
        menu = Bukkit.createInventory(null, 9 , "Seleziona Modalita'");
        inserisciElementi();
    }

    public static void inserisciElementi(){

        menu.setItem(2, ItemCreator.creaItem(Material.PODZOL, "§4§lHardcore", "", "§7Vanilla classico di minecraft ma con", "§cuna sola vita.",
                "", "§9Versione richiesta: §c1.18", "", "§9Stato: §c§oNon disponibile."));
        menu.setItem(4, ItemCreator.creaItem(Material.GRASS_BLOCK, "§a§lVanilla", "", "§7Un classico vanilla con comandi", "§7e funzionalita' avanzate!",
                "", "§9Versione richiesta: §c1.18", "", "§9Stato: §a§oOnline", "", "§9Giocatori connessi: §f" + SurvivalManager.vanillaPlayers.size()));
        menu.setItem(6, ItemCreator.creaItem(Material.LEGACY_BED, "§c§lBed§f§lWars", "", "§7Distruggi il letto avversario", "§7per dominare!",
                "", "§9Versione richiesta: §c1.8 / 1.18", "", "§9Stato: §c§oNon disponibile."));

        menu.setItem(0, ItemCreator.creaItem(Material.BARRIER, "§cComing soon...", ""));
        menu.setItem(8, ItemCreator.creaItem(Material.BARRIER, "§cComing soon...", ""));
    }

    public static void apri(Player p){
        p.openInventory(menu);
    }

}
