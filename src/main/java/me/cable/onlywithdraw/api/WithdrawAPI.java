package me.cable.onlywithdraw.api;

import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.currency.Currency;
import me.cable.onlywithdraw.handler.CurrencyManager;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public final class WithdrawAPI {

    private static final OnlyWithdraw onlyWithdraw = OnlyWithdraw.getInstance();

    public static @NotNull ItemStack createWithdrawItem(
            @NotNull String currencyId,
            @NotNull BigDecimal value,
            @Nullable String owner
    ) throws IllegalArgumentException {
        Currency currency = CurrencyManager.getCurrency(currencyId);
        if (currency == null) throw new IllegalArgumentException("Invalid currency: " + currencyId);
        return onlyWithdraw.getWithdrawItemHandler().create(currency, value, 1, owner);
    }
}
