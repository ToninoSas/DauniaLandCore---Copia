package it.gianluca.main.ZonaProtetta;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class ZonaProtettaSelect {

    private final Player player;
    private final String nomeZona;

    private final HashMap<UUID, Integer> time;
    private final HashMap<UUID, Location> firstPoint;
    private final HashMap<UUID, Location> secondPoint;

    ZonaProtettaSelect(
            Player player1,
            String nomeZona1,
            HashMap<UUID, Location> firstP,
            HashMap<UUID, Location> secondP,
            HashMap<UUID, Integer> time1
            )
    {
        this.player = player1;
        this.nomeZona = nomeZona1;
        this.firstPoint = firstP;
        this.secondPoint = secondP;
        this.time = time1;

        select();
    }

    private void select()
    {
        //il giocatore puo fare questo comando?
        if(time.containsKey(player.getUniqueId()))
        {
            Location PointLocation = player.getLocation();

            ZonaProtettaManager.Point p1 = new ZonaProtettaManager.Point(),
                    p2 = new ZonaProtettaManager.Point();

            if (firstPoint.isEmpty())
            {
                //primo punto nel primo hashmap
                firstPoint.put(player.getUniqueId(), PointLocation);

                p1.x = firstPoint.get(player.getUniqueId()).getBlockX();
                p1.z = firstPoint.get(player.getUniqueId()).getBlockZ();

                player.sendMessage("Primo punto registrato.");
                player.sendMessage("Coordinate: (X): " + p1.x + " | (Z): " + p1.z);
                player.sendMessage("Ti manca un punto!");
                return;

            }
            else if (secondPoint.isEmpty())
            {
                //secondo punto nel secondo hashmap
                secondPoint.put(player.getUniqueId(), PointLocation);

                p2.x = secondPoint.get(player.getUniqueId()).getBlockX();
                p2.z = secondPoint.get(player.getUniqueId()).getBlockZ();

                player.sendMessage("Secondo punto registrato.");
                player.sendMessage("Coordinate: (X): " + p2.x + " | (Z): " + p2.z);
            }
            else
            {
                player.sendMessage("Non devi registrare nessun punto!");
                return;
            }

            //riprendo il primo punto registrato precedentemente
            p1.x = firstPoint.get(player.getUniqueId()).getBlockX();
            p1.z = firstPoint.get(player.getUniqueId()).getBlockZ();

            ZonaProtettaManager manager = new ZonaProtettaManager(player, nomeZona, firstPoint, secondPoint);

            //devo controllare se contiene claims
            if(manager.containsClaim(p1, p2, player))
            {
                player.sendMessage("La zona è gia stata reclamata!");
                pulezz();
                return;
            }

            //controllo l'area
            if (manager.catchArea())
            {
                player.sendMessage("Non hai abbastanza spazio libero per creare un altra zona!");
                player.sendMessage("Creazione zona annullata.");
                return;
            }

            // TO DO
//            if (ZonaProtettaManager.fakeModifica){
//                player.sendMessage("La zona " + nomeZona + " non ha subito modifiche!");
//                return;
//            }

            //TO DO: evento per bloccare i player nelle zone protette

            manager.saveZone();

            player.sendMessage("Zona <" + nomeZona + "> salvata!");

            pulezz();
        }
        else
        {
            player.sendMessage("Non hai nessun punto da selezionare!");
        }
    }

    private void pulezz()
    {
        this.firstPoint.clear();
        this.secondPoint.clear();
        this.time.clear();
    }


}
