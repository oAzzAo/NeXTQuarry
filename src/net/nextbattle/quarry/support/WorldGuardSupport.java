package net.nextbattle.quarry.support;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class WorldGuardSupport {

    private WorldGuardPlugin getWorldGuard() {
        Plugin plugin = Bukkit.getPluginManager().getPlugin("WorldGuard");
        if (!(plugin instanceof WorldGuardPlugin)) return null;
        return (WorldGuardPlugin) plugin;
    }

    public boolean mayEditBlock(Block b, String playerName) {
        WorldGuardPlugin wg = getWorldGuard();
        if (wg == null) return true;
        Player p = Bukkit.getPlayerExact(playerName);
        com.sk89q.worldedit.util.Location weLoc = BukkitAdapter.adapt(b.getLocation());
        if (p != null) {
            com.sk89q.worldguard.LocalPlayer lp = wg.wrapPlayer(p);
            return com.sk89q.worldguard.WorldGuard.getInstance()
                    .getPlatform()
                    .getRegionContainer()
                    .createQuery()
                    .testState(weLoc, lp, Flags.BUILD);
        }
        ApplicableRegionSet set = com.sk89q.worldguard.WorldGuard.getInstance()
                .getPlatform()
                .getRegionContainer()
                .createQuery()
                .getApplicableRegions(weLoc);
        return set == null || set.testState(null, Flags.BUILD);
    }
}

