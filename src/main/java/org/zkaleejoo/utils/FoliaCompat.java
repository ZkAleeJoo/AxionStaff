package org.zkaleejoo.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.concurrent.TimeUnit;

/**
 * Compatibility layer for Folia and Paper schedulers.
 * Detects the server type at startup and delegates to the appropriate scheduler
 * API.
 */
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

    /** Returns {@code true} if the server is running Folia. */
    public static boolean isFolia() {
        return IS_FOLIA;
    }

    /** A cancellable task handle that works on both Paper and Folia. */
    @FunctionalInterface
    public interface WrappedTask {
        void cancel();
    }

    // ── Global-region tasks (not tied to any specific entity or location) ──

    /** Runs a task on the global region thread (main thread on Paper). */
    public static void runGlobal(Plugin plugin, Runnable runnable) {
        if (IS_FOLIA) {
            Bukkit.getGlobalRegionScheduler().run(plugin, task -> runnable.run());
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    /** Runs a task on the global region thread after a delay. */
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

    /** Runs a repeating task on the global region thread. */
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

    // ── Async tasks (I/O, database, network – never touch game state) ──

    /** Runs a task asynchronously (off any game thread). */
    public static void runAsync(Plugin plugin, Runnable runnable) {
        if (IS_FOLIA) {
            Bukkit.getAsyncScheduler().runNow(plugin, task -> runnable.run());
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, runnable);
        }
    }

    /** Runs a repeating task asynchronously. Delay and period are in ticks. */
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

    // ── Entity-scoped tasks (run on the entity's owning region thread) ──

    /** Runs a task on the region thread that owns the given entity. */
    public static void runForEntity(Plugin plugin, Entity entity, Runnable runnable) {
        if (IS_FOLIA) {
            entity.getScheduler().run(plugin, task -> runnable.run(), null);
        } else {
            Bukkit.getScheduler().runTask(plugin, runnable);
        }
    }

    /** Runs a delayed task on the region thread that owns the given entity. */
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

    /**
     * Runs a repeating task on the region thread that owns the given entity.
     */
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

    // ── Teleport ──

    /**
     * Teleports an entity. Uses {@code teleportAsync} on Folia, sync teleport on
     * Paper.
     */
    public static void teleport(Entity entity, Location location) {
        if (IS_FOLIA) {
            entity.teleportAsync(location);
        } else {
            entity.teleport(location);
        }
    }
}
