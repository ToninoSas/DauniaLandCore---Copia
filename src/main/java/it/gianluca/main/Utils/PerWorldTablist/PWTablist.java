package it.gianluca.main.Utils.PerWorldTablist;

import it.gianluca.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class PWTablist implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        showAndHidePlayers(e.getPlayer());
    }
    @EventHandler
    public void onPlayerChangedWorld(final PlayerChangedWorldEvent e) {
        showAndHidePlayers(e.getPlayer());
    }

    private void showAndHidePlayers(final Player player) {
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Main.plugin, () -> {
            for (Player players : Bukkit.getServer().getOnlinePlayers()) {
                if (player.getWorld().equals(players.getWorld())) {
                    player.showPlayer(players);
                    players.showPlayer(player);
                } else {
                    player.hidePlayer(players);
                    players.hidePlayer(player);
                }
            }
        }, 1);
    }
}
