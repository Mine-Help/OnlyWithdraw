package me.cable.onlywithdraw.currency;

import me.cable.onlywithdraw.api.CurrencyManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public abstract class Currency {

    private final String id;

    public Currency(@NotNull String id) {
        this.id = id;
    }

    public final void register() {
        CurrencyManager.registerCurrency(this);
    }

    public boolean isEnabled() {
        return true;
    }

    public abstract @NotNull String format(@NotNull BigDecimal value);

    public abstract @NotNull String getCommandLabel();

    public abstract @Nullable String getPermission();

    public abstract @Nullable String getAdminPermission();

    public abstract @Nullable BigDecimal getMin();

    public abstract @Nullable BigDecimal getMax();

    public abstract @NotNull BigDecimal getBalance(@NotNull Player player);

    public abstract void increaseBalance(@NotNull Player player, @NotNull BigDecimal value);

    public abstract void decreaseBalance(@NotNull Player player, @NotNull BigDecimal value);

    public abstract void onRedeem(@NotNull Player player, @NotNull BigDecimal value, int amount);

    public abstract void onWithdraw(@NotNull Player player, @NotNull BigDecimal value, int amount);

    public abstract @NotNull ItemStack createItem(@NotNull BigDecimal value, @Nullable String owner);

    public final @NotNull String getId() {
        return id;
    }
}
