package net.nextbattle.quarry.entities;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.nextbattle.quarry.main.MainClass;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Configuration {

	public ArrayList<Material> cantbreak;
	public ArrayList<String> world_whitelist;
	public boolean privatequarries;
	public boolean autoupdate;
	public boolean updatenotify;
	public boolean continue_when_unloaded;
	public boolean continue_when_offline;
	public boolean dev_join_message;
	public boolean send_usage_data;
	public boolean world_whitelist_enabled;
    public boolean draw_all_beams;
    public boolean visual_physics_updates = true;
    public int arm_draw_delay_ticks = 1;
    // Visuals are always animated; legacy modes removed
    public int visuals_refresh_ticks = 20;
    public boolean debug_visuals = false;
    public Material speed_upgrade = Material.CLOCK;
	public Material wrench_tool = Material.BLAZE_ROD;
	public Material fuel_tool = Material.BUCKET;
    public Material fuel_finder_upgrade = Material.OAK_TRAPDOOR;
    public Material chest_miner = Material.GOLDEN_AXE;
    public Material fuel_efficiency_upgrade = Material.END_PORTAL_FRAME;
	public Material smelter_upgrade = Material.FURNACE;
	public Material liquid_miner = Material.HOPPER;
	public int globalmaxquarries = 256;
	public int maxquarriestier1 = 8;
	public int maxquarriestier2 = 8;
	public int maxquarriestier3 = 8;
	public int user_max_quarries = 24;
	public int save_interval = 60;

    private static final Map<Integer, Material> LEGACY_ID_MAP = createLegacyMap();

    @SuppressWarnings("deprecation")
    public static void loadConfig() {
        File file = new File(MainClass.plugin.getDataFolder(), "config.yml");
        FileConfiguration fc = YamlConfiguration.loadConfiguration(file);
        // Merge in any new defaults from the jar's config.yml
        try {
            InputStream defStream = MainClass.plugin.getResource("config.yml");
            if (defStream != null) {
                YamlConfiguration def = YamlConfiguration.loadConfiguration(new InputStreamReader(defStream, StandardCharsets.UTF_8));
                fc.setDefaults(def);
                fc.options().copyDefaults(true);
                fc.save(file);
            }
        } catch (Exception ignored) {}

		MainClass.config = new Configuration(false);
        List<?> list = fc.getList("ignored-blocks");
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                Material m = resolveMaterial(list.get(i), null, "ignored-blocks[" + i + "]");
                if (m != null) {
                    MainClass.config.addCantBreak(m);
                }
            }
        }
		list = fc.getList("world-whitelist");
		ArrayList<String> worlds = new ArrayList<>();
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				try {
					worlds.add(list.get(i).toString());
				} catch (Exception e) {
				}
			}
			MainClass.config.world_whitelist = worlds;
		}
        MainClass.config.speed_upgrade = resolveMaterial(fc.get("speed_upgrade"), MainClass.config.speed_upgrade, "speed_upgrade");
        MainClass.config.wrench_tool = resolveMaterial(fc.get("wrench_tool"), MainClass.config.wrench_tool, "wrench_tool");
        MainClass.config.fuel_tool = resolveMaterial(fc.get("fuel_tool"), MainClass.config.fuel_tool, "fuel_tool");
        MainClass.config.fuel_finder_upgrade = resolveMaterial(fc.get("fuel_finder_upgrade"), MainClass.config.fuel_finder_upgrade, "fuel_finder_upgrade");
        MainClass.config.fuel_efficiency_upgrade = resolveMaterial(fc.get("fuel_efficiency_upgrade"), MainClass.config.fuel_efficiency_upgrade, "fuel_efficiency_upgrade");
        MainClass.config.chest_miner = resolveMaterial(fc.get("chest_miner"), MainClass.config.chest_miner, "chest_miner");
        MainClass.config.smelter_upgrade = resolveMaterial(fc.get("smelter_upgrade"), MainClass.config.smelter_upgrade, "smelter_upgrade");
        MainClass.config.liquid_miner = resolveMaterial(fc.get("liquid_miner"), MainClass.config.liquid_miner, "liquid_miner");
		MainClass.config.privatequarries = fc.getBoolean("private-quarries");
		MainClass.config.autoupdate = fc.getBoolean("auto-update");
		MainClass.config.updatenotify = fc.getBoolean("update-notify");
		MainClass.config.continue_when_unloaded = fc
				.getBoolean("continue-when-unloaded");
		MainClass.config.continue_when_offline = fc
				.getBoolean("continue-when-offline");
		MainClass.config.dev_join_message = fc.getBoolean("dev-join-message");
		MainClass.config.send_usage_data = fc.getBoolean("send-usage-data");
		MainClass.config.globalmaxquarries = fc.getInt("global-max-quarries");
		MainClass.config.maxquarriestier1 = fc
				.getInt("user-max-tier-1-quarries");
		MainClass.config.maxquarriestier2 = fc
				.getInt("user-max-tier-2-quarries");
		MainClass.config.maxquarriestier3 = fc
				.getInt("user-max-tier-3-quarries");
		MainClass.config.world_whitelist_enabled = fc
				.getBoolean("world-whitelist-enabled");
		MainClass.config.draw_all_beams = fc.getBoolean("draw-all-beams");
        MainClass.config.user_max_quarries = fc.getInt("user-max-quarries");
        MainClass.config.save_interval = fc.getInt("save-interval");
        MainClass.config.visual_physics_updates = fc.getBoolean("visual-physics-updates", true);
        MainClass.config.arm_draw_delay_ticks = fc.getInt("arm-draw-delay-ticks", 1);
        MainClass.config.visuals_refresh_ticks = fc.getInt("visuals.refresh-ticks", 20);
        MainClass.config.debug_visuals = fc.getBoolean("debug.visuals", false);
        if (MainClass.config.save_interval == 0) {
            MainClass.config.save_interval = 60;
        }
	}

	public Configuration() {
		cantbreak = new ArrayList<>();
		cantbreak.add(Material.BEDROCK);
		cantbreak.add(Material.OBSIDIAN);
        // Stationary variants removed; WATER/LAVA cover them in modern versions.
		cantbreak.add(Material.WATER);
		cantbreak.add(Material.LAVA);
		privatequarries = true;
		autoupdate = false;
		updatenotify = true;
		continue_when_unloaded = false;
		continue_when_offline = false;
		dev_join_message = true;
		send_usage_data = true;
	}

	public Configuration(boolean default_cantbreak) {
		cantbreak = new ArrayList<>();
		if (default_cantbreak) {
			cantbreak.add(Material.BEDROCK);
			cantbreak.add(Material.OBSIDIAN);
            // Stationary variants removed; WATER/LAVA cover them in modern versions.
			cantbreak.add(Material.WATER);
			cantbreak.add(Material.LAVA);
		}
	}

	public void addCantBreak(Material mat) {
		if (!cantbreak.contains(mat) && mat != null) {
			cantbreak.add(mat);
		}
	}

    private static Map<Integer, Material> createLegacyMap() {
        Map<Integer, Material> map = new HashMap<>();
        map.put(7, Material.BEDROCK);
        map.put(49, Material.OBSIDIAN);
        map.put(8, Material.WATER);
        map.put(9, Material.WATER);
        map.put(10, Material.LAVA);
        map.put(11, Material.LAVA);
        map.put(347, Material.CLOCK);
        map.put(369, Material.BLAZE_ROD);
        map.put(325, Material.BUCKET);
        map.put(96, Material.OAK_TRAPDOOR);
        map.put(286, Material.GOLDEN_AXE);
        map.put(120, Material.END_PORTAL_FRAME);
        map.put(61, Material.FURNACE);
        map.put(154, Material.HOPPER);
        return map;
    }

    private static Material resolveMaterial(Object raw, Material fallback, String path) {
        if (raw == null) {
            return fallback;
        }
        if (raw instanceof Material) {
            return (Material) raw;
        }
        String token = raw.toString().trim();
        if (token.isEmpty()) {
            return fallback;
        }
        Material mat = null;
        try {
            mat = Material.matchMaterial(token, true);
        } catch (NoSuchMethodError ignored) {
        }
        if (mat == null) {
            mat = Material.matchMaterial(token);
        }
        if (mat != null) {
            return mat;
        }
        try {
            int id = Integer.parseInt(token);
            mat = LEGACY_ID_MAP.get(id);
            if (mat != null) {
                logResolveWarning("Migrated legacy material id " + id + " at " + path + " to " + mat.name());
                return mat;
            }
        } catch (NumberFormatException ignored) {
        }
        logResolveWarning("Unable to resolve material '" + token + "' at " + path + "; using " + (fallback != null ? fallback.name() : "default value"));
        return fallback;
    }

    private static void logResolveWarning(String message) {
        if (MainClass.plugin != null) {
            MainClass.plugin.getLogger().warning("[NeXTQuarry] " + message);
        }
    }
}
