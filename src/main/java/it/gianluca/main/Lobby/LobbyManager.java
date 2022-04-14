package it.gianluca.main.Lobby;
import fr.onecraft.clientstats.ClientStats;
import fr.onecraft.clientstats.ClientStatsAPI;
import it.gianluca.main.GlobalCounters.GlobalCounters;
import it.gianluca.main.Lobby.MenuModalita.ModalitaSelector;
import it.gianluca.main.Lobby.RegisterOrLogin.RegOrLogManager;
import it.gianluca.main.Main;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import it.gianluca.main.Utils.FastBoard.FastBoard;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import it.gianluca.main.Utils.ItemCreator.ItemCreator;
import it.gianluca.main.Utils.SkinManager.HTTPManager;
import it.gianluca.main.Utils.SkinManager.SkinSystem;
import it.gianluca.main.Utils.SkinManager.SkinTextureParser;
import it.gianluca.main.Utils.SkinManager.UUIDHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.*;


public class LobbyManager implements Listener {

    public static Location lobby = new Location(Bukkit.getWorld("lobby"), 0, 102, 0);
    public static HashMap<UUID, FastBoard> lobbyboards = new HashMap<>();
    public static List<Player> lobbyPlayers = new ArrayList<>();
    public static HashMap<UUID, Integer> waitTask = new HashMap<>();

    @EventHandler

