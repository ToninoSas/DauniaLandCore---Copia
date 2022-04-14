package it.gianluca.main.Utils.FoodLvlRestorer;

import it.gianluca.main.Main;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestoreFoodLevel {
    public static void restore(String modalita, Player p){
        if(modalita.equals("Survival")){
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.setFoodLevel(GestioneConfigs.vanillaDatabaseConfiguration.getInt(p.getName() + ".foodlvl"));
                }
            }.runTaskAsynchronously(Main.plugin);
        }
    }
}
