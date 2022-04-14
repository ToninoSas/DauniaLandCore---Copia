package it.gianluca.main.Survival.HomeManager;

import it.gianluca.main.Main;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class HomeManager {
    public static void tpHome(Player p){
        if(GestioneConfigs.vanillaHomesConfiguration.get(p.getName()) == null){
            p.sendMessage("§cNon hai nessuna home. Impostane una con §a/sethome.");
            return;
        }
        float yaw = (float) GestioneConfigs.vanillaHomesConfiguration.getDouble(p.getName() + ".YAW");
        float pitch = (float) GestioneConfigs.vanillaHomesConfiguration.getDouble(p.getName() + ".PITCH");
        Location l = new Location(
                Bukkit.getWorld(Objects.requireNonNull(GestioneConfigs.vanillaHomesConfiguration.getString(p.getName() + ".world"))),
                GestioneConfigs.vanillaHomesConfiguration.getDouble(p.getName() + ".X"),
                GestioneConfigs.vanillaHomesConfiguration.getDouble(p.getName() + ".Y"),
                GestioneConfigs.vanillaHomesConfiguration.getDouble(p.getName() + ".Z"),
                yaw, pitch
        );
        p.teleport(l);
        p.sendMessage("§9Sei stato teletrasportato nella tua §chome!");
    }

    public static void createHome(Player p){
        if(GestioneConfigs.vanillaHomesConfiguration.contains(p.getName())){
            p.sendMessage("§cHai gia' una home impostata. Se desideri cambiarla, digita prima il comando §a/delhome §c e dopo §a/sethome.");
            return;
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                GestioneConfigs.vanillaHomesConfiguration.set(p.getName() + ".world", p.getLocation().getWorld().getName());
                GestioneConfigs.vanillaHomesConfiguration.set(p.getName() + ".X", p.getLocation().getX());
                GestioneConfigs.vanillaHomesConfiguration.set(p.getName() + ".Y", p.getLocation().getY());
                GestioneConfigs.vanillaHomesConfiguration.set(p.getName() + ".Z", p.getLocation().getZ());
                GestioneConfigs.vanillaHomesConfiguration.set(p.getName() + ".YAW", p.getLocation().getYaw());
                GestioneConfigs.vanillaHomesConfiguration.set(p.getName() + ".PITCH", p.getLocation().getPitch());
            }
        }.runTaskAsynchronously(Main.plugin);
        GestioneConfigs.save();
        p.sendMessage("§9Home impostata con §csuccesso!");
    }
}
