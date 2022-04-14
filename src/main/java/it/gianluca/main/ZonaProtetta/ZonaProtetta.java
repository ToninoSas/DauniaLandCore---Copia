package it.gianluca.main.ZonaProtetta;

import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ZonaProtetta implements CommandExecutor {

    void empty(Player p)
    {
        String string = p.getName();

        if(GestioneConfigs.protezioneConfiguration.get(string) == null ||
                GestioneConfigs.protezioneConfiguration.get(string+ ".list") == null ||
                Objects.requireNonNull(GestioneConfigs
                        .protezioneConfiguration
                        .getConfigurationSection(string + ".list"))
                        .getKeys(false)
                        .isEmpty())
        {
            p.sendMessage("Non hai zone protette.");
            p.sendMessage(" Utilizza </zonaprotetta new> per crearne una.");

        } else{
            ConfigurationSection cs = GestioneConfigs.protezioneConfiguration.getConfigurationSection(p.getName());

            assert cs != null;
            int blocksRemaining = cs.getInt("blocksRemaining");

            String listaZone = "";
            p.sendMessage("Zone Protette disponibili: ");

            for(String zona : Objects.requireNonNull(cs.getConfigurationSection("list")).getKeys(false))
            {
                listaZone = listaZone + zona + ", ";
            }

            p.sendMessage(listaZone);

            p.sendMessage("Blocchi rimasti: " + blocksRemaining);
        }
    }

    private void helper(Player p) {
        p.sendMessage("Comandi disponibili: ");
        p.sendMessage("/zonaprotetta new <nome zona>");
        p.sendMessage("/zonaprotetta delete <nome zona>");
        p.sendMessage("/zonaprotetta modify <nome zona>");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player p = (Player) sender;

        if (args.length == 0)
        {
            empty(p);
        }
        else if(args.length == 1)
        {
            switch (args[0]){
                case "select":
                    if (!ZonaProtettaNew.time.isEmpty())
                        //accesso statico a una funzione
                        ZonaProtettaNew.selectPoint();
                    else if(!ZonaProtettaModify.time.isEmpty())
                        ZonaProtettaModify.selectPoint();
                    else
                        p.sendMessage("Non hai cnessun punto da selezionare");
                    break;
                case "new":
                    new ZonaProtettaNew("", p);
                    break;
                case "delete":
                    new ZonaProtettaDelete("", p);
                    break;
                case "modify":
                    new ZonaProtettaModify("", p);
                    break;
                default:
                    helper(p);
                    break;
            }
            return true;
        }
        else if(args.length == 2)
        {
            switch (args[0]) {
                case "new":
                    new ZonaProtettaNew(args[1], p);
                    break;
                case "delete":
                    new ZonaProtettaDelete(args[1], p);
                    break;
                case "modify":
                    new ZonaProtettaModify(args[1], p);
                    break;
                default:
                    //helper
                    helper(p);
                    break;
            }
            return true;
        }
        else
        {
            helper(p);
        }
        return false;
    }
}
