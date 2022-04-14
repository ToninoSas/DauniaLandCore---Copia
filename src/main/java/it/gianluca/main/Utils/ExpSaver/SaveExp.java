package it.gianluca.main.Utils.ExpSaver;

import it.gianluca.main.Main;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveExp {
    public static void salva(String modalita, Player p){
        if(modalita.equals("Survival")){
            new BukkitRunnable() {
                @Override
                public void run() {
                    GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".exp", p.getLevel());
                }
            }.runTaskAsynchronously(Main.plugin);
        }
    }
}
