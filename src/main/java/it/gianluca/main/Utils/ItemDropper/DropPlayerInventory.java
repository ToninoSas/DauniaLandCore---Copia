package it.gianluca.main.Utils.ItemDropper;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DropPlayerInventory {
    public static void dropPlayerInventory(Player p){
        try
        {
            for (ItemStack itemStack : p.getInventory().getContents()) {
                p.getWorld().dropItemNaturally(p.getLocation(), itemStack);
                p.getInventory().removeItem(itemStack);
            }
            for (ItemStack itemStack : p.getInventory().getArmorContents()) {
                p.getWorld().dropItemNaturally(p.getLocation(), itemStack);
                p.getInventory().removeItem(itemStack);
            }
        }
        catch(IllegalArgumentException e)
        {

        }

        p.getInventory().clear();
    }
}
