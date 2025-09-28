package net.nextbattle.quarry.support;

import java.lang.reflect.Method;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.plugin.Plugin;

public class CoreProtect {

    private final Object api;
    private final Method logRemoval;
    private final Method logPlacement;

    public CoreProtect() {
        Object resolvedApi = null;
        Method removal = null;
        Method placement = null;
        try {
            Plugin plugin = Bukkit.getPluginManager().getPlugin("CoreProtect");
            if (plugin != null) {
                Method getApi = plugin.getClass().getMethod("getAPI");
                Object apiInstance = getApi.invoke(plugin);
                if (apiInstance != null) {
                    Method isEnabled = apiInstance.getClass().getMethod("isEnabled");
                    Object enabled = isEnabled.invoke(apiInstance);
                    if (!(enabled instanceof Boolean) || Boolean.TRUE.equals(enabled)) {
                        removal = findLoggingMethod(apiInstance.getClass(), "logRemoval");
                        placement = findLoggingMethod(apiInstance.getClass(), "logPlacement");
                        if (removal != null && placement != null) {
                            resolvedApi = apiInstance;
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        this.api = resolvedApi;
        this.logRemoval = removal;
        this.logPlacement = placement;
    }

    private Method findLoggingMethod(Class<?> clazz, String name) {
        try {
            return clazz.getMethod(name, String.class, Location.class, Material.class, BlockData.class);
        } catch (NoSuchMethodException e) {
            try {
                Class<?> materialData = Class.forName("org.bukkit.material.MaterialData");
                return clazz.getMethod(name, String.class, Location.class, Material.class, materialData);
            } catch (Throwable ignored) {
            }
        }
        return null;
    }

    public boolean isReady() {
        return api != null && logRemoval != null && logPlacement != null;
    }

    public void logRemoval(String username, Location loc, Material material, BlockData data) {
        if (!isReady()) {
            return;
        }
        invoke(logRemoval, username, loc, material, data);
    }

    public void logPlacement(String username, Location loc, Material material, BlockData data) {
        if (!isReady()) {
            return;
        }
        invoke(logPlacement, username, loc, material, data);
    }

    private void invoke(Method method, String username, Location loc, Material material, BlockData data) {
        try {
            Class<?>[] params = method.getParameterTypes();
            Object payload = data;
            if (params[3].getName().equals("org.bukkit.material.MaterialData")) {
                Class<?> materialData = params[3];
                try {
                    payload = materialData.getConstructor(Material.class).newInstance(material);
                } catch (NoSuchMethodException ex) {
                    payload = materialData.getConstructor(Material.class, byte.class)
                            .newInstance(material, (byte) 0);
                }
            } else if (payload == null && material != null) {
                payload = material.createBlockData();
            }
            method.invoke(api, username, loc, material, payload);
        } catch (Throwable ignored) {
        }
    }
}
