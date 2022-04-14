package it.gianluca.main.GlobalCounters;

import it.gianluca.main.Lobby.LobbyManager;
import it.gianluca.main.Lobby.MenuModalita.ModalitaSelector;
import it.gianluca.main.Main;
import it.gianluca.main.Survival.SurvivalManager;
import it.gianluca.main.Utils.FastBoard.FastBoard;
import it.gianluca.main.Utils.ItemCreator.ItemCreator;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

//CLASSE PER AGGIORNARE I VARI COUNTERS DELLE MODALITA.
//IN FASE DI SVILUPPO.

public class GlobalCounters {
    public static void updateGlobalOnlinePlayers(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(FastBoard b : LobbyManager.lobbyboards.values()){
                    b.updateLine(4, "§9Online: §f" + Main.Online);
            }
        }
        }.runTaskAsynchronously(Main.plugin);
    }

    public static void updateVanillaCounters(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(FastBoard b : SurvivalManager.survivalboards.values()){
                    b.updateLine(12, "§9Online: §f" + SurvivalManager.vanillaPlayers.size());
                }
                if(SurvivalManager.manutenzione){
                    ModalitaSelector.menu.setItem(4, ItemCreator.creaItem(Material.GRASS_BLOCK, "§a§lVanilla", "", "§7Un classico vanilla con comandi",
                            "§7e funzionalita' avanzate!", "", "§9Versione richiesta: §c1.18", "", "§9Stato: §6§oManutenzione", "",
                            "§9Giocatori connessi: §f" + SurvivalManager.vanillaPlayers.size()));
                }else{
                    ModalitaSelector.menu.setItem(4, ItemCreator.creaItem(Material.GRASS_BLOCK, "§a§lVanilla", "", "§7Un classico vanilla con comandi",
                            "§7e funzionalita' avanzate!", "", "§9Versione richiesta: §c1.18", "", "§9Stato: §a§oOnline", "",
                            "§9Giocatori connessi: §f" + SurvivalManager.vanillaPlayers.size()));
                }
            }
        }.runTaskAsynchronously(Main.plugin);
    }
}
