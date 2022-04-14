package it.gianluca.main.GlobalCommands;
import it.gianluca.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GlobalMessageCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.hasPermission("daunialand.globalmessage")){
            sender.sendMessage(Main.nopermessi);
            return true;
        }
        if(args.length == 0){
            sender.sendMessage("§cUtilizzo: /gmessage <messaggio>.");
            return true;
        }
        String messaggio = "";
        for(int i = 0; i < args.length; i++){
            messaggio = messaggio + " " + args[i];
        }
        Bukkit.broadcastMessage(Main.attenzione + messaggio);
        return true;
    }

}
