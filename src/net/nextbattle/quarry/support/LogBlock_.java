package net.nextbattle.quarry.support;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;

public class LogBlock_ {

    private final Object consumer;
    private final Method queueBreak;
    private final Method queuePlace;

    public LogBlock_() {
        Object resolvedConsumer = null;
        Method breakMethod = null;
        Method placeMethod = null;
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("LogBlock");
            if (plugin != null) {
                Method getConsumer = plugin.getClass().getMethod("getConsumer");
                Object consumerInstance = getConsumer.invoke(plugin);
                if (consumerInstance != null) {
                    breakMethod = findLoggingMethod(consumerInstance.getClass(), "queueBlockBreak");
                    placeMethod = findLoggingMethod(consumerInstance.getClass(), "queueBlockPlace");
                    if (breakMethod != null && placeMethod != null) {
                        resolvedConsumer = consumerInstance;
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        this.consumer = resolvedConsumer;
        this.queueBreak = breakMethod;
        this.queuePlace = placeMethod;
    }

    private Method findLoggingMethod(Class<?> clazz, String name) {
        try {
            return clazz.getMethod(name, String.class, Location.class, Material.class, BlockData.class);
        } catch (NoSuchMethodException e) {
            try {
                return clazz.getMethod(name, String.class, Location.class, Material.class, byte.class);
            } catch (NoSuchMethodException ignored) {
            }
        }
        return null;
    }

    public boolean isReady() {
        return consumer != null && queueBreak != null && queuePlace != null;
    }

    public void logRemoval(String username, Location loc, Material material, BlockData data) {
        if (!isReady()) {
            return;
        }
        invoke(queueBreak, username, loc, material, data);
    }

    public void logPlacement(String username, Location loc, Material material, BlockData data) {
        if (!isReady()) {
            return;
        }
        invoke(queuePlace, username, loc, material, data);
    }

    private void invoke(Method method, String username, Location loc, Material material, BlockData data) {
        try {
            Class<?>[] params = method.getParameterTypes();
            Object payload = data;
            if (params[3] == byte.class) {
                payload = (byte) 0;
            } else if (payload == null && material != null) {
                payload = material.createBlockData();
            }
            method.invoke(consumer, username, loc, material, payload);
        } catch (Throwable ignored) {
        }
    }
}
