package it.gianluca.main.GlobalCommands;

import it.gianluca.main.Main;
import it.gianluca.main.Survival.HomeManager.HomeManager;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SethomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(Main.modalitaConnessa.get(p.getUniqueId()).equals("Vanilla")){
                HomeManager.createHome(p);
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
