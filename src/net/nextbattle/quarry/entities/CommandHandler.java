package net.nextbattle.quarry.entities;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.nextbattle.quarry.main.MainClass;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("nextquarry")) {
            return true;
        }

        if (args.length >= 1 && args[0].equalsIgnoreCase("cleanup")) {
            handleCleanup(sender, args);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            handleReload(sender);
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("debug") && args[1].equalsIgnoreCase("visuals")) {
            handleToggleDebug(sender);
            return true;
        }

        if (args.length == 1 && args[0].equalsIgnoreCase("give")) {
            sender.sendMessage(Component.text(
                    "Items to choose from: tier1, tier2, tier3, wrench, speedupgrade, fuelinjector, fuelfinder, fuelupgrade, smelter, liquidminer & chestminer.",
                    NamedTextColor.GOLD));
            return true;
        }

        if (args.length == 2 && args[0].equalsIgnoreCase("give")) {
            handleGive(sender, args[1]);
            return true;
        }

        sendInfo(sender);
        return true;
    }

    private void handleReload(CommandSender sender) {
        if (!sender.hasPermission("nextquarry.admin")) {
            sender.sendMessage(Component.text("You do not have permission to reload NeXTQuarry.", NamedTextColor.RED));
            return;
        }
        try {
            MainClass.plugin.reloadConfig();
            Configuration.loadConfig();
            sender.sendMessage(Component.text("NeXTQuarry config reloaded.", NamedTextColor.GREEN));
        } catch (Throwable t) {
            sender.sendMessage(Component.text("Reload failed: " + t.getMessage(), NamedTextColor.RED));
        }
    }

    private void handleToggleDebug(CommandSender sender) {
        if (!sender.hasPermission("nextquarry.admin")) {
            sender.sendMessage(Component.text("You do not have permission to toggle debug.", NamedTextColor.RED));
            return;
        }
        try {
            boolean newVal = !MainClass.config.debug_visuals;
            MainClass.plugin.getConfig().set("debug.visuals", newVal);
            MainClass.plugin.saveConfig();
            Configuration.loadConfig();
            sender.sendMessage(Component.text("Debug visuals set to " + newVal, NamedTextColor.YELLOW));
        } catch (Throwable t) {
            sender.sendMessage(Component.text("Toggle failed: " + t.getMessage(), NamedTextColor.RED));
        }
    }

    private void handleGive(CommandSender sender, String itemKey) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("You can't execute this command from the console.", NamedTextColor.GOLD));
            return;
        }
        if (!player.hasPermission("nextquarry.admin")) {
            sender.sendMessage(Component.text("You are not allowed to execute this command with the give parameter!", NamedTextColor.GOLD));
            return;
        }

        switch (itemKey.toLowerCase()) {
            case "tier1" -> player.getInventory().addItem(MainClass.citems.quarry_tier1);
            case "tier2" -> player.getInventory().addItem(MainClass.citems.quarry_tier2);
            case "tier3" -> player.getInventory().addItem(MainClass.citems.quarry_tier3);
            case "wrench" -> player.getInventory().addItem(MainClass.citems.wrench_tool);
            case "speedupgrade" -> player.getInventory().addItem(MainClass.citems.speed_upgrade);
            case "fuelinjector" -> player.getInventory().addItem(MainClass.citems.fuel_tool);
            case "fuelfinder" -> player.getInventory().addItem(MainClass.citems.fuel_finder_upgrade);
            case "chestminer" -> player.getInventory().addItem(MainClass.citems.chest_miner);
            case "smelter" -> player.getInventory().addItem(MainClass.citems.smelter_upgrade);
            case "fuelupgrade" -> player.getInventory().addItem(MainClass.citems.fuel_efficiency_upgrade);
            case "liquidminer" -> player.getInventory().addItem(MainClass.citems.liquid_miner);
            default -> {
                player.sendMessage(Component.text("This item does not exist!", NamedTextColor.GOLD));
                player.sendMessage(Component.text(
                        "Items to choose from: tier1, tier2, tier3, wrench, speedupgrade, fuelinjector, fuelfinder, fuelupgrade, smelter, liquidminer & chestminer.",
                        NamedTextColor.GOLD));
            }
        }
    }

    private void sendInfo(CommandSender sender) {
        sender.sendMessage(Component.text("----[NeXTQuarry Plugin Info]----", NamedTextColor.GOLD));
        sender.sendMessage(Component.text("NeXTQuarry v2.0.0 - Coded by Furt & BeMacized", NamedTextColor.GOLD));
        if (sender instanceof Player player) {
            sender.sendMessage(Component.text("----[NeXTQuarry Permissions]----", NamedTextColor.GOLD));
            sendPermissionLine(sender, player.hasPermission("nextquarry.user.tier1"),
                    "You are allowed to place tier 1 quarries.",
                    "You are NOT allowed to place tier 1 quarries.");
            sendPermissionLine(sender, player.hasPermission("nextquarry.user.tier2"),
                    "You are allowed to place tier 2 quarries.",
                    "You are NOT allowed to place tier 2 quarries.");
            sendPermissionLine(sender, player.hasPermission("nextquarry.user.tier3"),
                    "You are allowed to place tier 3 quarries.",
                    "You are NOT allowed to place tier 3 quarries.");

            if (MainClass.config.privatequarries) {
                if (player.hasPermission("nextquarry.admin")) {
                    sender.sendMessage(Component.text("You are allowed to break all quarries.", NamedTextColor.GREEN));
                    sender.sendMessage(Component.text("You are allowed to edit all quarries.", NamedTextColor.GREEN));
                } else if (player.hasPermission("nextquarry.user.remove")) {
                    sender.sendMessage(Component.text("You are allowed to break YOUR quarries only.", NamedTextColor.GOLD));
                    sendPermissionLine(sender, player.hasPermission("nextquarry.user.edit"),
                            "You are allowed to edit YOUR quarries only.",
                            "You are NOT allowed to edit quarries.");
                } else {
                    sender.sendMessage(Component.text("You are NOT allowed to break quarries.", NamedTextColor.RED));
                    sendPermissionLine(sender, player.hasPermission("nextquarry.user.edit"),
                            "You are allowed to edit YOUR quarries only.",
                            "You are NOT allowed to edit quarries.");
                }
            } else {
                sender.sendMessage(Component.text("Private quarries are disabled, so you are allowed to break quarries you place.",
                        NamedTextColor.GREEN));
            }
        }
    }

    private void sendPermissionLine(CommandSender sender, boolean hasPermission, String allowed, String denied) {
        sender.sendMessage(Component.text(hasPermission ? allowed : denied,
                hasPermission ? NamedTextColor.GREEN : NamedTextColor.RED));
    }

    private void handleCleanup(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(Component.text("Cleanup must be run by a player (for coordinates).", NamedTextColor.RED));
            return;
        }
        if (!player.hasPermission("nextquarry.admin")) {
            sender.sendMessage(Component.text("You do not have permission to run cleanup.", NamedTextColor.RED));
            return;
        }
        double radius = 16.0;
        if (args.length >= 2) {
            try {
                radius = Math.max(1.0, Double.parseDouble(args[1]));
            } catch (NumberFormatException ignored) {
                sender.sendMessage(Component.text("Invalid radius, using default 16.", NamedTextColor.YELLOW));
            }
        }
        int chunks = MainClass.scheduleCleanup(player.getLocation(), radius);
        sender.sendMessage(Component.text("Scheduled cleanup across " + chunks + " chunk(s). Displays within "
                + radius + " blocks will be removed shortly.", NamedTextColor.GREEN));
    }
}
