package me.cable.onlywithdraw.currency.provided;

import me.cable.onlycore.util.FormatUtils;
import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.currency.ConfigCurrency;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public class MoneyCurrency extends ConfigCurrency {

    private final @Nullable Economy economy;

    public MoneyCurrency(@NotNull OnlyWithdraw onlyWithdraw) {
        super("money", onlyWithdraw);
        economy = onlyWithdraw.getEconomy();
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled() && (economy != null);
    }

    @Override
    public @NotNull String format(@NotNull BigDecimal value) {
        return '$' + FormatUtils.decimal(value);
    }

    @Override
    public @NotNull BigDecimal getBalance(@NotNull Player player) {
        return (economy == null) ? BigDecimal.ZERO : BigDecimal.valueOf(economy.getBalance(player));
    }

    @Override
    public void increaseBalance(@NotNull Player player, @NotNull BigDecimal value) {
        if (economy != null) {
            economy.depositPlayer(player, value.doubleValue());
        }
    }

    @Override
    public void decreaseBalance(@NotNull Player player, @NotNull BigDecimal value) {
        if (economy != null) {
            economy.withdrawPlayer(player, value.doubleValue());
        }
    }
}
