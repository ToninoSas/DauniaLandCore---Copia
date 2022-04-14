package it.gianluca.main.Survival;
import it.gianluca.main.GlobalCounters.GlobalCounters;
import it.gianluca.main.Lobby.LobbyManager;
import it.gianluca.main.Main;
import it.gianluca.main.Utils.BlockAllCommands.BlockAllCommands;
import it.gianluca.main.Utils.ExpRestorer.RestoreExp;
import it.gianluca.main.Utils.ExpSaver.SaveExp;
import it.gianluca.main.Utils.FastBoard.FastBoard;
import it.gianluca.main.Utils.FoodLvlRestorer.RestoreFoodLevel;
import it.gianluca.main.Utils.FoodLvlSaver.SaveFoodLevel;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import it.gianluca.main.Utils.HealthRestorer.RestoreHealth;
import it.gianluca.main.Utils.HealthSaver.SaveHealth;
import it.gianluca.main.Utils.InventoryRestorer.RestoreInventory;
import it.gianluca.main.Utils.InventorySaver.SaveInventory;
import it.gianluca.main.Utils.ItemDropper.DropPlayerInventory;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;

import static it.gianluca.main.Utils.BlockAllCommands.BlockAllCommands.blocca;

public class SurvivalManager implements Listener {
    public static HashMap<UUID, FastBoard> survivalboards = new HashMap<>();
    public static HashMap<UUID, Integer> deathCounter = new HashMap<>();
    public static Location spawn = Objects.requireNonNull(Bukkit.getWorld("world")).getSpawnLocation();
    public static List<Player> vanillaPlayers = new ArrayList<>();
    public static boolean manutenzione = false;


    //QUI QUANDO UN GIOCATORE VIENE TELETRASPORTATO NEL SURVIVAL
    public static void tpToSurvival(Player p){
        p.setGameMode(GameMode.CREATIVE); //HIHIHIH
        vanillaPlayers.add(p);
        GlobalCounters.updateVanillaCounters();
        createSurvivalData(p);
        Main.modalitaConnessa.replace(p.getUniqueId(), "Vanilla");
        p.getInventory().clear();
        p.teleport(spawn);
        p.setGameMode(GameMode.SURVIVAL);
        survivalScoreboard(p);
        p.sendTitle("§aVanilla", Main.nomeServer, 20, 60, 20);
        loadSurvivalPlayerData(p);

        p.setInvulnerable(true);
        new BukkitRunnable() {
            @Override
            public void run() {
                p.setInvulnerable(false);
            }
        }.runTaskLater(Main.plugin, 60);
    }

