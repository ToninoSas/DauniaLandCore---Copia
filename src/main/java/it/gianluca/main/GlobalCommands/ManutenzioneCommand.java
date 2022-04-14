package it.gianluca.main.GlobalCommands;

import it.gianluca.main.Main;
import it.gianluca.main.Survival.SurvivalManager;
import it.gianluca.main.Survival.VanillaMaintenance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ManutenzioneCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("daunia.manutenzione")){
            sender.sendMessage(Main.nopermessi);
            return true;
        }
        if(args.length == 0){
            sender.sendMessage("§cUtilizzo: /manutenzione <nome modalita>.");
            return true;
        }
        if(args[0].equalsIgnoreCase("vanilla")){
            if(SurvivalManager.manutenzione) VanillaMaintenance.stopVanillaMaintenance();
            else VanillaMaintenance.startVanillaMaintenance();
        }
        return false;
    }
}
