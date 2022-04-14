package it.gianluca.main.Utils.ConsoleMessages;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

public class ConsoleMessage {
    public static void send(String message){
        Bukkit.getLogger().info(message);
    }
}
