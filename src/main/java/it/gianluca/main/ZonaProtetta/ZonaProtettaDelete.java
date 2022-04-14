package it.gianluca.main.ZonaProtetta;

import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ZonaProtettaDelete {

    private final Player player;
    private final String nomeZona;

    private final HashMap<UUID, Integer> time = new HashMap<>();

    //costruttore
    ZonaProtettaDelete(String nome, Player p)
    {
        this.nomeZona = nome;
        this.player = p;

        starting();
    }

    private void starting()
    {
        if(Objects.equals(nomeZona, ""))
        {
            player.sendMessage("Utilizzo: /zonaprotetta delete <nome zona>");
        }
        else {
            //ora deve selezionare i punti
            time.put(player.getUniqueId(), 60);
            removeZona();
        }
    }

    private void removeZona()
    {
        if(time.containsKey(player.getUniqueId()))
        {
            //trovo la sezione del giocatore
            ConfigurationSection cs =
                    Objects.requireNonNull(GestioneConfigs.protezioneConfiguration.getConfigurationSection(player.getName() + ".list"));

            //verifico che la zona esista
            if(cs.get(nomeZona) == null)
            {
                player.sendMessage("ZonaProtetta non trovata");
            }
            else
            {
                //incremento i blocchi rimasti
                ZonaProtettaManager.refreshBlockRemaining(
                        player, nomeZona);

                //setto la zona a valore nullo, cosi viene eliminata
                cs.set(nomeZona, null);
                GestioneConfigs.save();

                player.sendMessage("Zona eliminata.");
            }
        }
        else
        {
            player.sendMessage("Non hai nessuna zona da eliminare!");
        }


    }
}
