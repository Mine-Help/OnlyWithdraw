package me.cable.onlywithdraw.api;

import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.currency.Currency;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public final class WithdrawAPI {

    private static final OnlyWithdraw onlyWithdraw = JavaPlugin.getPlugin(OnlyWithdraw.class);

    public static @NotNull ItemStack createWithdrawItem(
            @NotNull Currency currency,
            @NotNull BigDecimal value,
            int amount,
            @Nullable String owner
    ) {
        return onlyWithdraw.getWithdrawItemHandler().create(currency, value, amount, owner);
    }

    public static @Nullable ItemStack createWithdrawItem(
            @NotNull String currencyId,
            @NotNull BigDecimal value,
            int amount,
            @Nullable String owner
    ) {
        Currency currency = CurrencyManager.getCurrency(currencyId);
        return (currency == null) ? null : createWithdrawItem(currency, value, amount, owner);
    }
}
