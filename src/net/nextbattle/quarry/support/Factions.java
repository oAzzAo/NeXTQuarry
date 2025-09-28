package net.nextbattle.quarry.support;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Factions {

	public boolean mayEditBlock(Block b, String playerName) {
		try {
			Player p = Bukkit.getPlayerExact(playerName);
			if (p == null) return true;
			// Resolve FPlayers singleton
			Class<?> fPlayersCls = Class.forName("com.massivecraft.factions.FPlayers");
			Object fPlayers;
			try {
				Method getInstance = fPlayersCls.getMethod("getInstance");
				fPlayers = getInstance.invoke(null);
			} catch (NoSuchMethodException nsme) {
				Field i = fPlayersCls.getField("i");
				fPlayers = i.get(null);
			}
			Method getFPlayer = fPlayers.getClass().getMethod("get", Player.class);
			Object fPlayer = getFPlayer.invoke(fPlayers, p);

			// Board and FLocation
			Class<?> boardCls = Class.forName("com.massivecraft.factions.Board");
			Class<?> fLocCls = Class.forName("com.massivecraft.factions.FLocation");
			Constructor<?> fLocCtor = fLocCls.getConstructor(org.bukkit.Location.class);
			Object fLoc = fLocCtor.newInstance(b.getLocation());
			Method getFactionAt = boardCls.getMethod("getFactionAt", fLocCls);
			Object factionAt = getFactionAt.invoke(null, fLoc);

			// Wilderness checks
			try {
				Method isWilderness = factionAt.getClass().getMethod("isWilderness");
				if ((boolean) isWilderness.invoke(factionAt)) return true;
			} catch (NoSuchMethodException ignored) {
				try {
					Method isNone = factionAt.getClass().getMethod("isNone");
					if ((boolean) isNone.invoke(factionAt)) return true;
				} catch (NoSuchMethodException ignored2) {
					try {
						Method getId = factionAt.getClass().getMethod("getId");
						Object id = getId.invoke(factionAt);
						if ("0".equals(String.valueOf(id))) return true;
					} catch (NoSuchMethodException ignored3) {}
				}
			}

			// Compare factions
			Method getFaction = fPlayer.getClass().getMethod("getFaction");
			Object playerFaction = getFaction.invoke(fPlayer);
			return factionAt != null && factionAt.equals(playerFaction);
		} catch (Throwable t) {
			return true; // Fail-open if integration API not matched
		}
	}
}
