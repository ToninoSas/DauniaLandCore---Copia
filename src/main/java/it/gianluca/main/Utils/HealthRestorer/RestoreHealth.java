package it.gianluca.main.Utils.HealthRestorer;
import it.gianluca.main.Main;
import it.gianluca.main.Survival.SurvivalManager;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class RestoreHealth {
    public static void restore(String modalita, Player p){
        if(modalita.equals("Survival")){
            new BukkitRunnable() {
                @Override
                public void run() {
                    p.setHealth(GestioneConfigs.vanillaDatabaseConfiguration.getDouble(p.getName() + ".health"));
                }
            }.runTaskAsynchronously(Main.plugin);
        }
    }
}
