package me.cable.onlywithdraw.currency.provided;

import me.cable.onlycore.util.FormatUtils;
import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.currency.ConfigCurrency;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class MoneyCurrency extends ConfigCurrency {

    public MoneyCurrency(@NotNull OnlyWithdraw onlyWithdraw) {
        super("money", onlyWithdraw);
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal value) {
        return FormatUtils.decimal(value);
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Player player) {
        // TODO
        return BigDecimal.ZERO;
    }

    @Override
    public void increaseBalance(@NotNull Player player, @NotNull BigDecimal value) {
        // TODO
    }

    @Override
    public void decreaseBalance(@NotNull Player player, @NotNull BigDecimal value) {
        // TODO
    }
}
