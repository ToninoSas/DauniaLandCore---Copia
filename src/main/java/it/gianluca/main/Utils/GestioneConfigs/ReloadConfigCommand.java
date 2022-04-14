package it.gianluca.main.Utils.GestioneConfigs;

import it.gianluca.main.Main;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadConfigCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("staff.reloadconfig")){
            sender.sendMessage("Config aggiornato.");
            GestioneConfigs.reload();
            return true;
        }else{
            sender.sendMessage(Main.nopermessi);
            return true;
        }
    }
}