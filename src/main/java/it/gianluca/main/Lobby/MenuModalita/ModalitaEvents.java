package it.gianluca.main.Lobby.MenuModalita;
import it.gianluca.main.Lobby.LobbyManager;
import it.gianluca.main.Survival.SurvivalManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;



public class ModalitaEvents implements Listener {
    @EventHandler
    public void onInventory(InventoryClickEvent e){
        if(e.getInventory() != ModalitaSelector.menu){
            return;
        }
        e.setCancelled(true);
        Player p = (Player) e.getWhoClicked();
        if(e.getRawSlot() == 2){
            p.sendMessage("§cModalita' attualmente non disponibile.");
        }
        else if(e.getRawSlot() == 4){
            if(SurvivalManager.manutenzione){
                p.sendMessage("§cModalita' attualmente in §6§omanutenzione.");
            }else{
                SurvivalManager.tpToSurvival(p);
                LobbyManager.cleanLobbyThings(p);
            }

        }
        else if(e.getRawSlot() == 6){
            p.sendMessage("§cModalita' attualmente non disponibile.");
        }
    }

}
