package it.gianluca.main.GlobalCommands;
import it.gianluca.main.Main;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class GamemodeCommands implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(p.hasPermission("daunialand.gamemode")){
                if(command.getLabel().equalsIgnoreCase("gamemode")){
                    if(args.length == 0){
                        p.sendMessage("§cUtilizzo: /gamemode <survival/creative/avventura/spettatore> [NomeGiocatore].");
                        return true;
                    }
                    if (args.length == 1){
                        setPlayerGamemode(p, args[0]);
                        return true;
                    }else{
                        setOtherPlayerGamemode(p, Bukkit.getPlayer(args[1]), args[0]);
                        return true;
                    }
                }
                if(command.getLabel().equalsIgnoreCase("gms")){
                    if(args.length >= 1){
                        setOtherPlayerGamemode(p, Bukkit.getPlayer(args[0]), "survival");
                        return true;
                    }else{
                        setPlayerGamemode(p, "survival");
                        return true;
                    }
                }
                if(command.getLabel().equalsIgnoreCase("gmc")){
                    if(args.length >= 1){
                        setOtherPlayerGamemode(p, Bukkit.getPlayer(args[0]), "creative");
                        return true;
                    }else{
                        setPlayerGamemode(p, "creative");
                        return true;
                    }
                }
                if(command.getLabel().equalsIgnoreCase("gmsp")){
                    if(args.length >= 1){
                        setOtherPlayerGamemode(p, Bukkit.getPlayer(args[0]), "spettatore");
                        return true;
                    }else{
                        setPlayerGamemode(p, "spettatore");
                        return true;
                    }
                }
                if(command.getLabel().equalsIgnoreCase("gmadv")){
                    if(args.length >= 1){
                        setOtherPlayerGamemode(p, Bukkit.getPlayer(args[0]), "avventura");
                    }else{
                        setPlayerGamemode(p, "avventura");
                    }
                }

            }else{
                p.sendMessage(Main.nopermessi);
                return true;
            }
        }else{
            ConsoleMessage.send(Main.noconsole);
            return true;
        }
        return false;
    }

    public void setPlayerGamemode(Player p, String Gamemode){
        if(Gamemode.equalsIgnoreCase("survival")){
            p.setGameMode(GameMode.SURVIVAL);
            p.sendMessage("§9La tua gamemode e' stata impostata su §csurvival.");
        }else if(Gamemode.equalsIgnoreCase("creative")){
            p.setGameMode(GameMode.CREATIVE);
            p.sendMessage("§9La tua gamemode e' stata impostata su §ccreative.");
        }else if(Gamemode.equalsIgnoreCase("avventura")){
            p.setGameMode(GameMode.ADVENTURE);
            p.sendMessage("§9La tua gamemode e' stata impostata su §cavventura.");
        }else if(Gamemode.equalsIgnoreCase("spettatore")){
            p.setGameMode(GameMode.SPECTATOR);
            p.sendMessage("§9La tua gamemode e' stata impostata su §cspettatore.");
        }else{
            p.sendMessage("§cErrore: Non hai digitato la gamemode corretta.");
        }
    }

    public void setOtherPlayerGamemode(Player whoExec, Player t , String Gamemode){
        if (t == null){
            whoExec.sendMessage("§cErrore: Giocatore non trovato.");
            return;
        }
        if(Gamemode.equalsIgnoreCase("survival")){
            t.setGameMode(GameMode.SURVIVAL);
            t.sendMessage("§9La tua gamemode e' stata impostata su §csurvival §9dall'amministratore §c" + whoExec.getName() + ".");
            whoExec.sendMessage("§9La gamemode del giocatore §c" + t.getName() + " §9e' stata impostata su §csurvival.");
        }else if(Gamemode.equalsIgnoreCase("creative")){
            t.setGameMode(GameMode.CREATIVE);
            t.sendMessage("§9La tua gamemode e' stata impostata su §ccreative §9dall'amministratore §c" + whoExec.getName() + ".");
            whoExec.sendMessage("§9La gamemode del giocatore §c" + t.getName() + " §9e' stata impostata su §ccreative.");
        }else if(Gamemode.equalsIgnoreCase("avventura")){
            t.setGameMode(GameMode.ADVENTURE);
            t.sendMessage("§9La tua gamemode e' stata impostata su §cavventura §9dall'amministratore §c" + whoExec.getName() + ".");
            whoExec.sendMessage("§9La gamemode del giocatore §c" + t.getName() + " §9e' stata impostata su §cavventura.");
        }else if(Gamemode.equalsIgnoreCase("spettatore")){
            t.setGameMode(GameMode.SPECTATOR);
            t.sendMessage("§9La tua gamemode e' stata impostata su §cspettatore §9dall'amministratore §c" + whoExec.getName() + ".");
            whoExec.sendMessage("§9La gamemode del giocatore §c" + t.getName() + " §9e' stata impostata su §cspettatore.");
        }else{
            whoExec.sendMessage("§cErrore: Non hai digitato la gamemode corretta.");
        }
    }
}
