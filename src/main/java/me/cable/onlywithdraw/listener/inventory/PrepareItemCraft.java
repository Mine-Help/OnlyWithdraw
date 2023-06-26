package me.cable.onlywithdraw.listener.inventory;

import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.handler.Settings;
import me.cable.onlywithdraw.handler.WithdrawItemHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PrepareItemCraft implements Listener {

    private final Settings settings;
    private final WithdrawItemHandler withdrawItemHandler;

    public PrepareItemCraft(@NotNull OnlyWithdraw onlyWithdraw) {
        settings = onlyWithdraw.getSettings();
        withdrawItemHandler = onlyWithdraw.getWithdrawItemHandler();
    }

    @EventHandler
    private void event(@NotNull PrepareItemCraftEvent e) {
        boolean cancelCrafting = settings.cancelCrafting();
        if (!cancelCrafting) return;

        for (ItemStack item : e.getInventory().getMatrix()) {
            if (item != null && withdrawItemHandler.isWithdrawItem(item)) {
                e.getInventory().setResult(null);
                break;
            }
        }
    }
}
