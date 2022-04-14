package it.gianluca.main.GlobalCommands;
import it.gianluca.main.Main;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class SkinCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length == 0) {
                p.sendMessage("§cUtilizzo: /skin <nome>.");
                return true;
            }
            new BukkitRunnable() {
                @Override
                public void run() {
                    GestioneConfigs.skinConfiguration.set(p.getName(), args[0]);
                    GestioneConfigs.save();
                }
            }.runTaskAsynchronously(Main.plugin);
            p.sendMessage("§9Skin cambiata con successo. Esci e rientra per visualizzarla correttamente.");
            return true;
        }else{
            ConsoleMessage.send(Main.noconsole);
            return true;
        }
    }
}
