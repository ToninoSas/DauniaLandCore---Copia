package it.gianluca.main.GlobalCommands;

import it.gianluca.main.Main;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label,  String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(!p.hasPermission("daunia.fly")){
                p.sendMessage(Main.nopermessi);
                return true;
            }
            if(args.length > 0){
                if(Bukkit.getPlayer(args[0]) == null){
                    p.sendMessage("§cErrore: Giocatore non trovato.");
                    return true;
                }
                Player t = Bukkit.getPlayer(args[0]);
                assert t != null;
                if(t.getAllowFlight()){
                    t.setAllowFlight(false);
                    t.setFlying(false);
                    t.sendMessage("§9La fly e' stata §cdisattivata §9dall' amministratore §c" + p.getName() + ".");
                    p.sendMessage("§9La fly del giocatore §c" + t.getName() + " §9e' stata §cdisattivata.");
                    return true;
                }else{
                    t.setAllowFlight(true);
                    t.setFlying(true);
                    t.sendMessage("§9La fly e' stata §cattivata §9dall' amministratore §c" +p.getName() + ".");
                    p.sendMessage("§9La fly del giocatore §c" + t.getName() + " §9e' stata §cattivata.");
                }
            }else{
                if(p.getAllowFlight()){
                    p.setAllowFlight(false);
                    p.setFlying(false);
                    p.sendMessage("§9La fly e' stata §cdisattivata.");
                    return true;
                }else{
                    p.setAllowFlight(true);
                    p.setFlying(true);
                    p.sendMessage("§9La fly e' stata §cattivata.");
                    return true;
                }
            }
        }else{
            ConsoleMessage.send(Main.noconsole);
            return true;
        }
        return false;
    }
}
