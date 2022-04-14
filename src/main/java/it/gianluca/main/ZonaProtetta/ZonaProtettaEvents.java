package it.gianluca.main.ZonaProtetta;

import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.player.PlayerInteractEvent;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ZonaProtettaEvents implements Listener {

    @EventHandler
    public void onFire(BlockBurnEvent e){
        Block b = e.getBlock();

        String owner = ZonaProtettaManager.isClaimedBy(b);

        if(owner != null){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplosion(BlockExplodeEvent e){
        Block b = e.getBlock();

        String owner = ZonaProtettaManager.isClaimedBy(b);

        if(owner != null){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        Player p = e.getPlayer();
        Block b = e.getBlock();

        String owner = ZonaProtettaManager.isClaimedBy(b);

        if(!Objects.equals(owner, p.getName()) && owner != null){
            e.setCancelled(true);
            p.sendMessage("Questa zona appartiene a " + owner);
        }

    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Block b = e.getBlock();

        String owner = ZonaProtettaManager.isClaimedBy(b);

        if(!Objects.equals(owner, p.getName()) && owner != null){
            p.sendMessage("Questa zona appartiene a " + owner);
            e.setCancelled(true);

        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Block b = e.getClickedBlock();

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
            assert b != null;
            if(b.getType().isInteractable()){
                String owner = ZonaProtettaManager.isClaimedBy(b);
                if(!Objects.equals(owner, p.getName()) && owner != null){
                    e.setCancelled(true);
                    p.sendMessage("Questa zona appartiene a " + owner);
                }
            }
        }

    }


}
