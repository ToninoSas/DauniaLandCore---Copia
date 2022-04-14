package it.gianluca.main.Utils.GestioneConfigs;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class GestioneConfigs {

    public static File credenzialiFile, vanillaDatabaseFile, skinFile, vanillaHomesFile, protezioneFile;
    public static FileConfiguration credenzialiConfiguration, vanillaDatabaseConfiguration, skinConfiguration, vanillaHomesConfiguration, protezioneConfiguration;

    //Finds or generates the custom config file
    public static void setup(){

        credenzialiFile = new File(Bukkit.getServer().getPluginManager().getPlugin("DauniaLand").getDataFolder(),
                "Lobby/credenziali.yml");
        vanillaDatabaseFile = new File(Bukkit.getServer().getPluginManager().getPlugin("DauniaLand").getDataFolder(),
                "Vanilla/playerDatabase.yml");
        skinFile = new File(Bukkit.getServer().getPluginManager().getPlugin("DauniaLand").getDataFolder(),
                "skins.yml");
        vanillaHomesFile = new File(Bukkit.getServer().getPluginManager().getPlugin("DauniaLand").getDataFolder(),
                "Vanilla/homes.yml");
        protezioneFile = new File(Bukkit.getServer().getPluginManager().getPlugin("DauniaLand").getDataFolder(),
                "Vanilla/protezione.yml");


        CheckIfExist(credenzialiFile);
        CheckIfExist(vanillaDatabaseFile);
        CheckIfExist(skinFile);
        CheckIfExist(vanillaHomesFile);
        CheckIfExist(protezioneFile);

        credenzialiConfiguration = YamlConfiguration.loadConfiguration(credenzialiFile);
        vanillaDatabaseConfiguration = YamlConfiguration.loadConfiguration(vanillaDatabaseFile);
        skinConfiguration = YamlConfiguration.loadConfiguration(skinFile);
        vanillaHomesConfiguration = YamlConfiguration.loadConfiguration(vanillaHomesFile);
        protezioneConfiguration = YamlConfiguration.loadConfiguration(protezioneFile);
    }


    public static void save(){
        try{
            credenzialiConfiguration.save(credenzialiFile);
            vanillaDatabaseConfiguration.save(vanillaDatabaseFile);
            skinConfiguration.save(skinFile);
            vanillaHomesConfiguration.save(vanillaHomesFile);
            protezioneConfiguration.save(protezioneFile);
        }catch (IOException e){
            System.out.println("Impossibile salvare il file.");
        }
    }

    public static void reload(){
        credenzialiConfiguration = YamlConfiguration.loadConfiguration(credenzialiFile);
        vanillaDatabaseConfiguration = YamlConfiguration.loadConfiguration(vanillaDatabaseFile);
        skinConfiguration = YamlConfiguration.loadConfiguration(skinFile);
        vanillaHomesConfiguration = YamlConfiguration.loadConfiguration(vanillaHomesFile);
        protezioneConfiguration = YamlConfiguration.loadConfiguration(protezioneFile);
    }

    public static void CheckIfExist(File file){
        if (!file.exists()){
            try {
                file.createNewFile();
            }catch (IOException e){
                //eee
            }
        }
    }

}