package it.gianluca.main.PacketsTesting;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import it.gianluca.main.Main;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.lang.reflect.InvocationTargetException;


public class Testing {
    public static void test(){
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        manager.addPacketListener(new PacketAdapter(Main.plugin, ListenerPriority.NORMAL,
                PacketType.Play.Server.ENTITY_STATUS) {
            @Override
            public void onPacketSending(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                Player p = event.getPlayer();
                if(packet.getIntegers().read(0) == p.getEntityId() &&
                        packet.getBytes().read(0) == 2) {
                    event.setCancelled(true);

                    p.playSound(p.getLocation(), Sound.ENTITY_CHICKEN_HURT, 1, 1);
                }

            }
        });

        ConsoleMessage.send("Condizione pacchetto caricata.");
    }

}
