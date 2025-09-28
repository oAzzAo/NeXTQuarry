package net.nextbattle.quarry.types;

import java.util.Objects;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class BlockLocation {
	private int x;
	private int y;
	private int z;
	private World world;

	public BlockLocation(int x, int y, int z, World world) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.world = world;
	}

	public BlockLocation(Block b) {
		this.x = b.getX();
		this.y = b.getY();
		this.z = b.getZ();
		this.world = b.getWorld();
	}

	public BlockLocation(Location loc) {
		this.x = (int) loc.getX();
		this.y = (int) loc.getY();
		this.z = (int) loc.getZ();
		this.world = loc.getWorld();
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public World getWorld() {
		return world;
	}

	public Location getLocation() {
		return new Location(world, x, y, z);
	}

	public Block getBlock() {
		return world.getBlockAt(getLocation());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BlockLocation)) {
			return false;
		}
		BlockLocation bl = (BlockLocation) obj;
		return x == bl.x && y == bl.y && z == bl.z
				&& Objects.equals(world == null ? null : world.getName(),
						bl.world == null ? null : bl.world.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(world == null ? null : world.getName(), x, y, z);
	}
}
