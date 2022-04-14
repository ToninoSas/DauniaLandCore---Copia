package it.gianluca.main.Utils.SkinManager;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import it.gianluca.main.Utils.ConsoleMessages.ConsoleMessage;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;


public class SkinSystem {

    public static HashMap<UUID, String> skin = new HashMap<>();
    public static HashMap<UUID, String> signature = new HashMap<>();


    public static void changeSkin(Player p, String skinName){
        HTTPManager httpManager = new HTTPManager();
        UUIDHelper uuidHelper = new UUIDHelper();
        SkinTextureParser skinTextureParser = new SkinTextureParser();
        String uuidData = httpManager.get("https://api.mojang.com/users/profiles/minecraft/%s", skinName);
        String uuid = uuidHelper.getUUID(uuidData);
        String skinData = httpManager.get("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", uuid);
        String skin = skinTextureParser.getSkin(skinData);
        String signature = skinTextureParser.getSig(skinData);

        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        try {
            manager.sendServerPacket(p, packet);
        } catch (InvocationTargetException e) {
            ConsoleMessage.send("Impossibile eseguire pacchetto di rimorzione.");
        }

        GameProfile gm = ((CraftPlayer)p).getHandle().fp();
        gm.getProperties().removeAll("textures");
        gm.getProperties().put("textures", new Property("textures", skin, signature));

        PacketContainer packet2 = manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet2.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        try {
            manager.sendServerPacket(p, packet2);
        } catch (InvocationTargetException e) {
            ConsoleMessage.send("Impossibile eseguire pacchetto di rimorzione.");
        }
        //p.sendMessage("Ho eseguito 2 fase");
    }

    public static void changeSkinTest(Player p){
        if(!skin.containsKey(p.getUniqueId()) && !signature.containsKey(p.getUniqueId())){
            p.sendMessage("§9Skin non disponibile o assente. Impostane una con §c/skin <nome>.");
            return;
        }
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();
        PacketContainer packet = manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        try {
            manager.sendServerPacket(p, packet);
        } catch (InvocationTargetException e) {
            ConsoleMessage.send("Impossibile eseguire pacchetto di rimorzione.");
        }
        GameProfile gm = ((CraftPlayer)p).getHandle().fp();
        gm.getProperties().removeAll("textures");
        gm.getProperties().put("textures", new Property("textures", skin.get(p.getUniqueId()), signature.get(p.getUniqueId())));

        PacketContainer packet2 = manager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet2.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        try {
            manager.sendServerPacket(p, packet2);
        } catch (InvocationTargetException e) {
            ConsoleMessage.send("Impossibile eseguire pacchetto di rimorzione.");
        }
    }

}
