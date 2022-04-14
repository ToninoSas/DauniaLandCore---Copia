package it.gianluca.main.Utils.InventoryRestorer;


import it.gianluca.main.Main;
import it.gianluca.main.Survival.SurvivalManager;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RestoreInventory {
    public static void restore(String modalita, Player p){
        if(modalita.equals("Survival")){
            new BukkitRunnable() {
                @Override
                public void run() {
                    if(GestioneConfigs.vanillaDatabaseConfiguration.get(p.getName() + ".inventory") == null) return;
                    ConfigurationSection cs = GestioneConfigs.vanillaDatabaseConfiguration.getConfigurationSection(p.getName() + ".inventory");
                    List<ItemStack> items = new ArrayList<>();
                    for(String key : cs.getKeys(false)){
                        Object o = cs.get(key);
                        if(o instanceof ItemStack){
                            items.add((ItemStack) o);
                        }else{
                            items.add(null);
                        }
                    }
                    ItemStack[] inv = items.toArray(new ItemStack[0]);
                    p.getInventory().setContents(inv);
                }
            }.runTaskAsynchronously(Main.plugin);
        }
    }
}
