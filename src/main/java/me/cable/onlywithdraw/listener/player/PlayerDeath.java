package me.cable.onlywithdraw.listener.player;

import me.cable.onlycore.util.ExpUtils;
import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.handler.CurrencyManager;
import me.cable.onlywithdraw.currency.Currency;
import me.cable.onlywithdraw.handler.Settings;
import me.cable.onlywithdraw.handler.WithdrawItemHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class PlayerDeath implements Listener {

    private final Settings settings;
    private final WithdrawItemHandler withdrawItemHandler;

    public PlayerDeath(@NotNull OnlyWithdraw onlyWithdraw) {
        settings = onlyWithdraw.getSettings();
        withdrawItemHandler = onlyWithdraw.getWithdrawItemHandler();
    }

    @EventHandler(ignoreCancelled = true)
    private void event(@NotNull PlayerDeathEvent playerDeathEvent) {
        if (playerDeathEvent.getKeepInventory()) {
            return;
        }

        boolean experienceOnDeath = settings.experienceOnDeathEnabled();
        String experienceCurrencyId = settings.experienceOnDeathCurrencyId();
        boolean fullAmount = settings.experienceOnDeathFullAmount();
        BigDecimal experienceOnDeathMin = settings.experienceOnDeathMin();

        if (!experienceOnDeath || experienceCurrencyId == null) {
            return;
        }

        Player player = playerDeathEvent.getEntity();
        int dropExp = fullAmount ? ExpUtils.getPoints(player) : playerDeathEvent.getDroppedExp();
        BigDecimal value = new BigDecimal(dropExp);

        if (value.compareTo(experienceOnDeathMin) < 0) {
            return;
        }

        Currency currency = CurrencyManager.getCurrency(experienceCurrencyId);
        if (currency == null) return;

        ItemStack drop = withdrawItemHandler.create(currency, value, 1, player.getName());

        playerDeathEvent.getDrops().add(drop);
        playerDeathEvent.setDroppedExp(0);
    }
}
