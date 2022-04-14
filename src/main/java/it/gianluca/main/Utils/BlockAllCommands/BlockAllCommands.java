package it.gianluca.main.Utils.BlockAllCommands;

import it.gianluca.main.Main;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.Console;
import java.util.HashMap;
import java.util.UUID;

public class BlockAllCommands implements Listener {
    public static HashMap<UUID, Integer> blockCommands = new HashMap<>();


    public static void blocca (Player p, int tempo){
        blockCommands.put(p.getUniqueId(), tempo);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(p.isOnline()){
                    if(blockCommands.get(p.getUniqueId()) == 0){
                        blockCommands.remove(p.getUniqueId());
                        this.cancel();
                    }else{
                        blockCommands.put(p.getUniqueId(), blockCommands.get(p.getUniqueId()) - 1);
                    }
                }else{
                    blockCommands.remove(p.getUniqueId());
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.plugin, 0, 20);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        if(blockCommands.containsKey(e.getPlayer().getUniqueId())){
            e.setCancelled(true);
            e.getPlayer().sendMessage("§cNon puoi eseguire comandi in questo momento.");
        }
    }
}
