package net.nextbattle.quarry.main;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import net.nextbattle.quarry.entities.CommandHandler;
import net.nextbattle.quarry.entities.Configuration;
import net.nextbattle.quarry.entities.CustomItems;
import net.nextbattle.quarry.entities.Quarry;
import net.nextbattle.quarry.visual.ArmAnimator;
import net.nextbattle.quarry.support.PluginSupport;

import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.HandlerList;

public final class MainClass extends JavaPlugin {

	public static Plugin plugin;
	public static Configuration config;
	public static CustomItems citems;
    private ScheduledTask tickTask;
    private ScheduledTask saveTask;
    private ScheduledTask cleanupTask;
	public static CommandHandler ch;
	public static PluginSupport ps;

	public static void main(String[] args) {
		System.out
				.println("NeXTQuarry is a plugin for MineCraft servers based on CraftBukkit.");
		System.out
				.println("Please place NeXTQuarry.jar (this file) in the plugins directory of your CraftBukkit installation.");
		System.out
				.println("You can then use it by launching the Craftbukkit server.");
	}

	public void log(String s) {
		getLogger().log(Level.INFO, s);
	}

    @Override
    public void onEnable() {
        // Define plugin
        plugin = this;

        // Defensive cleanup for hot-loads: ensure no duplicate listeners or tasks
        try { HandlerList.unregisterAll(this); } catch (Throwable ignored) {}
        if (tickTask != null) { try { tickTask.cancel(); } catch (Throwable ignored) {} tickTask = null; }
        if (saveTask != null) { try { saveTask.cancel(); } catch (Throwable ignored) {} saveTask = null; }
        if (cleanupTask != null) { try { cleanupTask.cancel(); } catch (Throwable ignored) {} cleanupTask = null; }

        // Event Listener
        getServer().getPluginManager().registerEvents(
                new GeneralEventListener(), plugin);

        // Cleanup orphaned displays from previous runs (including any worlds not yet loaded)
        cleanupTask = getServer().getGlobalRegionScheduler().runAtFixedRate(this, task -> {
            for (org.bukkit.World world : getServer().getWorlds()) {
                ArmAnimator.removeOrphanDisplays(world);
            }
        }, 20L, 20L * 30); // sweep every 30 seconds

		// Data Folders
		if (!plugin.getDataFolder().exists()) {
			plugin.getDataFolder().mkdir();
		}
		File quarrydir = new File(plugin.getDataFolder(), "/quarries");
		if (!quarrydir.exists()) {
			quarrydir.mkdir();
		}

        // Initialize Command Handler
        ch = new CommandHandler();
        getCommand("nextquarry").setExecutor(ch);

		// Config
		if (!(new File(plugin.getDataFolder(), "config.yml")).exists()) {
			plugin.saveDefaultConfig();
		}
		plugin.reloadConfig();
		Configuration.loadConfig();

		// Initialize Custom Items
		citems = new CustomItems();
		citems.addRecipes();

		// Plugin Support
		ps = new PluginSupport();

        // Load Quarries
		File dir = new File(plugin.getDataFolder(), "/quarries");
		for (File child : dir.listFiles()) {
			Quarry.LoadQuarry(child);
		}

        // Main Timer (Luminol/Folia safe): schedule globally and dispatch per-quarry in-region
        this.tickTask = getServer().getGlobalRegionScheduler().runAtFixedRate(this, task -> {
            for (Quarry q : Quarry.quarrylist) {
                Location base = q.getBaseLocation();
                if (base == null || base.getWorld() == null) continue;
                getServer().getRegionScheduler().execute(this, base, () -> {
                    try {
                        q.doTick();
                    } catch (Throwable t) {
                        getLogger().log(Level.SEVERE, "Error ticking quarry", t);
                    }
                });
            }
        }, 1L, 5L);

        this.saveTask = getServer().getGlobalRegionScheduler().runAtFixedRate(this, task -> {
            try {
                getServer().getLogger().log(Level.INFO, "NeXTQuarry: Saving quarries...");
                Quarry.saveAll();
                getServer().getLogger().log(Level.INFO, "NeXTQuarry: All quarries saved.");
            } catch (Throwable t) {
                getLogger().log(Level.SEVERE, "Error saving quarries", t);
            }
        }, Math.max(1L, 20L * (long) config.save_interval), Math.max(1L, 20L * (long) config.save_interval));
	}

    @Override
    public void onDisable() {
        try { HandlerList.unregisterAll(this); } catch (Throwable ignored) {}
        try {
            if (citems != null) citems.removeRecipes();
        } catch (Throwable ignored) {}
        if (tickTask != null) tickTask.cancel();
        if (saveTask != null) saveTask.cancel();
        if (cleanupTask != null) cleanupTask.cancel();
        getServer().getLogger().log(Level.INFO,
                "NeXTQuarry: Saving quarries...");
        Quarry.saveAll();
        getServer().getLogger().log(Level.INFO,
                "NeXTQuarry: All quarries saved.");
    }

    public static int scheduleCleanup(org.bukkit.Location center, double radius) {
        if (center == null || center.getWorld() == null) return 0;
        org.bukkit.World world = center.getWorld();
        int chunkRadius = Math.max(1, (int) Math.ceil(radius / 16.0));
        int originChunkX = center.getBlockX() >> 4;
        int originChunkZ = center.getBlockZ() >> 4;
        org.bukkit.Location targetCenter = center.clone();
        double radiusFinal = radius;
        for (int dx = -chunkRadius; dx <= chunkRadius; dx++) {
            for (int dz = -chunkRadius; dz <= chunkRadius; dz++) {
                final int chunkX = originChunkX + dx;
                final int chunkZ = originChunkZ + dz;
                ArmAnimator.scheduleChunkSweep(world, chunkX, chunkZ, targetCenter, radiusFinal, false);
            }
        }
        return (chunkRadius * 2 + 1) * (chunkRadius * 2 + 1);
    }
}
