package it.gianluca.main.GlobalCommands;
import it.gianluca.main.Main;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ModalitaCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            p.sendMessage("§aSei connesso alla modalita: §c" + Main.modalitaConnessa.get(p.getUniqueId()));
            return true;
        }else{
            ConsoleMessage.send(Main.noconsole);
            return true;
        }
    }
}
