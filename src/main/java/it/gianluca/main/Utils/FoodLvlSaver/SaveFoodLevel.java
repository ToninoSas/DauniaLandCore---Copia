package it.gianluca.main.Utils.FoodLvlSaver;

import it.gianluca.main.Main;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveFoodLevel {
    public static void salva(String modalita, Player p){
        if(modalita.equals("Survival")){
            new BukkitRunnable() {
                @Override
                public void run() {
                    GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".foodlvl", p.getFoodLevel());
                }
            }.runTaskAsynchronously(Main.plugin);
        }
    }
}
