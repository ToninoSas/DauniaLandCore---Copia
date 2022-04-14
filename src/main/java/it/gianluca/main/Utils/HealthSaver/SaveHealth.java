package it.gianluca.main.Utils.HealthSaver;

import it.gianluca.main.Main;
import it.gianluca.main.Survival.SurvivalManager;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SaveHealth {
    public static void salva(String modalita, Player p){
        if(modalita.equals("Survival")){
            new BukkitRunnable() {
                @Override
                public void run() {
                    GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".health", p.getHealth());
                }
            }.runTaskAsynchronously(Main.plugin);

        }
    }
}
