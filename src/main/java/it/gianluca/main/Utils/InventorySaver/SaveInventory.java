package it.gianluca.main.Utils.InventorySaver;
import it.gianluca.main.Main;
import it.gianluca.main.Survival.SurvivalManager;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class SaveInventory {
    public static void salva(String modalita, Player p){
        if(modalita.equals("Survival")){
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (int i = 0; i < p.getInventory().getContents().length; i++){
                        ItemStack item =   p.getInventory().getContents()[i];
                        if(item == null) GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".inventory." + i, null);
                        GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".inventory." + i, item);
                    }
                }
            }.runTaskAsynchronously(Main.plugin);
        }
    }


}
