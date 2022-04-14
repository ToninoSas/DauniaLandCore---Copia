package it.gianluca.main.Lobby.RegisterOrLogin.Commands;
import it.gianluca.main.Lobby.RegisterOrLogin.RegOrLogManager;
import it.gianluca.main.Main;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.time.Duration;

public class RegisterCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(!RegOrLogManager.isRegistering.containsKey(p.getUniqueId())){
                p.sendMessage("§cNon puoi usare questo perche' risulti gia' registrato oppure stai effettuando il login.");
                return true;
            }
            if(args.length < 2){
                p.sendMessage("§5Utilizzo: §c/register <password> <confermaPassword>.");
                return true;
            }
            if(!args[0].equals(args[1])){
                p.sendMessage("§cLe password che hai inserito non corrispondono. Riprovare.");
                return true;
            }
            GestioneConfigs.credenzialiConfiguration.set(p.getName(), args[0]);
            GestioneConfigs.save();
            Bukkit.getScheduler().cancelTask(RegOrLogManager.taskID.get(p.getUniqueId()));
            RegOrLogManager.taskID.remove(p.getUniqueId());
            RegOrLogManager.clearPlayerStatus(p);
            RegOrLogManager.isRegistering.remove(p.getUniqueId());


            p.sendTitle(Main.nomeServer, "§7Benvenuto, §9" + p.getName(), 20, 40, 20);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aTi sei registrato!"));

            return true;
        }else{
            ConsoleMessage.send("Non puoi usare questo comando da console.");
            return true;
        }
    }
}
