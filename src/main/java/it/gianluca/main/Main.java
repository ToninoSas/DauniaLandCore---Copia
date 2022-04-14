package it.gianluca.main;

import it.gianluca.main.GlobalCommands.*;
import it.gianluca.main.Lobby.LobbyManager;
import it.gianluca.main.Lobby.MenuModalita.ModalitaEvents;
import it.gianluca.main.Lobby.MenuModalita.ModalitaSelector;
import it.gianluca.main.Lobby.RegisterOrLogin.Commands.LoginCommand;
import it.gianluca.main.Lobby.RegisterOrLogin.Commands.RegisterCommand;
import it.gianluca.main.Lobby.RegisterOrLogin.RegOrLogEvents;
import it.gianluca.main.PacketsTesting.Testing;
import it.gianluca.main.Survival.SurvivalManager;
import it.gianluca.main.Utils.BlockAllCommands.BlockAllCommands;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import it.gianluca.main.Utils.GestioneConfigs.ReloadConfigCommand;
import it.gianluca.main.Utils.LoadWorlds;
import it.gianluca.main.Utils.PerWorldTablist.PWTablist;
import it.gianluca.main.ZonaProtetta.ZonaProtetta;
import it.gianluca.main.ZonaProtetta.ZonaProtettaEvents;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public final class Main extends JavaPlugin {
    public static Main plugin;
    public static String nopermessi = "§cNon hai il permesso per usare questo comando.";
    public static String noconsole = "Non puoi usare questo comando da console.";
    public static String comandoSconosciuto = "§7§oComando sconosciuto.";
    public static int Online = 0;
    public static HashMap<UUID, String> modalitaConnessa = new HashMap<>();
    public static String nomeServer = "§6§l♔§fDauniaLand§6§l♔";
    public static String attenzione = "§c§l§oATTENZIONE: §f§7";


    @Override
    public void onEnable() {
        plugin = this;
        LoadWorlds.loadWorlds();
        GestioneConfigs.setup();
        GestioneConfigs.save();
        registraEventi();
        registraComandi();
        caricaMenuVari();
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SurvivalManager.saveAllSurvivalPlayerData();
    }

    public void registraEventi(){
        Bukkit.getPluginManager().registerEvents(new LobbyManager(), this);
        Bukkit.getPluginManager().registerEvents(new RegOrLogEvents(), this);
        Bukkit.getPluginManager().registerEvents(new ModalitaEvents(), this);
        Bukkit.getPluginManager().registerEvents(new SurvivalManager(), this);
        Bukkit.getPluginManager().registerEvents(new PWTablist(), this);
        Bukkit.getPluginManager().registerEvents(new BlockAllCommands(), this);
        Bukkit.getPluginManager().registerEvents(new ZonaProtettaEvents(), this);
    }

    public void registraComandi(){
        Objects.requireNonNull(getCommand("reloadconfig")).setExecutor(new ReloadConfigCommand());
        Objects.requireNonNull(getCommand("register")).setExecutor(new RegisterCommand());
        Objects.requireNonNull(getCommand("login")).setExecutor(new LoginCommand());
        Objects.requireNonNull(getCommand("hub")).setExecutor(new HubCommand());
        Objects.requireNonNull(getCommand("modalita")).setExecutor(new ModalitaCommand());
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new GamemodeCommands());
        Objects.requireNonNull(getCommand("gms")).setExecutor(new GamemodeCommands());
        Objects.requireNonNull(getCommand("gmc")).setExecutor(new GamemodeCommands());
        Objects.requireNonNull(getCommand("gmsp")).setExecutor(new GamemodeCommands());
        Objects.requireNonNull(getCommand("gmadv")).setExecutor(new GamemodeCommands());
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand());
        Objects.requireNonNull(getCommand("gmessage")).setExecutor(new GlobalMessageCommand());
        Objects.requireNonNull(getCommand("skin")).setExecutor(new SkinCommand());
        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SethomeCommand());
        Objects.requireNonNull(getCommand("tpa")).setExecutor(new TPACommand());
        Objects.requireNonNull(getCommand("tpaccept")).setExecutor(new TPAcceptCommand());
        Objects.requireNonNull(getCommand("manutenzione")).setExecutor(new ManutenzioneCommand());
        Objects.requireNonNull(getCommand("zonaprotetta")).setExecutor(new ZonaProtetta());


    }

    public void caricaMenuVari(){
        ModalitaSelector.crea();
    }
}
