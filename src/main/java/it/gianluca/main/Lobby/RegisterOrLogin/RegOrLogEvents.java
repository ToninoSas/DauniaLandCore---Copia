package it.gianluca.main.Lobby.RegisterOrLogin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class RegOrLogEvents implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        if (RegOrLogManager.isRegistered(p)) RegOrLogManager.startLogin(p);

        else RegOrLogManager.startRegister(p);

    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        RegOrLogManager.isRegistering.remove(p.getUniqueId());
        RegOrLogManager.isLogging.remove(p.getUniqueId());
        if(RegOrLogManager.taskID.containsKey(p.getUniqueId())){
            Bukkit.getScheduler().cancelTask(RegOrLogManager.taskID.get(p.getUniqueId()));
            RegOrLogManager.taskID.remove(p.getUniqueId());
        }
    }
}
