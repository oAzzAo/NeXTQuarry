package net.nextbattle.quarry.visual;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import net.nextbattle.quarry.main.MainClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.Chunk;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.BlockDisplay;
import org.bukkit.entity.Entity;
import org.bukkit.util.BoundingBox;

/**
 * Region-safe display manager for quarry arm visuals.
 */
public class ArmAnimator {

    public record VisualBlock(Location location, BlockData blockData) {}

    private static final String ARM_TAG = "nextquarry-arm";

    private final Map<BlockKey, UUID> activeDisplays = new HashMap<>();

    public ArmAnimator() {}

    public void update(List<VisualBlock> blocks) {
        if (blocks == null || blocks.isEmpty()) {
            clear();
            return;
        }
        Map<BlockKey, VisualBlock> targets = new HashMap<>();
        for (VisualBlock block : blocks) {
            if (block == null) {
                continue;
            }
            Location loc = block.location();
            if (loc == null || loc.getWorld() == null) {
                continue;
            }
            BlockKey key = BlockKey.from(loc);
            if (key == null) {
                continue;
            }
            targets.put(key, block);
            scheduleSpawnOrMove(key, block);
        }
        Set<BlockKey> toRemove;
        synchronized (this) {
            toRemove = new HashSet<>(activeDisplays.keySet());
        }
        toRemove.removeAll(targets.keySet());
        for (BlockKey key : toRemove) {
            scheduleRemoval(key);
        }
    }

    public void clear() {
        Set<BlockKey> keys;
        synchronized (this) {
            keys = new HashSet<>(activeDisplays.keySet());
        }
        for (BlockKey key : keys) {
            scheduleRemoval(key);
        }
    }

    private void scheduleSpawnOrMove(BlockKey key, VisualBlock visual) {
        Location loc = visual.location();
        BlockData data = visual.blockData();
        if (loc == null || loc.getWorld() == null || data == null) {
            return;
        }
        Location executionLoc = loc.clone();
        MainClass.plugin.getServer().getRegionScheduler().execute(MainClass.plugin, executionLoc, () -> {
            UUID existingId;
            synchronized (ArmAnimator.this) {
                existingId = activeDisplays.get(key);
            }
            BlockDisplay display = null;
            if (existingId != null) {
                Entity entity = Bukkit.getEntity(existingId);
                if (entity instanceof BlockDisplay bd) {
                    display = bd;
                } else {
                    synchronized (ArmAnimator.this) {
                        activeDisplays.remove(key);
                    }
                }
            }
            if (display == null) {
                World world = executionLoc.getWorld();
                if (world == null) {
                    return;
                }
                try {
                    BlockDisplay spawned = world.spawn(executionLoc.clone().add(0.5, 0.0, 0.5), BlockDisplay.class);
                    spawned.setBlock(data.clone());
                    spawned.addScoreboardTag(ARM_TAG);
                    synchronized (ArmAnimator.this) {
                        activeDisplays.put(key, spawned.getUniqueId());
                    }
                } catch (Throwable ignored) {
                }
            } else {
                display.setBlock(data.clone());
                display.teleportAsync(executionLoc.clone().add(0.5, 0.0, 0.5));
            }
        });
    }

    private void scheduleRemoval(BlockKey key) {
        Location loc = key.toLocation();
        if (loc == null) {
            synchronized (this) {
                activeDisplays.remove(key);
            }
            return;
        }
        MainClass.plugin.getServer().getRegionScheduler().execute(MainClass.plugin, loc, () -> {
            UUID id;
            synchronized (ArmAnimator.this) {
                id = activeDisplays.remove(key);
            }
            if (id != null) {
                Entity entity = Bukkit.getEntity(id);
                if (entity != null) {
                    entity.remove();
                }
            }
        });
    }

    public static void removeOrphanDisplays(World world) {
        if (world == null || MainClass.plugin == null) {
            return;
        }
        Chunk[] chunks = world.getLoadedChunks();
        if (chunks.length == 0) {
            return;
        }
        for (Chunk chunk : chunks) {
            scheduleChunkSweep(world, chunk.getX(), chunk.getZ(), null, 0.0D, true);
        }
    }

    public static void scheduleChunkSweep(World world, int chunkX, int chunkZ, Location center, double radius, boolean taggedOnly) {
        scheduleChunkSweep(world, chunkX, chunkZ, center, radius, taggedOnly, 1L);
    }

    public static void scheduleChunkSweep(World world, int chunkX, int chunkZ, Location center, double radius, boolean taggedOnly, long delayTicks) {
        if (world == null || MainClass.plugin == null) {
            return;
        }
        long delay = Math.max(1L, delayTicks);
        Location targetCenter = center != null ? center.clone() : null;
        MainClass.plugin.getServer().getRegionScheduler().runDelayed(MainClass.plugin, world, chunkX, chunkZ,
                task -> sweepChunk(world, chunkX, chunkZ, targetCenter, radius, taggedOnly), delay);
    }

    private static void sweepChunk(World world, int chunkX, int chunkZ, Location center, double radius, boolean taggedOnly) {
        BoundingBox searchBox = createChunkBoundingBox(world, chunkX, chunkZ);
        double radiusSquared = radius > 0.0D ? radius * radius : -1.0D;
        for (Entity entity : world.getNearbyEntities(searchBox, candidate -> candidate instanceof BlockDisplay)) {
            BlockDisplay display = (BlockDisplay) entity;
            if (taggedOnly && !display.getScoreboardTags().contains(ARM_TAG)) {
                continue;
            }
            if (radiusSquared >= 0.0D) {
                if (center == null || !display.getWorld().equals(center.getWorld())) {
                    continue;
                }
                Location displayLocation = display.getLocation();
                if (displayLocation.distanceSquared(center) > radiusSquared) {
                    continue;
                }
            }
            display.remove();
        }
    }

    private static BoundingBox createChunkBoundingBox(World world, int chunkX, int chunkZ) {
        int minX = chunkX << 4;
        int minZ = chunkZ << 4;
        int minY = world.getMinHeight();
        int maxY = world.getMaxHeight();
        return new BoundingBox(minX, minY, minZ, minX + 16, maxY, minZ + 16);
    }

    private static final class BlockKey {
        private final String worldName;
        private final int x;
        private final int y;
        private final int z;

        private BlockKey(String worldName, int x, int y, int z) {
            this.worldName = worldName;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        static BlockKey from(Location location) {
            World world = location.getWorld();
            if (world == null) {
                return null;
            }
            return new BlockKey(world.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
        }

        Location toLocation() {
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                return null;
            }
            return new Location(world, x, y, z);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof BlockKey)) return false;
            BlockKey blockKey = (BlockKey) o;
            return x == blockKey.x && y == blockKey.y && z == blockKey.z && Objects.equals(worldName, blockKey.worldName);
        }

        @Override
        public int hashCode() {
            return Objects.hash(worldName, x, y, z);
        }
    }
}
