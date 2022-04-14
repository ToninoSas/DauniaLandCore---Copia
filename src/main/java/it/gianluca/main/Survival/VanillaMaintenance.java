package it.gianluca.main.Survival;
import it.gianluca.main.GlobalCounters.GlobalCounters;
import it.gianluca.main.Main;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


public class VanillaMaintenance {
    public static void startVanillaMaintenance(){
        Bukkit.broadcastMessage(Main.attenzione + "La modalita' §a§lVanilla §7chiude momentaneamente per §6manutenzione.");
        Bukkit.broadcastMessage("§7Ci scusiamo per il disagio.");
        final int[] time = {60};
        new BukkitRunnable() {
            @Override
            public void run() {
                if(time[0] == 0){
                    SurvivalManager.kickAllVanillaPlayers();
                    SurvivalManager.manutenzione = true;
                    this.cancel();
                    return;
                }
                if(time[0] % 5 == 0){
                    for(Player p : SurvivalManager.vanillaPlayers){
                        p.sendTitle("§c§oATTENZIONE", "§6Terminare tutte le azioni in corso.", 0, 40, 0);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Tempo rimanente: §c" + time[0]));
                    }
                    time[0]--;
                }else{
                    for(Player p : SurvivalManager.vanillaPlayers){
                        p.sendTitle("§c§oATTENZIONE", "§7Il vanilla andra' in manutenzione.", 0, 40, 0);
                        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Tempo rimanente: §c" + time[0]));
                    }
                    time[0]--;
                }

            }
        }.runTaskTimer(Main.plugin, 0 , 20);
    }

    public static void stopVanillaMaintenance(){
        SurvivalManager.manutenzione = false;
        GlobalCounters.updateVanillaCounters();
        Bukkit.broadcastMessage(Main.attenzione + "Abbiamo appena terminato la manutenzione per la modalita' §a§lVanilla.");
        Bukkit.broadcastMessage("§7Corri a provarla!  Possono esserci delle novita! :)");
    }

}
