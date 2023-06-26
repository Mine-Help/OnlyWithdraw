package me.cable.onlywithdraw.api;

import me.cable.onlycore.util.CUtils;
import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.command.WithdrawCommand;
import me.cable.onlywithdraw.currency.Currency;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CurrencyManager {

    private static final OnlyWithdraw onlyWithdraw = JavaPlugin.getPlugin(OnlyWithdraw.class);

    private static final Set<Currency> enabledCurrencies = new HashSet<>();
    private static final Set<Currency> disabledCurrencies = new HashSet<>();
    private static final Set<WithdrawCommand> withdrawCommands = new HashSet<>();

    public static @Nullable Currency getCurrency(@NotNull String id) {
        for (Currency currency : enabledCurrencies) {
            if (currency.getId().equals(id)) {
                return currency;
            }
        }

        return null;
    }

    public static void registerCurrency(@NotNull Currency currency) {
        if (enabledCurrencies.contains(currency) || disabledCurrencies.contains(currency)) {
            throw new IllegalArgumentException(Currency.class.getSimpleName() + " has already been registered");
        }

        if (currency.isEnabled()) {
            enabledCurrencies.add(currency);

            WithdrawCommand withdrawCommand = new WithdrawCommand(onlyWithdraw, currency);
            withdrawCommands.add(withdrawCommand);
            CUtils.registerCommand(withdrawCommand, onlyWithdraw);
        } else {
            disabledCurrencies.add(currency);
        }
    }

    public static void reloadCurrencies() {

        /* Commands */

        for (WithdrawCommand withdrawCommand : withdrawCommands) {
            CUtils.unregisterCommand(withdrawCommand);
        }

        withdrawCommands.clear();

        /* Currencies */

        List<Currency> registeredCurrencies = new ArrayList<>(enabledCurrencies);
        registeredCurrencies.addAll(disabledCurrencies);

        enabledCurrencies.clear();
        disabledCurrencies.clear();

        for (Currency currency : registeredCurrencies) {
            registerCurrency(currency);
        }
    }
}
