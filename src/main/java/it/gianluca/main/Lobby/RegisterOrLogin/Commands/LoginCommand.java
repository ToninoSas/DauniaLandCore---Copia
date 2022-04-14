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

public class LoginCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(RegOrLogManager.isRegistering.containsKey(p.getUniqueId())){
                p.sendMessage("§cNon ti sei ancora registrato.");
                return true;
            }
            if(!RegOrLogManager.isLogging.containsKey(p.getUniqueId())){
                p.sendMessage("§cHai gia' effettuato il login.");
                return true;
            }
            if(args.length < 1){
                p.sendMessage("§5Utilizzo: §c/login <password>.");
                return true;
            }
            if(!args[0].equals(GestioneConfigs.credenzialiConfiguration.getString(p.getName()))){
                p.sendMessage("§cPassword errata.");
                return true;
            }
            Bukkit.getScheduler().cancelTask(RegOrLogManager.taskID.get(p.getUniqueId()));
            RegOrLogManager.clearPlayerStatus(p);
            RegOrLogManager.isLogging.remove(p.getUniqueId());
            RegOrLogManager.taskID.remove(p.getUniqueId());
            p.sendTitle(Main.nomeServer, "§7Bentornato, §9" + p.getName(), 20, 40, 20);
            p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent("§aPassword corretta!"));
            return true;
        }else{
            ConsoleMessage.send("Non puoi usare questo comando da console.");
            return true;
        }
    }
}
