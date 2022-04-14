package it.gianluca.main.Lobby.RegisterOrLogin;
import it.gianluca.main.Main;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.UUID;

public class RegOrLogManager {

    public static HashMap<UUID, Boolean> isRegistering = new HashMap<>();
    public static HashMap<UUID, Boolean> isLogging = new HashMap<>();
    public static HashMap<UUID, Integer> taskID = new HashMap<>();


    public static void startLogin(Player p){
        final int[] secondi = {20};
        isLogging.put(p.getUniqueId(), true);
        setPlayerStatus(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!taskID.containsKey(p.getUniqueId())){
                    taskID.put(p.getUniqueId(), this.getTaskId());  //Registro il task id cosi lo posso terminare anche al di fuori del runnable.
                }
                if(secondi[0] == 0){
                    p.kickPlayer("§cTempo scaduto per loggarsi.");
                    this.cancel();
                    return;
                }

                p.sendTitle("§9Esegui il login..", "§c/login <password>.", 0, 40, 0);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Tempo rimanente: §a" + secondi[0]));
                secondi[0]--;
            }
        }.runTaskTimerAsynchronously(Main.plugin, 0, 20);
    }

    public static void startRegister(Player p){
        final int[] secondi = {20};
        isRegistering.put(p.getUniqueId(), true);
        setPlayerStatus(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(!taskID.containsKey(p.getUniqueId())){
                    taskID.put(p.getUniqueId(), this.getTaskId());  //Registro il task id cosi lo posso terminare anche al di fuori del runnable.
                }
                if(secondi[0] == 0){
                    p.kickPlayer("§cTempo scaduto per registrarti.");
                    this.cancel();
                    return;
                }
                p.sendTitle("§6Devi registrarti!", "§7Apri la chat e digita: §c/reg <password> <conf.pass>.", 0, 40, 0);
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("Tempo rimanente: §a" + secondi[0]));
                secondi[0]--;

            }
        }.runTaskTimerAsynchronously(Main.plugin, 0, 20);
    }






    public static boolean isRegistered(Player p){
        return GestioneConfigs.credenzialiConfiguration.contains(p.getName());
    }

    private static void setPlayerStatus(Player p){
        p.setFlySpeed(0.0f);
        p.setGameMode(GameMode.SPECTATOR);
        p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999999, 10), true);
    }

    public static void clearPlayerStatus(Player p){
        p.setFlySpeed(0.1f);
        p.setGameMode(GameMode.ADVENTURE);
        p.removePotionEffect(PotionEffectType.BLINDNESS);
    }

}
