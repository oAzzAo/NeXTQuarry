package net.nextbattle.quarry.entities;

import java.util.ArrayList;
import java.util.List;
import net.nextbattle.quarry.main.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItems {

	public ItemStack quarry_tier1;
	public ItemStack quarry_tier2;
	public ItemStack quarry_tier3;
	public ItemStack speed_upgrade;
	public ItemStack wrench_tool;
	public ItemStack fuel_tool;
	public ItemStack fuel_finder_upgrade;
	public ItemStack chest_miner;
	public ItemStack fuel_efficiency_upgrade;
	public ItemStack smelter_upgrade;
    public ItemStack liquid_miner;
    private final List<NamespacedKey> registeredKeys = new ArrayList<>();

	public CustomItems() {
		// Work Vars
		ItemStack is;
		ItemMeta meta;
		ArrayList<String> lorelist;

		// Fuel Efficiency Upgrade
		is = new ItemStack(MainClass.config.fuel_efficiency_upgrade, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN
				+ "Fuel Efficiency Upgrade");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Makes the quarry last longer per unit of fuel");
		lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 3 per quarry");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		fuel_efficiency_upgrade = is;

		// Smelter Upgrade
		is = new ItemStack(MainClass.config.smelter_upgrade, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN
				+ "Smelter Upgrade");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Gives the quarry the ability to smelt ores");
		lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 1 per quarry");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		smelter_upgrade = is;

		// Liquid Miner
		is = new ItemStack(MainClass.config.liquid_miner, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN
				+ "Liquid Miner");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Allows buckets to be filled with found liquids");
		lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 1 per quarry");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		liquid_miner = is;

		// Tier 1 Quarry
		is = new ItemStack(Material.IRON_BLOCK, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GRAY
				+ "Quarry [Tier 1]");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Mines a 16x16 area");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		quarry_tier1 = is;

		// Tier 2 Quarry
		is = new ItemStack(Material.GOLD_BLOCK, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Quarry [Tier 2]");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Mines a 32x32 area");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		quarry_tier2 = is;

		// Tier 3 Quarry
		is = new ItemStack(Material.OBSIDIAN, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.AQUA
				+ "Quarry [Tier 3]");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Mines a 48x48 area");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		quarry_tier3 = is;

		// Speed Upgrade
		is = new ItemStack(MainClass.config.speed_upgrade, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN
				+ "Quarry Speed Upgrade");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Makes the quarry a bit faster");
		lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 3 per quarry");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		speed_upgrade = is;

		// Fuel Finder Upgrade
		is = new ItemStack(MainClass.config.fuel_finder_upgrade, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN
				+ "Quarry Fuel Finder");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Automatically transfers coal found to the fuel bay");
		lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 1 per quarry");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		fuel_finder_upgrade = is;

		// Wrench Tool
		is = new ItemStack(MainClass.config.wrench_tool, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW
				+ "Quarry Wrench");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Right click a quarry with this to apply upgrades!");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		wrench_tool = is;

		// Fuel Tool
		is = new ItemStack(MainClass.config.fuel_tool, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.YELLOW
				+ "Quarry Fuel Injector");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Right click a quarry with this to inject fuel!");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		fuel_tool = is;

		// Chest Miner
		is = new ItemStack(MainClass.config.chest_miner, 1);
		meta = is.getItemMeta();
		meta.setDisplayName(ChatColor.RESET + "" + ChatColor.GREEN
				+ "Chest Mining Upgrade");
		lorelist = new ArrayList<String>();
		lorelist.add(ChatColor.RESET + "" + ChatColor.GOLD
				+ "Allows you to mine the contents of chests.");
		lorelist.add(ChatColor.RESET + "" + ChatColor.RED + "Max 1 per quarry");
		meta.setLore(lorelist);
		is.setItemMeta(meta);
		chest_miner = is;

	}

    public void addRecipes() {
        // Work Variable
        ShapedRecipe recipe;

		// Fuel Efficiency Upgrade
        NamespacedKey keyFuelEff = new NamespacedKey(MainClass.plugin, "fuel_efficiency_upgrade");
        recipe = new ShapedRecipe(keyFuelEff, fuel_efficiency_upgrade);
        recipe.shape("ABA", "BCB", "ABA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.REDSTONE);
        recipe.setIngredient('C', Material.DIAMOND);
        registerRecipeIfAbsent(keyFuelEff, recipe);

		// Liquid Miner
        NamespacedKey keyLiquidMiner = new NamespacedKey(MainClass.plugin, "liquid_miner");
        recipe = new ShapedRecipe(keyLiquidMiner, liquid_miner);
        recipe.shape("A A", "A A", " B ");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.BUCKET);
        registerRecipeIfAbsent(keyLiquidMiner, recipe);

		// Tier 1 Quarry
        NamespacedKey keyT1 = new NamespacedKey(MainClass.plugin, "quarry_tier1");
        recipe = new ShapedRecipe(keyT1, quarry_tier1);
        recipe.shape("ABA", "ACA", "ADA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.REDSTONE);
        recipe.setIngredient('C', Material.HOPPER);
        recipe.setIngredient('D', Material.DIAMOND_PICKAXE);
        registerRecipeIfAbsent(keyT1, recipe);

		// Tier 2 Quarry
        NamespacedKey keyT2 = new NamespacedKey(MainClass.plugin, "quarry_tier2");
        recipe = new ShapedRecipe(keyT2, quarry_tier2);
        recipe.shape("ABA", "ACA", "ADA");
        recipe.setIngredient('A', Material.GOLD_INGOT);
        recipe.setIngredient('B', Material.REDSTONE);
        recipe.setIngredient('C', Material.HOPPER);
        recipe.setIngredient('D', Material.DIAMOND_PICKAXE);
        registerRecipeIfAbsent(keyT2, recipe);

		// Tier 3 Quarry
        NamespacedKey keyT3 = new NamespacedKey(MainClass.plugin, "quarry_tier3");
        recipe = new ShapedRecipe(keyT3, quarry_tier3);
        recipe.shape("ABA", "ACA", "ADA");
        recipe.setIngredient('A', Material.OBSIDIAN);
        recipe.setIngredient('B', Material.REDSTONE_BLOCK);
        recipe.setIngredient('C', Material.HOPPER);
        recipe.setIngredient('D', Material.DIAMOND_PICKAXE);
        registerRecipeIfAbsent(keyT3, recipe);

		// Speed Upgrade
        NamespacedKey keySpeed = new NamespacedKey(MainClass.plugin, "speed_upgrade");
        recipe = new ShapedRecipe(keySpeed, speed_upgrade);
        recipe.shape("ADA", "BCB", "ADA");
        recipe.setIngredient('A', Material.GOLD_INGOT);
        recipe.setIngredient('B', Material.GLASS);
        recipe.setIngredient('C', Material.REDSTONE);
        recipe.setIngredient('D', Material.STRING);
        registerRecipeIfAbsent(keySpeed, recipe);

		// Fuel Finder Upgrade
        NamespacedKey keyFuelFinder = new NamespacedKey(MainClass.plugin, "fuel_finder_upgrade");
        recipe = new ShapedRecipe(keyFuelFinder, fuel_finder_upgrade);
        recipe.shape("ABA", "BCB", "ABA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.STICK);
        recipe.setIngredient('C', Material.COAL);
        registerRecipeIfAbsent(keyFuelFinder, recipe);

		// Wrench Tool
        NamespacedKey keyWrench = new NamespacedKey(MainClass.plugin, "wrench_tool");
        recipe = new ShapedRecipe(keyWrench, wrench_tool);
        recipe.shape("B B", " A ", " A ");
        recipe.setIngredient('A', Material.GOLD_INGOT);
        recipe.setIngredient('B', Material.IRON_INGOT);
        registerRecipeIfAbsent(keyWrench, recipe);

		// Fuel Tool
        NamespacedKey keyFuelTool = new NamespacedKey(MainClass.plugin, "fuel_tool");
        recipe = new ShapedRecipe(keyFuelTool, fuel_tool);
        recipe.shape("ABA", "A A", " C ");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.BUCKET);
        recipe.setIngredient('C', Material.HOPPER);
        registerRecipeIfAbsent(keyFuelTool, recipe);

		// Chest Miner upgrade
        NamespacedKey keyChestMiner = new NamespacedKey(MainClass.plugin, "chest_miner");
        recipe = new ShapedRecipe(keyChestMiner, chest_miner);
        recipe.shape(" A ", " B ", " C ");
        recipe.setIngredient('A', Material.REDSTONE);
        recipe.setIngredient('B', Material.GOLDEN_HOE);
        recipe.setIngredient('C', Material.CHEST);
        registerRecipeIfAbsent(keyChestMiner, recipe);

		// Smelter upgrade
        NamespacedKey keySmelter = new NamespacedKey(MainClass.plugin, "smelter_upgrade");
        recipe = new ShapedRecipe(keySmelter, smelter_upgrade);
        recipe.shape("ABA", "CDA", "ABA");
        recipe.setIngredient('A', Material.IRON_INGOT);
        recipe.setIngredient('B', Material.HOPPER);
        recipe.setIngredient('C', Material.REDSTONE);
        recipe.setIngredient('D', Material.FURNACE);
        registerRecipeIfAbsent(keySmelter, recipe);

    }

    private void registerRecipeIfAbsent(NamespacedKey key, ShapedRecipe recipe) {
        try {
            if (Bukkit.getRecipe(key) == null) {
                Bukkit.getServer().addRecipe(recipe);
                registeredKeys.add(key);
            }
        } catch (Throwable t) {
            try {
                Bukkit.getServer().addRecipe(recipe);
                registeredKeys.add(key);
            } catch (Throwable ignored) {}
        }
    }

    public void removeRecipes() {
        for (NamespacedKey key : registeredKeys) {
            try { Bukkit.removeRecipe(key); } catch (Throwable ignored) {}
        }
        registeredKeys.clear();
    }

	public static boolean customItemsMatch(ItemStack first, ItemStack second) {
		if (first.getItemMeta().equals(second.getItemMeta())) {
			return true;
		}
		return false;
	}
}
