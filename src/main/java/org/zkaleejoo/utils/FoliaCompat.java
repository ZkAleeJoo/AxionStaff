package org.zkaleejoo.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

public final class FoliaCompat {

    private static final boolean IS_FOLIA;

    static {
        boolean folia;
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            folia = true;
        } catch (ClassNotFoundException e) {
            folia = false;
        }
        IS_FOLIA = folia;
    }

    private FoliaCompat() {
    }

    public static boolean isFolia() {
        return IS_FOLIA;
    }

    @FunctionalInterface
    public interface WrappedTask {
        void cancel();
    }

    public static void runGlobal(Plugin plugin, Runnable runnable) {
        if (IS_FOLIA) {
            Bukkit.getGlobalRegionScheduler().run(plugin, task -> runnable.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static void runGlobalLater(Plugin plugin, Runnable runnable, long delayTicks) {
        if (IS_FOLIA) {
            if (delayTicks <= 0) {
                Bukkit.getGlobalRegionScheduler().run(plugin, task -> runnable.run());
            } else {
                Bukkit.getGlobalRegionScheduler().runDelayed(plugin, task -> runnable.run(), delayTicks);
            }
        } else {
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks);
        }
    }

    public static WrappedTask runGlobalTimer(Plugin plugin, Runnable runnable, long delayTicks, long periodTicks) {
        if (IS_FOLIA) {
            var task = Bukkit.getGlobalRegionScheduler().runAtFixedRate(
                    plugin, t -> runnable.run(), Math.max(1L, delayTicks), periodTicks);
            return task::cancel;
        } else {
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, periodTicks);
            return task::cancel;
        }
    }

    public static void runAsync(Plugin plugin, Runnable runnable) {
        if (IS_FOLIA) {
            Bukkit.getAsyncScheduler().runNow(plugin, task -> runnable.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }

    public static WrappedTask runAsyncTimer(Plugin plugin, Runnable runnable, long delayTicks, long periodTicks) {
        if (IS_FOLIA) {
            long initialDelayMs = Math.max(50L, delayTicks * 50L);
            long periodMs = Math.max(50L, periodTicks * 50L);
            var task = Bukkit.getAsyncScheduler().runAtFixedRate(
                    plugin, t -> runnable.run(), initialDelayMs, periodMs, TimeUnit.MILLISECONDS);
            return task::cancel;
        } else {
            BukkitTask task = Bukkit.getScheduler().runTaskTimerAsynchronously(
                    plugin, runnable, delayTicks, periodTicks);
            return task::cancel;
        }
    }

    public static void runForEntity(Plugin plugin, Entity entity, Runnable runnable) {
        if (IS_FOLIA) {
            entity.getScheduler().run(plugin, task -> runnable.run(), null);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    public static WrappedTask runForEntityLater(Plugin plugin, Entity entity, Runnable runnable, long delayTicks) {
        if (IS_FOLIA) {
            if (delayTicks <= 0) {
                entity.getScheduler().run(plugin, task -> runnable.run(), null);
                return () -> {
                };
            }
            var task = entity.getScheduler().runDelayed(plugin, t -> runnable.run(), null, delayTicks);
            return task == null ? () -> {
            } : task::cancel;
        } else {
            BukkitTask task = Bukkit.getScheduler().runTaskLater(plugin, runnable, delayTicks);
            return task::cancel;
        }
    }

    public static WrappedTask runForEntityTimer(Plugin plugin, Entity entity, Runnable runnable,
            long delayTicks, long periodTicks) {
        if (IS_FOLIA) {
            var task = entity.getScheduler().runAtFixedRate(
                    plugin, t -> runnable.run(), null, Math.max(1L, delayTicks), periodTicks);
            return task == null ? () -> {
            } : task::cancel;
        } else {
            BukkitTask task = Bukkit.getScheduler().runTaskTimer(plugin, runnable, delayTicks, periodTicks);
            return task::cancel;
        }
    }

    public static void teleport(Entity entity, Location location) {
        if (IS_FOLIA) {
            entity.teleportAsync(location);
        } else {
            entity.teleport(location);
        }
    }
}
