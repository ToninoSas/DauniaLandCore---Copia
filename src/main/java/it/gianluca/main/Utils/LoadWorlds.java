package it.gianluca.main.Utils;

import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.WorldCreator;

public class LoadWorlds {
    public static void loadWorlds(){
        new WorldCreator("lobby").createWorld();
        Bukkit.getWorld("lobby").setDifficulty(Difficulty.PEACEFUL);
        Bukkit.getLogger().info("Mondo lobby caricato con successo.");
    }
}
