package me.cable.onlywithdraw.handler;

import me.cable.onlycore.util.CUtils;
import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.command.WithdrawCommand;
import me.cable.onlywithdraw.currency.Currency;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class CurrencyManager {

    private static final OnlyWithdraw onlyWithdraw = OnlyWithdraw.getInstance();

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

    private static @NotNull List<Currency> getAllCurrencies() {
        List<Currency> list = new ArrayList<>(enabledCurrencies);
        list.addAll(disabledCurrencies);
        return list;
    }

    public static void registerCurrency(@NotNull Currency currency) {

        /* Check If Registered */

        for (Currency c : getAllCurrencies()) {
            if (currency.getId().equals(c.getId())) {
                throw new IllegalArgumentException(Currency.class.getSimpleName() + " has already been registered");
            }
        }

        /* Register */

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

        /* Unload Commands */

        for (WithdrawCommand withdrawCommand : withdrawCommands) {
            CUtils.unregisterCommand(withdrawCommand);
        }

        withdrawCommands.clear();

        /* Re-Register Currencies */

        List<Currency> allCurrencies = getAllCurrencies();

        enabledCurrencies.clear();
        disabledCurrencies.clear();

        for (Currency currency : allCurrencies) {
            registerCurrency(currency);
        }
    }
}
