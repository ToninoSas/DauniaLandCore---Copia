package it.gianluca.main.GlobalCommands;

import it.gianluca.main.Main;
import it.gianluca.main.Survival.HomeManager.HomeManager;
import it.gianluca.main.Survival.SurvivalManager;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(SurvivalManager.vanillaPlayers.contains(p)){
                HomeManager.tpHome(p);
                return true;
            }else{
                p.sendMessage(Main.comandoSconosciuto);
                return true;
            }
        }else{
            ConsoleMessage.send(Main.noconsole);
            return true;
        }
    }
}
