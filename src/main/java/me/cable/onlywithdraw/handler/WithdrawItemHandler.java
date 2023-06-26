package me.cable.onlywithdraw.handler;

import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.currency.Currency;
import me.cable.onlywithdraw.util.BigDecimalPersistentDataType;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

public class WithdrawItemHandler {

    private final NamespacedKey WITHDRAW_CURRENCY_KEY;
    private final NamespacedKey WITHDRAW_VALUE_KEY;

    public WithdrawItemHandler(@NotNull OnlyWithdraw onlyWithdraw) {
        WITHDRAW_CURRENCY_KEY = new NamespacedKey(onlyWithdraw, "withdraw-currency");
        WITHDRAW_VALUE_KEY = new NamespacedKey(onlyWithdraw, "withdraw-value");
    }

    public @NotNull ItemStack create(
            @NotNull Currency currency,
            @NotNull BigDecimal value,
            int amount,
            @Nullable String owner
    ) {
        ItemStack item = currency.createItem(value, owner);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            PersistentDataContainer pdc = meta.getPersistentDataContainer();
            pdc.set(WITHDRAW_CURRENCY_KEY, PersistentDataType.STRING, currency.getId());
            pdc.set(WITHDRAW_VALUE_KEY, BigDecimalPersistentDataType.INSTANCE, value);

            item.setItemMeta(meta);
        }

        item.setAmount(amount);
        return item;
    }

    public @Nullable BigDecimal getValue(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        return persistentDataContainer.get(WITHDRAW_VALUE_KEY, BigDecimalPersistentDataType.INSTANCE);
    }

    public @Nullable String getCurrencyId(@NotNull ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return null;
        PersistentDataContainer persistentDataContainer = meta.getPersistentDataContainer();
        return persistentDataContainer.get(WITHDRAW_CURRENCY_KEY, PersistentDataType.STRING);
    }

    public boolean isWithdrawItem(@NotNull ItemStack item) {
        return (getCurrencyId(item) != null);
    }
}
