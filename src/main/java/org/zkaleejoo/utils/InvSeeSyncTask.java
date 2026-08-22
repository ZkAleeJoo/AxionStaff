package org.zkaleejoo.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.zkaleejoo.config.MainConfigManager;

public final class InvSeeSyncTask {

    private static final int STORAGE_SIZE = 36;
    private static final int SYNC_INTERVAL_TICKS = 4;

    private final Plugin plugin;
    private final Player staff;
    private final Player target;
    private final Inventory gui;
    private final MainConfigManager config;

    private final int armorStartSlot;
    private final int offhandSlot;
    private final int mainhandSlot;

    private final ItemStack[] lastSyncedStorage = new ItemStack[STORAGE_SIZE];
    private final ItemStack[] lastSyncedArmor = new ItemStack[4];
    private ItemStack lastSyncedOffhand;

    private FoliaCompat.WrappedTask task;
    private volatile boolean cancelled;

    public InvSeeSyncTask(Plugin plugin, Player staff, Player target,
            Inventory gui, MainConfigManager config) {
        this.plugin = plugin;
        this.staff = staff;
        this.target = target;
        this.gui = gui;
        this.config = config;
        this.armorStartSlot = InspectionInventoryBuilder.getArmorStartSlot(config, gui);
        this.offhandSlot = InspectionInventoryBuilder.getOffhandSlot(config, gui);
        this.mainhandSlot = InspectionInventoryBuilder.getMainhandSlot(config, gui);

        captureInitialState();
    }

    public void start() {
        this.task = FoliaCompat.runForEntityTimer(
                plugin, target, this::sync,
                SYNC_INTERVAL_TICKS, SYNC_INTERVAL_TICKS);
    }

    public void stop() {
        cancelled = true;
        if (task != null) {
            task.cancel();
            task = null;
        }
    }

    public void finalSync() {
        if (target.isOnline()) {
            sync();
        }
    }

    private void sync() {
        if (cancelled || !target.isOnline() || !staff.isOnline()) {
            stop();
            return;
        }

        PlayerInventory playerInv = target.getInventory();
        boolean playerDirty = false;

        ItemStack[] currentStorage = playerInv.getStorageContents();
        for (int i = 0; i < STORAGE_SIZE; i++) {
            ItemStack guiItem = resolveItem(gui.getItem(i));
            ItemStack playerItem = normalise(safeGet(currentStorage, i));
            ItemStack lastItem = lastSyncedStorage[i];

            boolean guiChanged = !itemsEqual(guiItem, lastItem);
            boolean playerChanged = !itemsEqual(playerItem, lastItem);

            if (guiChanged) {
                playerInv.setItem(i, safeClone(guiItem));
                lastSyncedStorage[i] = safeClone(guiItem);
                playerDirty = true;
            } else if (playerChanged) {
                gui.setItem(i, safeClone(playerItem));
                lastSyncedStorage[i] = safeClone(playerItem);
            }
        }

        ItemStack[] currentArmor = playerInv.getArmorContents();
        boolean armorDirty = false;
        for (int i = 0; i < 4; i++) {
            int guiSlot = armorStartSlot + i;
            ItemStack guiItem = resolveItem(gui.getItem(guiSlot));
            ItemStack playerItem = normalise(safeGet(currentArmor, i));
            ItemStack lastItem = lastSyncedArmor[i];

            boolean guiChanged = !itemsEqual(guiItem, lastItem);
            boolean playerChanged = !itemsEqual(playerItem, lastItem);

            if (guiChanged) {
                currentArmor[i] = safeClone(guiItem);
                setGuiSlotOrPlaceholder(guiSlot, guiItem, armorSlotLabel(i));
                lastSyncedArmor[i] = safeClone(guiItem);
                armorDirty = true;
            } else if (playerChanged) {
                setGuiSlotOrPlaceholder(guiSlot, playerItem, armorSlotLabel(i));
                lastSyncedArmor[i] = safeClone(playerItem);
            }
        }
        if (armorDirty) {
            playerInv.setArmorContents(currentArmor);
            playerDirty = true;
        }

        {
            ItemStack guiItem = resolveItem(gui.getItem(offhandSlot));
            ItemStack playerItem = normalise(playerInv.getItemInOffHand());
            ItemStack lastItem = lastSyncedOffhand;

            boolean guiChanged = !itemsEqual(guiItem, lastItem);
            boolean playerChanged = !itemsEqual(playerItem, lastItem);

            if (guiChanged) {
                playerInv.setItemInOffHand(safeClone(guiItem));
                setGuiSlotOrPlaceholder(offhandSlot, guiItem, config.getInspectOffhandLabel());
                lastSyncedOffhand = safeClone(guiItem);
                playerDirty = true;
            } else if (playerChanged) {
                setGuiSlotOrPlaceholder(offhandSlot, playerItem, config.getInspectOffhandLabel());
                lastSyncedOffhand = safeClone(playerItem);
            }
        }

        ItemStack currentMainHand = normalise(playerInv.getItemInMainHand());
        if (isEmpty(currentMainHand)) {
            gui.setItem(mainhandSlot,
                    InspectionInventoryBuilder.createPlaceholder(
                            config.getInspectMainhandLabel(), config));
        } else {
            gui.setItem(mainhandSlot, currentMainHand.clone());
        }

        if (playerDirty) {
            target.updateInventory();
        }
    }

    private void captureInitialState() {
        PlayerInventory inv = target.getInventory();

        ItemStack[] storage = inv.getStorageContents();
        for (int i = 0; i < STORAGE_SIZE; i++) {
            lastSyncedStorage[i] = safeClone(normalise(safeGet(storage, i)));
        }

        ItemStack[] armor = inv.getArmorContents();
        for (int i = 0; i < 4; i++) {
            lastSyncedArmor[i] = safeClone(normalise(safeGet(armor, i)));
        }

        lastSyncedOffhand = safeClone(normalise(inv.getItemInOffHand()));
    }

    private void setGuiSlotOrPlaceholder(int slot, ItemStack item, String label) {
        if (isEmpty(item)) {
            gui.setItem(slot,
                    InspectionInventoryBuilder.createPlaceholder(label, config));
        } else {
            gui.setItem(slot, item.clone());
        }
    }

    private String armorSlotLabel(int index) {
        return switch (index) {
            case 0 -> config.getInspectArmorBootsLabel();
            case 1 -> config.getInspectArmorLeggingsLabel();
            case 2 -> config.getInspectArmorChestplateLabel();
            case 3 -> config.getInspectArmorHelmetLabel();
            default -> "&bArmor";
        };
    }

    private static ItemStack resolveItem(ItemStack item) {
        if (isEmpty(item))
            return null;
        if (InspectionInventoryBuilder.isInspectionPlaceholder(item))
            return null;
        return item;
    }

    private static ItemStack normalise(ItemStack item) {
        return isEmpty(item) ? null : item;
    }

    private static boolean itemsEqual(ItemStack a, ItemStack b) {
        if (a == null && b == null)
            return true;
        if (a == null || b == null)
            return false;
        return a.getAmount() == b.getAmount() && a.isSimilar(b);
    }

    private static ItemStack safeClone(ItemStack item) {
        return isEmpty(item) ? null : item.clone();
    }

    private static ItemStack safeGet(ItemStack[] array, int index) {
        if (index < 0 || index >= array.length)
            return null;
        return array[index];
    }

    private static boolean isEmpty(ItemStack item) {
        return item == null || item.getType() == Material.AIR;
    }
}
