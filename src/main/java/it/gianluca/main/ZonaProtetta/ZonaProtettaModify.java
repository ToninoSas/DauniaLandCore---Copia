package it.gianluca.main.ZonaProtetta;

import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ZonaProtettaModify {

    static String nomeZona;
    static Player player;

    static HashMap<UUID, Integer> time = new HashMap<>();
    static HashMap<UUID, Location> firstPoint = new HashMap<>();
    static HashMap<UUID, Location> secondPoint = new HashMap<>();

    ZonaProtettaModify(String name, Player player1){
        nomeZona = name;
        player = player1;

        starting();
    }

    private void starting()
    {
        if(Objects.equals(nomeZona, ""))
        {
            player.sendMessage("Utilizzo: /zonaprotetta modify <nome zona>");
        }
        else
        {
            //controllo se la zona esiste
            if (ifZonaExist())
            {
                //ora deve selezionare i punti
                time.put(player.getUniqueId(), 60);

                player.sendMessage("Ora puoi selezionare i 2 angoli opposti della zona");
                player.sendMessage("Con: /zonaprotetta select");
            }
        }
    }

    private boolean ifZonaExist()
    {
        ConfigurationSection cs =
                Objects.requireNonNull(
                        GestioneConfigs.protezioneConfiguration.getConfigurationSection(player.getName() + ".list"));

        if(cs.get(nomeZona) == null)
        {
            player.sendMessage("Zona protetta non trovata.");
            return false;
        }

        //TO DO: quando si modifica una zona, il contatore dei blocchi rimasti puo crescere o decrescere

        return true;
    }

    //chiamato quando si fa /zonaprotetta select
    public static void selectPoint(){
        new ZonaProtettaSelect(player, nomeZona ,firstPoint,secondPoint, time);
    }

}
