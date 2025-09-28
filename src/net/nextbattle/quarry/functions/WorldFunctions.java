package net.nextbattle.quarry.functions;

import net.nextbattle.quarry.main.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

public class WorldFunctions {

	public static BlockFace getCardinalDirection(Player player) {
		double rotation = (player.getLocation().getYaw() - 90) % 360;
		if (rotation < 0) {
			rotation += 360.0;
		}
		if (0 <= rotation && rotation < 22.5) {
			return BlockFace.NORTH;
		} else if (22.5 <= rotation && rotation < 67.5) {
			return BlockFace.NORTH;
		} else if (67.5 <= rotation && rotation < 112.5) {
			return BlockFace.EAST;
		} else if (112.5 <= rotation && rotation < 157.5) {
			return BlockFace.EAST;
		} else if (157.5 <= rotation && rotation < 202.5) {
			return BlockFace.SOUTH;
		} else if (202.5 <= rotation && rotation < 247.5) {
			return BlockFace.SOUTH;
		} else if (247.5 <= rotation && rotation < 292.5) {
			return BlockFace.WEST;
		} else if (292.5 <= rotation && rotation < 337.5) {
			return BlockFace.WEST;
		} else if (337.5 <= rotation && rotation < 360.0) {
			return BlockFace.NORTH;
		} else {
			return null;
		}
	}

    // In modern APIs, setting a block's type should be done directly from a
    // region-safe context (Folia/Luminol). This method is a thin wrapper to
    // simplify call sites.
    public static void queueBlock(Block b, Material material) {
        final boolean physics;
        boolean physicsTmp = false;
        if (material == Material.COBBLESTONE_WALL && MainClass.config != null && MainClass.config.visual_physics_updates) {
            physicsTmp = true; // allow wall connections to resolve
        }
        physics = physicsTmp;
        // Ensure region-safe placement under Folia/Luminol
        try {
            final Block fb = b;
            final Material matFinal = material;
            MainClass.plugin.getServer().getRegionScheduler().execute(MainClass.plugin, fb.getLocation(), () -> {
                if (MainClass.config != null && MainClass.config.debug_visuals) {
                    MainClass.plugin.getLogger().info("[DBG] queueBlock apply " + matFinal + " at " + fb.getLocation() + " physics=" + physics + " prev=" + fb.getType());
                }
                fb.setType(matFinal, physics);
            });
        } catch (Throwable t) {
            // Fallback (should not be used under Folia)
            if (MainClass.config != null && MainClass.config.debug_visuals) {
                MainClass.plugin.getLogger().info("[DBG] queueBlock fallback apply " + material + " at " + b.getLocation() + " physics=" + physics + " prev=" + b.getType());
            }
            b.setType(material, physics);
        }
    }

    public static void processQueue() {
        // No-op: immediate set in Luminol-compatible code paths.
    }

    public static void clientRenderBlock(Location loc, Material material) {
        try {
            final Location locFinal = loc.clone();
            final Material matFinal = material;
            MainClass.plugin.getServer().getRegionScheduler().execute(MainClass.plugin, locFinal, () -> {
                BlockData data = matFinal.createBlockData();
                World world = locFinal.getWorld();
                if (world == null) return;
                for (Player p : world.getPlayers()) {
                    if (p.getWorld() != world) continue;
                    if (p.getLocation().distanceSquared(locFinal) > 128 * 128) continue;
                    if (MainClass.config != null && MainClass.config.debug_visuals) {
                        MainClass.plugin.getLogger().info("[DBG] sendBlockChange " + matFinal + " -> " + p.getName() + " at " + locFinal);
                    }
                    p.sendBlockChange(locFinal, data);
                }
            });
        } catch (Throwable ignored) {}
    }
}
