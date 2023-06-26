package me.cable.onlywithdraw.currency.provided;

import me.cable.onlycore.util.ExpUtils;
import me.cable.onlycore.util.FormatUtils;
import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.currency.ConfigCurrency;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;

public class ExperienceCurrency extends ConfigCurrency {

    public ExperienceCurrency(@NotNull OnlyWithdraw onlyWithdraw) {
        super("experience", onlyWithdraw);
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal value) {
        return FormatUtils.integer(value);
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Player player) {
        return new BigDecimal(ExpUtils.getPoints(player));
    }

    @Override
    public void increaseBalance(@NotNull Player player, @NotNull BigDecimal value) {
        ExpUtils.addPoints(player, value.intValue());
    }

    @Override
    public void decreaseBalance(@NotNull Player player, @NotNull BigDecimal value) {
        ExpUtils.removePoints(player, value.intValue());
    }
}