    public void onPrejoin(AsyncPlayerPreLoginEvent e){
        HTTPManager httpManager = new HTTPManager();
        UUIDHelper uuidHelper = new UUIDHelper();
        SkinTextureParser skinTextureParser = new SkinTextureParser();
        String uuidData;
        //CONTROLLO SE IL GIOCATORE E' PRESENTE NEL DATABASE DELLE SKIN

        if(GestioneConfigs.skinConfiguration.contains(Objects.requireNonNull(e.getName()))){
            if(httpManager.get("https://api.mojang.com/users/profiles/minecraft/%s",
                    GestioneConfigs.skinConfiguration.getString(e.getName())).equals("error")) return;

            uuidData = httpManager.get("https://api.mojang.com/users/profiles/minecraft/%s",
                                       GestioneConfigs.skinConfiguration.getString(e.getName()));
        }
        //NEL CASO NON FOSSE PRESENTE, PROVO A PRENDERE LA SUA SKIN DAI DATABASE MOJANG

        else{
            if(httpManager.get("https://api.mojang.com/users/profiles/minecraft/%s", e.getName()).equals("error")) return;

            uuidData = httpManager.get("https://api.mojang.com/users/profiles/minecraft/%s", e.getName());

        }
        String uuid = uuidHelper.getUUID(uuidData);
        String skinData = httpManager.get("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", uuid);
        String skin = skinTextureParser.getSkin(skinData);
        String signature = skinTextureParser.getSig(skinData);
        SkinSystem.skin.put(e.getUniqueId(), skin);
        SkinSystem.signature.put(e.getUniqueId(), signature);

    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Main.Online++;
        e.setJoinMessage("");
        Player p = e.getPlayer();
        setPlayerTab(p);
        if(GestioneConfigs.skinConfiguration.contains(p.getName())){
           //SkinSystem.changeSkin(p, GestioneConfigs.skinConfiguration.getString(p.getName()));
            SkinSystem.changeSkinTest(p);

        }else{
           //SkinSystem.changeSkin(p, p.getName());
            SkinSystem.changeSkinTest(p);
        }
        tpToLobby(p);
        checkVersion(p);
        GlobalCounters.updateGlobalOnlinePlayers();

        Bukkit.getWorld("lobby").setTime(1000);

    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Main.Online--;
        e.setQuitMessage("");
        Player p = e.getPlayer();
        if(lobbyPlayers.contains(p)){
            cleanLobbyThings(p);
            GlobalCounters.updateGlobalOnlinePlayers();
        }
        Main.modalitaConnessa.remove(p.getUniqueId());
    }
    @EventHandler
    public void onDeath(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        if(p.getWorld().getName().equals("lobby")){
            e.setRespawnLocation(lobby);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(e.getPlayer().getWorld().getName().equals("lobby")){
            if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                e.setCancelled(true);
            }
        }

    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if(e.getPlayer().getWorld().getName().equals("lobby")){
            if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
                e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if(lobbyPlayers.contains(p)){
            if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
                if(!e.hasItem()) return;
                if(waitTask.containsKey(p.getUniqueId())) return;

                if(e.getItem().getItemMeta().getDisplayName().equals("§6Seleziona modalita'")){
                    ModalitaSelector.apri(p);
                    addTimeTask(p);
                }

                if(e.getItem().getItemMeta().getDisplayName().equals("§aVisibilita' giocatori: §cOn")){
                    nascondiGiocatori(p);
                    addTimeTask(p);
                }
                if(e.getItem().getItemMeta().getDisplayName().equals("§aVisibilita' giocatori: §cOff")){
                    mostraGiocatori(p);
                    addTimeTask(p);
                }

            }
        }
    }
    @EventHandler
    public void onDrop(PlayerDropItemEvent e){
        Player p = e.getPlayer();
        if(p.getWorld().getName().equals("lobby")){
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onInventory(InventoryClickEvent e){
        Player p = (Player) e.getWhoClicked();
        if(p.getWorld().getName().equals("lobby")){
            if(p.getGameMode().equals(GameMode.CREATIVE)){
                return;
            }
            e.setCancelled(true);
        }
    }
    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if(event.getLocation().getWorld().getName().equals("lobby")){
            if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.NATURAL)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if(e.getEntity() instanceof Player){
            Player p = (Player) e.getEntity();
            if(lobbyPlayers.contains(p)){
                if(e.getCause().equals(EntityDamageEvent.DamageCause.VOID)){
                    e.getEntity().teleport(lobby);
                }
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        e.setCancelled(true);
        if (lobbyPlayers.contains(e.getPlayer())){
            for (Object list : lobbyPlayers.toArray()) {
                if (RegOrLogManager.isLogging.containsKey(e.getPlayer().getUniqueId()) || RegOrLogManager.isRegistering.containsKey(e.getPlayer().getUniqueId())) {
                    e.getPlayer().sendMessage("§cEsegui prima l'autenticazione per poter parlare.");
                    return;
                }
                Player pls = (Player) list;
                pls.sendMessage("§3[Lobby] §c[Ranks non disponibili] §7" + e.getPlayer().getName() + " §3: §7" + e.getMessage());
            }
        }
    }
    @EventHandler
    public void onWeather(WeatherChangeEvent e){
        if(e.getWorld().getName().equals("lobby")){
            e.setCancelled(true);
        }
    }

    public static void tpToLobby(Player p){
        lobbyPlayers.add(p);
        checkIfHide(p);
        Main.modalitaConnessa.put(p.getUniqueId(), "Lobby");
        p.setHealth(20);
        p.getInventory().clear();
        p.setExp(0);
        p.setLevel(0);
        p.teleport(lobby);
        p.setGameMode(GameMode.ADVENTURE);
        p.getInventory().setItem(4, ItemCreator.creaItem(Material.COMPASS, "§6Seleziona modalita'"));
        p.getInventory().setItem(8, ItemCreator.creaItem(Material.LIME_DYE, "§aVisibilita' giocatori: §cOn"));
        p.getInventory().setItem(0, ItemCreator.creaItem(Material.LADDER, "§9Parkour"));
        LobbyManager.lobbyScoreboard(p);

    }

    public static void lobbyScoreboard(Player p){
        FastBoard b = new FastBoard(p);
        b.updateTitle(Main.nomeServer + " §c[Beta]");
        b.updateLine(0, "");
        b.updateLine(1, "§9Nome: §f" + p.getName());
        b.updateLine(2, "§9Rank: §c[Non disponibile]");
        b.updateLine(3, "");
        b.updateLine(4, "§9Online: §f" + Main.Online);
        b.updateLine(5, "");
        lobbyboards.put(p.getUniqueId(), b);
    }


    public static void setPlayerTab(Player p){
        p.setPlayerListHeader(Main.nomeServer + "\n");
        p.setPlayerListFooter("\n§c[In sviluppo]");

    }

    public static void checkVersion(Player p){
        ClientStatsAPI cstats = ClientStats.getApi();
        int protocolVersion = cstats.getProtocol(p.getUniqueId());
        String versionName = cstats.getVersionName(protocolVersion);
        p.sendMessage("§aLe versione di minecraft attualmente in uso e' la: §c" + versionName);
    }

    public static void cleanLobbyThings(Player p){
        if(lobbyboards.containsKey(p.getUniqueId())){
            lobbyboards.get(p.getUniqueId()).delete();
            lobbyboards.remove(p.getUniqueId());
        }
        lobbyPlayers.remove(p);
    }

    public static void nascondiGiocatori(Player p){
        for(Player toHide : Bukkit.getOnlinePlayers()){
            if(p != toHide){
                p.hidePlayer(toHide);
            }
        }
        p.getInventory().setItem(8, ItemCreator.creaItem(Material.GRAY_DYE, "§aVisibilita' giocatori: §cOff"));
    }

    public static void mostraGiocatori(Player p){
        for(Player toShow : Bukkit.getOnlinePlayers()){
            if(p != toShow){
                p.showPlayer(toShow);
            }
        }
        p.getInventory().setItem(8, ItemCreator.creaItem(Material.LIME_DYE, "§aVisibilita' giocatori: §cOn"));
    }

    public static void checkIfHide(Player p){
        for(Player pls : Objects.requireNonNull(Bukkit.getWorld("lobby")).getPlayers()){
            if(p != pls){
                if (Objects.requireNonNull(pls.getInventory().getItem(8)).getItemMeta().getDisplayName().equals("§aVisibilita' giocatori: §cOff")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            pls.hidePlayer(p);
                        }
                    }.runTaskLater(Main.plugin, 20);

                }
            }
        }
    }

    public static void addTimeTask(Player p){
        waitTask.put(p.getUniqueId(), 3);
        new BukkitRunnable() {
            @Override
            public void run() {
                waitTask.put(p.getUniqueId(), waitTask.get(p.getUniqueId()) - 1);
                if(!p.isOnline()){
                    waitTask.remove(p.getUniqueId());
                    this.cancel();
                }
                if(waitTask.get(p.getUniqueId()) == 0){
                    waitTask.remove(p.getUniqueId());
                    this.cancel();
                }
            }
        }.runTaskTimer(Main.plugin, 0, 20);
    }


}
