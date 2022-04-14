package it.gianluca.main.Utils.ExpRestorer;

import it.gianluca.main.Main;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestoreExp {
    public static void restore(String modalita, Player p){
        if(modalita.equals("Survival")){
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.setLevel(GestioneConfigs.vanillaDatabaseConfiguration.getInt(p.getName() + ".exp"));
                }
            }.runTaskAsynchronously(Main.plugin);
        }
    }
}
