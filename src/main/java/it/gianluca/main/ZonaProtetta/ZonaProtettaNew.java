package it.gianluca.main.ZonaProtetta;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ZonaProtettaNew {
    static String nomeZona;
    static Player player;

    static HashMap<UUID, Integer> time = new HashMap<>();
    static HashMap<UUID, Location> firstPoint = new HashMap<>();
    static HashMap<UUID, Location> secondPoint = new HashMap<>();

    //costruttore
    ZonaProtettaNew(String name, Player player1)
    {
        nomeZona = name;
        player = player1;

        starting();
    }

    private void starting(){

        if(Objects.equals(nomeZona, ""))
        {
            player.sendMessage("Utilizzo: /zonaprotetta new <nome zona>");
        }
        else
        {
            //ora deve selezionare i punti
            time.put(player.getUniqueId(), 60);

            player.sendMessage("Ora puoi selezionare i 2 angoli opposti della zona");
            player.sendMessage("Con: /zonaprotetta select");
        }
    }

    //chiamato quando si fa /zonaprotetta select
    public static void selectPoint() { new ZonaProtettaSelect(player, nomeZona, firstPoint, secondPoint, time); }

}
