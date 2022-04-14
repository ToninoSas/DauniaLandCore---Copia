package it.gianluca.main.Survival.TpaManager;
import it.gianluca.main.Main;
import it.gianluca.main.Survival.SurvivalManager;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.HashMap;
import java.util.UUID;

public class VanillaTPA {
    public static HashMap<UUID, UUID> richieste = new HashMap<>();
    public static HashMap<UUID, Integer> tempo = new HashMap<>();

    public static void inviaRichiesta(Player sender, Player receiver){
        if(sender == receiver){
            sender.sendMessage("§cNon puoi mandare richiesta di teletrasporto a te stesso.");
            return;
        }
        if(!SurvivalManager.vanillaPlayers.contains(receiver)){
            sender.sendMessage("§cGiocatore non trovato.");
            return;
        }
        if(richieste.containsKey(receiver.getUniqueId()) && richieste.get(receiver.getUniqueId()).equals(sender.getUniqueId())){
            sender.sendMessage("§cHai gia' invitato richiesta di teletrasporto a questo giocatore!");
            return;
        }
        sender.sendMessage("§9Hai mandato richiesta di teletrasporto a §c" + receiver.getName() + ".");
        receiver.sendMessage("§c" + sender.getName() + " §9ti ha mandato una richiesta di teletrasporto.");
        receiver.sendMessage("§9Digita §c§o/tpaccept: §9Accetta la richiesta.");
        receiver.sendMessage("§9Digita §c§o/tpadeny: §9Rifiuta la richiesta.");
        receiver.sendMessage("§9Hai §c§o30 secondi §9per accettare o rifiutare.");
        richieste.put(receiver.getUniqueId(), sender.getUniqueId());
        tempo.put(receiver.getUniqueId(), 30);
        new BukkitRunnable() {
            @Override
            public void run() {
                if(SurvivalManager.vanillaPlayers.contains(receiver)  && SurvivalManager.vanillaPlayers.contains(sender) &&
                        richieste.containsKey(receiver.getUniqueId())){
                    if(tempo.get(receiver.getUniqueId()) == 0){
                        tempo.remove(receiver.getUniqueId());
                        richieste.remove(receiver.getUniqueId());
                        this.cancel();
                        return;
                    }
                    tempo.put(receiver.getUniqueId(), tempo.get(receiver.getUniqueId()) - 1);
                }else{
                    if(!SurvivalManager.vanillaPlayers.contains(receiver)){
                        sender.sendMessage("§c§lATTENZIONE: §9Il giocatore a cui hai mandato richiesta di teletrasporto §c§oe' uscito.");
                        sender.sendMessage("§9La richiesta di teletrasporto e' stata automaticamente §c§ocancellata.");
                    }
                    if(!SurvivalManager.vanillaPlayers.contains(sender)){
                        receiver.sendMessage("§c§lATTENZIONE: §9Il giocatore che ti ha mandato la richiesta di teletrasporto §c§oe' uscito.");
                        receiver.sendMessage("§9La richiesta di teletrasporto e' stata automaticamente §c§ocancellata.");
                    }
                    if(richieste.containsKey(receiver.getUniqueId())){
                        ConsoleMessage.send("Ho cancellato l'hashmap dal runnable.");
                        tempo.remove(receiver.getUniqueId());
                        richieste.remove(receiver.getUniqueId());
                    }
                    this.cancel();
                }
            }
        }.runTaskTimerAsynchronously(Main.plugin, 0, 20);
    }

    public static void accettaRichiesta(Player p){
        if(richieste.containsKey(p.getUniqueId())){
            Player sender = Bukkit.getPlayer(richieste.get(p.getUniqueId()));
            assert sender != null;
            sender.teleport(p);
            sender.sendMessage("§c" + p.getName() + " §9ha accettato la tua richiesta. Sei stato teletrasportato.");
            p.sendMessage("§9Hai accettato la richiesta di teletrasporto di §c" + sender.getName() + ".");
            richieste.remove(p.getUniqueId());
        }else{
            p.sendMessage("§cNon hai nessuna richiesta di teletrasporto in sospeso.");
        }
    }
}
