package net.nextbattle.quarry.support;

import java.util.logging.Level;
import net.nextbattle.quarry.main.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

public class PluginSupport {

    public WorldGuardSupport wg;
	public Factions fa;
	public CoreProtect cp;
	public LogBlock_ lb;
	public Residence re;

	public PluginSupport() {
		init();
	}

    public void logPlacement(String username, Location loc, Material material, BlockData data) {
        material = material != null ? material : (loc != null ? loc.getBlock().getType() : null);
        data = data != null ? data : (loc != null ? loc.getBlock().getBlockData() : null);
        if (cp != null) {
            try {
                cp.logPlacement(username, loc, material, data);
            } catch (Exception e) {
                cp = null;
				Bukkit.getServer()
						.getLogger()
						.log(Level.INFO,
								"[NeXTQuarry] An error occurred with CoreProtect. CoreProtect has been detatched from NeXTQuarry.");
            }
        }
        if (lb != null) {
            try {
                lb.logPlacement(username, loc, material, data);
            } catch (Exception e) {
                lb = null;
                Bukkit.getServer()
                        .getLogger()
                        .log(Level.INFO,
                                "[NeXTQuarry] An error occurred with LogBlock. LogBlock has been detatched from NeXTQuarry.");
            }
        }
    }

    public void logPlacement(String username, Location loc, Material material) {
        logPlacement(username, loc, material, null);
    }

    public void logRemoval(String username, Location loc, Material material, BlockData data) {
        material = material != null ? material : (loc != null ? loc.getBlock().getType() : null);
        if (data == null && loc != null) {
            data = loc.getBlock().getBlockData();
        }
        if (cp != null) {
            try {
                cp.logRemoval(username, loc, material, data);
            } catch (Exception e) {
                cp = null;
				Bukkit.getServer()
						.getLogger()
						.log(Level.INFO,
								"[NeXTQuarry] An error occurred with CoreProtect. CoreProtect has been detatched from NeXTQuarry.");
            }
        }
        if (lb != null) {
            try {
                lb.logRemoval(username, loc, material, data);
            } catch (Exception e) {
                lb = null;
                Bukkit.getServer()
                        .getLogger()
                        .log(Level.INFO,
                                "[NeXTQuarry] An error occurred with LogBlock. LogBlock has been detatched from NeXTQuarry.");
            }
        }
    }

    public void logRemoval(String username, Location loc, Material material) {
        logRemoval(username, loc, material, null);
    }

	public boolean mayEditBlock(Block b, String playername) {
		boolean mayedit = true;
		if (wg != null) {
			if (mayedit) {
				mayedit = wg.mayEditBlock(b, playername);
			}
		}
		if (re != null) {
			if (mayedit) {
				mayedit = re.mayEditBlock(b, playername);
			}
		}
		if (fa != null) {
			if (mayedit) {
				mayedit = fa.mayEditBlock(b, playername);
			}
		}
		return mayedit;
	}

	private void init() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
            wg = new WorldGuardSupport();
			MainClass.plugin
					.getServer()
					.getLogger()
					.log(Level.INFO,
							"[NeXTQuarry] WorldGuard found & Attatched.");

		} else {
			wg = null;
		}
		if (Bukkit.getPluginManager().getPlugin("Factions") != null) {
			fa = new Factions();
			MainClass.plugin
					.getServer()
					.getLogger()
					.log(Level.INFO, "[NeXTQuarry] Factions found & Attatched.");

		} else {
			fa = null;
		}
		if (Bukkit.getPluginManager().getPlugin("Residence") != null) {
			re = new Residence();
			MainClass.plugin
					.getServer()
					.getLogger()
					.log(Level.INFO, "[NeXTQuarry] Residence found & Attatched.");
		} else {
			re = null;
		}
		if (Bukkit.getPluginManager().getPlugin("CoreProtect") != null) {
			cp = new CoreProtect();
			if (cp.isReady()) {
				MainClass.plugin
						.getServer()
						.getLogger()
						.log(Level.INFO,
								"[NeXTQuarry] CoreProtect found & Attatched.");
			} else {
				cp = null;
				MainClass.plugin.getLogger().warning("[NeXTQuarry] CoreProtect detected but API not available.");
			}

		} else {
			cp = null;
		}
		if (Bukkit.getPluginManager().getPlugin("LogBlock") != null) {
			lb = new LogBlock_();
			if (lb.isReady()) {
				MainClass.plugin
						.getServer()
						.getLogger()
						.log(Level.INFO, "[NeXTQuarry] LogBlock found & Attatched.");
			} else {
				lb = null;
				MainClass.plugin.getLogger().warning("[NeXTQuarry] LogBlock detected but API not available.");
			}
		} else {
			lb = null;
		}
	}
}