    public static void createSurvivalData(Player p) {
        if (!GestioneConfigs.vanillaDatabaseConfiguration.contains(p.getName())) {
            GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".kills", 0);
            GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".deaths", 0);
            GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".health", 20);
            GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".foodlvl", 20);
            GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".exp", 0);
            GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".rank", "default");
            new BukkitRunnable() {
                @Override
                public void run() {
                    GestioneConfigs.save();
                }
            }.runTaskAsynchronously(Main.plugin);

        }
    }


    //QUI AVVIENE TUTTI PROCEDIMENTO QUANDO UN GIOCATORE ESCE DAL SURVIVAL
    public static void quitSurvival(Player p){
        if(survivalboards.containsKey(p.getUniqueId())){
            survivalboards.get(p.getUniqueId()).delete();
            survivalboards.remove(p.getUniqueId());
        }
        vanillaPlayers.remove(p);
        GlobalCounters.updateVanillaCounters();
        saveSurvivalPlayerData(p);
        if(p.isOnline()){
            //IN CASO IL GIOCATORE ABBIA DIGITATO /HUB MANDO DEI MESSAGGI A SCHERMO
            p.setFlySpeed(0.0f);
            p.setGameMode(GameMode.SPECTATOR);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999999, 10), true);

            p.sendMessage("§aTi stiamo per teletrasportare all'hub. Salvataggio dati in corso.");
            p.sendTitle("", "§7Attendere..." , 20, 40, 0);
            blocca(p, 2);
            new BukkitRunnable() {
                @Override
                public void run() {
                    //PULISCO IL GIOCATORE DA TUTTI GLI EFFETTI DI POZIONE
                    for(PotionEffect effect : p.getActivePotionEffects()){
                        p.removePotionEffect(effect.getType());
                    }
                    //RIPRISTINO LA VELOCITA' DI FLY E TELETRASPORTO IL PLAYER IN HUB
                    p.setFlySpeed(0.1f);
                    LobbyManager.tpToLobby(p);
                }
            }.runTaskLater(Main.plugin, 40);
        }
    }

    //IN CASO TUTTI I GIOCATORI VENGONO CACCIATI DAL VANILLA.
    public static void kickAllVanillaPlayers(){
        for(Player p : vanillaPlayers){
            if(survivalboards.containsKey(p.getUniqueId())){
                survivalboards.get(p.getUniqueId()).delete();
                survivalboards.remove(p.getUniqueId());
            }
            GlobalCounters.updateVanillaCounters();
            //IMPOSTO VARI EFFETTI PER EVITARE CHE IL GIOCATORE SI MUOVA / VENGA UCCISO DURANTE LA SUA USCITA
            p.setFlySpeed(0.0f);
            p.spigot().sendMessage();
            p.setGameMode(GameMode.SPECTATOR);
            p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999999, 10), true);
            if(p.isOnline()){
                //IN CASO IL GIOCATORE ABBIA DIGITATO /HUB MANDO DEI MESSAGGI A SCHERMO

                p.sendMessage("§aTi stiamo per teletrasportare all'hub. Salvataggio dati in corso.");
                p.sendTitle("", "§7Attendere..." , 20, 40, 0);
                blocca(p, 2);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        //PULISCO IL GIOCATORE DA TUTTI GLI EFFETTI DI POZIONE
                        for(PotionEffect effect : p.getActivePotionEffects()){
                            p.removePotionEffect(effect.getType());
                        }
                        //RIPRISTINO LA VELOCITA' DI FLY E TELETRASPORTO IL PLAYER IN HUB
                        p.setFlySpeed(0.1f);
                        LobbyManager.tpToLobby(p);
                    }
                }.runTaskLater(Main.plugin, 40);
            }
        }
        saveAllSurvivalPlayerData();
        vanillaPlayers.clear();
    }







    //CARICARE I DATI SURVIVAL PER IL GIOCATORE
    public static void loadSurvivalPlayerData(Player p){
        RestoreInventory.restore("Survival", p);
        RestoreHealth.restore("Survival", p);
        RestoreFoodLevel.restore("Survival", p);
        RestoreExp.restore("Survival", p);
    }







    //SALVARE I DATI SURVIVAL PER IL GIOCATORE
    public static void saveSurvivalPlayerData(Player p){
        SaveInventory.salva("Survival", p);
        SaveHealth.salva("Survival", p);
        SaveFoodLevel.salva("Survival", p);
        SaveExp.salva("Survival", p);
        GestioneConfigs.save();

    }
    //SALVARE I DATI SURVIVAL PER TUTTI I GIOCATORI ONLINE
    public static void saveAllSurvivalPlayerData(){
        for(Player p : vanillaPlayers){
            SaveInventory.salva("Survival", p);
            SaveHealth.salva("Survival", p);
            SaveFoodLevel.salva("Survival", p);
            SaveExp.salva("Survival", p);
        }
        GestioneConfigs.save();
    }










    //CREARE LA SCOREBOARD SURVIVAL
    public static void survivalScoreboard(Player p){
        new BukkitRunnable() {
            @Override
            public void run() {
                FastBoard b = new FastBoard(p);
                b.updateTitle(Main.nomeServer);
                b.updateLine(1, "§9Modalita': §a§oVanilla");
                b.updateLine(2, "");
                b.updateLine(3, "§9Nome: §f" + p.getName());
                b.updateLine(4, "§9Rank: §c[Non disponibile]");
                b.updateLine(5, "");
                b.updateLine(6, "§9Uccisioni: §a" + GestioneConfigs.vanillaDatabaseConfiguration.get(p.getName() + ".kills"));
                b.updateLine(7, "§9Morti: §c" + GestioneConfigs.vanillaDatabaseConfiguration.get(p.getName() + ".deaths"));
                b.updateLine(8, "");
                b.updateLine(9, "§9Coordinate:");
                b.updateLine(10, "§9X §f" + p.getLocation().getX() + " §9Y §f" + p.getLocation().getY() + " §9Z §f" + p.getLocation().getZ());
                b.updateLine(11, "");
                b.updateLine(12, "§9Online: §f" + vanillaPlayers.size());
                survivalboards.put(p.getUniqueId(), b);
            }
        }.runTaskAsynchronously(Main.plugin);

    }


    public static void updateCoordinates(int x, int y, int z, Player p){
        new BukkitRunnable() {
            @Override
            public void run() {
                survivalboards.get(p.getUniqueId()).updateLine(10, "§9X §f" + x + " §9Y §f" + y + " §9Z §f" + z);
            }
        }.runTaskAsynchronously(Main.plugin);

    }

    public static void addPlayerDeath(Player p){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(GestioneConfigs.vanillaDatabaseConfiguration.get(p.getName() + ".deaths") == null){
                    GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".deaths", 0);
                }
                GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".deaths",
                        GestioneConfigs.vanillaDatabaseConfiguration.getInt(p.getName() + ".deaths") + 1);
                survivalboards.get(p.getUniqueId()).updateLine(7, "§9Morti: §c" + GestioneConfigs.vanillaDatabaseConfiguration.get
                        (p.getName() + ".deaths"));
            }
        }.runTaskAsynchronously(Main.plugin);
        GestioneConfigs.save();
    }
    public static void addPlayerKill(Player p){
        new BukkitRunnable() {
            @Override
            public void run() {
                if(GestioneConfigs.vanillaDatabaseConfiguration.get(p.getName() + ".kills") == null){
                    GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".kills", 0);
                }
                GestioneConfigs.vanillaDatabaseConfiguration.set(p.getName() + ".kills",
                        GestioneConfigs.vanillaDatabaseConfiguration.getInt(p.getName() + ".kills") + 1);
                survivalboards.get(p.getUniqueId()).updateLine(6, "§9Uccisioni: §a" + GestioneConfigs.vanillaDatabaseConfiguration.get
                        (p.getName() + ".kills"));
            }
        }.runTaskAsynchronously(Main.plugin);
        GestioneConfigs.save();
    }










    //VARI EVENTI DI CONTROLLO
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Player p = e.getPlayer();
        if(vanillaPlayers.contains(p)){
            quitSurvival(p);
            GlobalCounters.updateGlobalOnlinePlayers();
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        e.setCancelled(true);
        if(vanillaPlayers.contains(e.getPlayer())){
            for(Object list : vanillaPlayers.toArray().clone()){ // TODO
                e.setCancelled(true);
                Player pls = (Player) list;
                pls.sendMessage("§6[Vanilla] §c[Ranks non disponibili] §7" + e.getPlayer().getName() + " §3: §7" + e.getMessage());
            }
        }
    }

    //DANNO PER COLPA DI CADUTA ECCETERA
    @EventHandler
    public void onDeath(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            if(e.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)){
                return;
            }
            Player p = (Player) e.getEntity();
            if(vanillaPlayers.contains(p)){
                double health = p.getHealth();
                if(health <= e.getFinalDamage()){
                    e.setCancelled(true);
                    addPlayerDeath(p);
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setLevel(0);
                    p.setExp(0);
                    DropPlayerInventory.dropPlayerInventory(p);
                    BlockAllCommands.blocca(p, 3);
                    deathCounter.put(p.getUniqueId(), 3);
                    p.setFlySpeed(0.0f);
                    p.setGameMode(GameMode.SPECTATOR);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999999, 10), true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(p.isOnline()){
                                if(deathCounter.get(p.getUniqueId()) == 0){
                                    p.setFlySpeed(0.1f);
                                    for(PotionEffect effect : p.getActivePotionEffects()){
                                        p.removePotionEffect(effect.getType());
                                    }
                                    p.setGameMode(GameMode.SURVIVAL);
                                    p.teleport(spawn);
                                    p.sendTitle("", "§aRespawnato con successo.", 20, 60, 20);
                                    deathCounter.remove(p.getUniqueId());
                                    this.cancel();
                                    return;
                                }
                                p.sendTitle("§cSei morto", "Respawm tra: §a" + deathCounter.get(p.getUniqueId()), 0, 40, 0);
                                deathCounter.put(p.getUniqueId(), deathCounter.get(p.getUniqueId()) - 1);
                            }else{
                                deathCounter.remove(p.getUniqueId());
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Main.plugin, 0, 20);
                }
            }
        }

    }
    //DANNO PER COLPA DI ENTITA
    @EventHandler
    public void onDeath(EntityDamageByEntityEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(vanillaPlayers.contains(p)){
                double health = p.getHealth();
                if(health <= e.getFinalDamage()){
                    e.setCancelled(true);
                    addPlayerDeath(p);
                    if(e.getDamager() instanceof Player){
                        addPlayerKill((Player) e.getDamager());
                    }
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setLevel(0);
                    p.setExp(0);
                    DropPlayerInventory.dropPlayerInventory(p);
                    BlockAllCommands.blocca(p, 3);
                    deathCounter.put(p.getUniqueId(), 3);
                    p.setFlySpeed(0.0f);
                    p.setGameMode(GameMode.SPECTATOR);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 999999999, 10), true);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if(p.isOnline()){
                                if(deathCounter.get(p.getUniqueId()) == 0){
                                    p.setFlySpeed(0.1f);
                                    for(PotionEffect effect : p.getActivePotionEffects()){
                                        p.removePotionEffect(effect.getType());
                                    }
                                    p.setGameMode(GameMode.SURVIVAL);
                                    p.teleport(spawn);
                                    p.sendTitle("", "§aRespawnato con successo.", 20, 60, 20);
                                    deathCounter.remove(p.getUniqueId());
                                    this.cancel();
                                    return;
                                }
                                p.sendTitle("§cSei morto", "Respawm tra: §a" + deathCounter.get(p.getUniqueId()));
                                deathCounter.put(p.getUniqueId(), deathCounter.get(p.getUniqueId()) - 1);
                            }else{
                                deathCounter.remove(p.getUniqueId());
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(Main.plugin, 0, 20);
                }
            }
        }
    }
    @EventHandler
    public void onMove(PlayerMoveEvent e){
        if(vanillaPlayers.contains(e.getPlayer())){
            if(deathCounter.containsKey(e.getPlayer().getUniqueId())){
                e.setCancelled(true);
            }
            updateCoordinates((int) e.getFrom().getX(), (int) e.getFrom().getY(), (int) e.getFrom().getZ(), e.getPlayer());
        }
    }
}
