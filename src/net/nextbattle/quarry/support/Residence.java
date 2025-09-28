package net.nextbattle.quarry.support;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class Residence {

    public boolean mayEditBlock(Block b, String playerName) {
        try {
            Player p = Bukkit.getPlayerExact(playerName);
            if (p == null) return true;

            // Try Residence API via reflection: Residence.getInstance().getResidenceManager().getByLoc(loc)
            Class<?> resCls = Class.forName("com.bekvon.bukkit.residence.Residence");
            Object resObj = null;
            try {
                resObj = resCls.getMethod("getInstance").invoke(null);
            } catch (NoSuchMethodException ignored) {
                resObj = Bukkit.getPluginManager().getPlugin("Residence");
            }
            if (resObj == null) return true;

            Object manager = resObj.getClass().getMethod("getResidenceManager").invoke(resObj);
            if (manager == null) return true;
            Object claimed = manager.getClass().getMethod("getByLoc", org.bukkit.Location.class).invoke(manager, b.getLocation());
            if (claimed == null) return true; // No residence at location

            // Check permission: claimed.getPermissions().playerHas(player, "build", true)
            Object perms = claimed.getClass().getMethod("getPermissions").invoke(claimed);
            Boolean has = (Boolean) perms.getClass().getMethod("playerHas", Player.class, String.class, boolean.class)
                    .invoke(perms, p, "build", true);
            return has != null && has;
        } catch (Throwable t) {
            return true; // Fail-open if API not matched
        }
    }
}

