package it.gianluca.main.ZonaProtetta;

import it.gianluca.main.Utils.GestioneConfigs.GestioneConfigs;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ZonaProtettaManager {

    static class Point {
        public int x, z;
    }

    public static int STACK = 50;

    private final String nomeZona;
    private final Player player;

    private static int blocksOccuped = 0;
    private static int blocksRemaining = STACK;

    private final HashMap<UUID, Location> firstPoint;
    private final HashMap<UUID, Location> secondPoint;

    ZonaProtettaManager(
            Player player1,
            String nomeZona1,
            HashMap<UUID, Location> firstP,
            HashMap<UUID, Location> secondP)
    {
        this.player = player1;
        this.nomeZona = nomeZona1;
        this.firstPoint = firstP;
        this.secondPoint = secondP;
    }

    public static boolean fakeModifica = false;

    public static String isClaimedBy(Block b){
        Point point = new Point();

        point.x = b.getLocation().getBlockX();
        point.z = b.getLocation().getBlockZ();

        Point oldPoint1 = new Point(), oldPoint2 = new Point();

        //analizzo 1 per 1 i player
        for(String playerNameInList : GestioneConfigs.protezioneConfiguration.getKeys(false))
        {
            //analizzo le loro zone
            for(String zona : Objects.requireNonNull(GestioneConfigs
                            .protezioneConfiguration
                            .getConfigurationSection(playerNameInList+".list"))
                    .getKeys(false))
            {
                oldPoint1.x = GestioneConfigs.protezioneConfiguration.getInt(playerNameInList+".list."+zona+".p1.x");
                oldPoint1.z = GestioneConfigs.protezioneConfiguration.getInt(playerNameInList+".list."+zona+".p1.z");

                oldPoint2.x = GestioneConfigs.protezioneConfiguration.getInt(playerNameInList+".list."+zona+".p2.x");
                oldPoint2.z = GestioneConfigs.protezioneConfiguration.getInt(playerNameInList+".list."+zona+".p2.z");

                if(findPoint(oldPoint1, oldPoint2, point))
                {
                    //punto trovato nel database
                    return playerNameInList;
                }
            }
        }

        return null;
    }
    public boolean containsClaim(Point newPoint1, Point newPoint2, Player creator)
    {
        Point oldPoint1 = new Point(), oldPoint2 = new Point();

        //analizzo 1 per 1 i player
        for(String playerNameInList : GestioneConfigs.protezioneConfiguration.getKeys(false))
        {
            //analizzo le loro zone
            for(String zona : Objects.requireNonNull(GestioneConfigs
                    .protezioneConfiguration
                    .getConfigurationSection(playerNameInList+".list"))
                    .getKeys(false))
            {
                oldPoint1.x = GestioneConfigs.protezioneConfiguration.getInt(playerNameInList+".list."+zona+".p1.x");
                oldPoint1.z = GestioneConfigs.protezioneConfiguration.getInt(playerNameInList+".list."+zona+".p1.z");

                oldPoint2.x = GestioneConfigs.protezioneConfiguration.getInt(playerNameInList+".list."+zona+".p2.x");
                oldPoint2.z = GestioneConfigs.protezioneConfiguration.getInt(playerNameInList+".list."+zona+".p2.z");

                if(findPoint(newPoint1, newPoint2, oldPoint1) || findPoint(newPoint1, newPoint2, oldPoint2))
                {
                    //punto trovato nel database
                    //il punto appartiene al player che sta reclamando?

                    if(playerNameInList.equals(creator.getName()))
                    {
                        //TO DO: RIMUOVERE UNA ZONA CONTENUTA IN UN ALTRA
                        ConfigurationSection cs = Objects.requireNonNull(
                                GestioneConfigs
                                .protezioneConfiguration
                                .getConfigurationSection(creator.getName() + ".list"));

                        refreshBlockRemaining(creator, zona);

                        cs.set(zona, null);
                        GestioneConfigs.save();

                        creator.sendMessage("La zona appena creata contiene all'interno un' altra zona.");
                        creator.sendMessage("La zona all'interno è stata eliminata automaticamente.");

                        return false;
                    }
                    else
                        return true;
                }

                //TO DO: SE LE ZONA CREATA è UGUALE A UNA PRECEDENTE, NON DEVE FARE NIENTE
            }
        }
        return false;
    }

    private static boolean findPoint(Point a, Point b, Point p)
    {

        int Xmax, Xmin, Zmax, Zmin;

        Xmax = Math.max((a.x), (b.x));
        Xmin = Math.min((a.x), (b.x));

        Zmax = Math.max((a.z), (b.z));
        Zmin = Math.min((a.z), (b.z));

        if((p.x >= Xmin && p.x <= Xmax) && (p.z >= Zmin && p.z <= Zmax)) {
            //appartiene
            return true;
        }

        return false;
    }

    private int getRemainingBlocks()
    {
        String string1 = player.getName();

        //controllo se è presente la voce blockremaining
        if(GestioneConfigs.protezioneConfiguration.get(string1 + ".blocksRemaining") != null)
        {

            //se è presente returno il valore che ha
            return GestioneConfigs.protezioneConfiguration.getInt(string1 + ".blocksRemaining");

        }else{
            //se non c'è la metto al valore stabilito, cioè STACK

            GestioneConfigs.protezioneConfiguration.set(string1 + ".blocksRemaining", blocksRemaining);
            return STACK;
        }
    }

    public boolean catchArea()
    {
        Point p1 = new Point(), p2 = new Point(), p3 = new Point();

        p1.x = firstPoint.get(player.getUniqueId()).getBlockX();
        p1.z = firstPoint.get(player.getUniqueId()).getBlockZ();

        p2.x = secondPoint.get(player.getUniqueId()).getBlockX();
        p2.z = secondPoint.get(player.getUniqueId()).getBlockZ();

        p3.x = p2.x;
        p3.z = p1.z;

        int area = calculateArea(p1, p2, p3);

        //prendo il valore di blocksremaining
        blocksRemaining = getRemainingBlocks();

        blocksOccuped = area;

        //sottraggo i blocchi occupati dai blocchi rimasti
        blocksRemaining -= blocksOccuped;

        if (blocksRemaining<=0)
            return true;

        return false;
    }

    private int calculateArea(Point p1, Point p2, Point p3)
    {
        int P1P3 = (int) Math.sqrt(Math.pow(((p3.x) - p1.x), 2) + Math.pow(((p3.z) - p1.z), 2)) + 1;
        int P2P3 = (int) Math.sqrt(Math.pow(((p2.x) - p3.x), 2) + Math.pow(((p2.z) - p3.z), 2)) + 1;

        int area = P1P3 * P2P3;
        return area;
    }

    public static void refreshBlockRemaining(Player player, String nomeZona)
    {
        String string = player.getName();

        //quando una zona viene eliminata, incremento i blocchi rimasti che prima erano occupati dalla zona
        blocksRemaining += GestioneConfigs.protezioneConfiguration.getInt(string + ".list." + nomeZona + ".blocksOccuped");

        GestioneConfigs.protezioneConfiguration.set(string + ".blocksRemaining", blocksRemaining);
        GestioneConfigs.save();
    }

    public void saveZone()
    {
        String string = player.getName() + ".list." + nomeZona ;
        String string1 = player.getName();

        //blocchi rimasti
        GestioneConfigs.protezioneConfiguration.set(string1 + ".blocksRemaining", blocksRemaining);

        GestioneConfigs.protezioneConfiguration.set(string + ".world", firstPoint.get(player.getUniqueId()).getWorld().getName());

        //blocchi occupati da ogni zona
        GestioneConfigs.protezioneConfiguration.set(string + ".blocksOccuped", blocksOccuped);

        GestioneConfigs.protezioneConfiguration.set(string + ".p1.x", firstPoint.get(player.getUniqueId()).getBlockX());
        GestioneConfigs.protezioneConfiguration.set(string + ".p1.z", firstPoint.get(player.getUniqueId()).getBlockZ());
        GestioneConfigs.protezioneConfiguration.set(string + ".p2.x", secondPoint.get(player.getUniqueId()).getBlockX());
        GestioneConfigs.protezioneConfiguration.set(string + ".p2.z", secondPoint.get(player.getUniqueId()).getBlockZ());

        GestioneConfigs.save();
    }
}
