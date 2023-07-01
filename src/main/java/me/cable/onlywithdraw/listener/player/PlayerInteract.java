package me.cable.onlywithdraw.listener.player;

import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.handler.CurrencyManager;
import me.cable.onlywithdraw.currency.Currency;
import me.cable.onlywithdraw.handler.WithdrawItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class PlayerInteract implements Listener {

    private final WithdrawItemHandler withdrawItemHandler;

    public PlayerInteract(@NotNull OnlyWithdraw onlyWithdraw) {
        withdrawItemHandler = onlyWithdraw.getWithdrawItemHandler();
    }

    @EventHandler
    private void event(@NotNull PlayerInteractEvent e) {
        ItemStack item = e.getItem();
        if (item == null) return;

        if (!e.getAction().toString().contains("RIGHT")) {
            return;
        }

        String currencyId = withdrawItemHandler.getCurrencyId(item);
        if (currencyId == null) return;

        Currency currency = CurrencyManager.getCurrency(currencyId);
        if (currency == null) return;

        BigDecimal value = withdrawItemHandler.getValue(item);
        if (value == null) return;

        e.setCancelled(true);

        Player player = e.getPlayer();
        int amount;
        BigDecimal totalValue;

        if (player.isSneaking()) {
            amount = item.getAmount();
            totalValue = value.multiply(new BigDecimal(amount));
            item.setAmount(0);
        } else {
            amount = 1;
            totalValue = value;
            item.setAmount(item.getAmount() - 1);
        }

        currency.increaseBalance(player, totalValue);
        currency.onRedeem(player, value, amount);
    }
}
