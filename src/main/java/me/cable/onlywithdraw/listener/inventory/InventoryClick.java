package me.cable.onlywithdraw.listener.inventory;

import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.handler.Settings;
import me.cable.onlywithdraw.handler.WithdrawItemHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class InventoryClick implements Listener {

    private final Settings settings;
    private final WithdrawItemHandler withdrawItemHandler;

    public InventoryClick(@NotNull OnlyWithdraw onlyWithdraw) {
        settings = onlyWithdraw.getSettings();
        withdrawItemHandler = onlyWithdraw.getWithdrawItemHandler();
    }

    @EventHandler
    private void event(@NotNull InventoryClickEvent e) {
        boolean cancelVillagerTrade = settings.cancelVillagerTrading();
        if (!cancelVillagerTrade) return;

        Inventory topInventory = e.getView().getTopInventory();
        if (topInventory.getType() != InventoryType.MERCHANT) return;

        ItemStack result = topInventory.getItem(2);
        if (result == null || result.getType().isAir()) return;

        if (e.getRawSlot() != 2) return;

        // attempt to trade

        for (int i = 0; i <= 1; i++) {
            ItemStack item = e.getView().getItem(i);

            if (item != null && withdrawItemHandler.isWithdrawItem(item)) {
                e.setCancelled(true);
                break;
            }
        }
    }
}
