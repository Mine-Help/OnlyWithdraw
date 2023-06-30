package me.cable.onlywithdraw.currency;

import me.cable.onlycore.action.Actions;
import me.cable.onlycore.util.CUtils;
import me.cable.onlycore.util.FormatUtils;
import me.cable.onlycore.util.ItemUtils;
import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.handler.Settings;
import me.cable.onlywithdraw.util.NumberUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public abstract class ConfigCurrency extends Currency {

    private final Settings settings;

    public ConfigCurrency(@NotNull String id) {
        super(id);
        settings = OnlyWithdraw.getInstance().getSettings();
    }

    private @NotNull ConfigurationSection config() {
        return settings.csOrCreate("currencies." + getId());
    }

    @Override
    public boolean isEnabled() {
        return config().getBoolean("enabled");
    }

    @Override
    public @NotNull String getCommandLabel() {
        return config().getString("command.label", "");
    }

    @Override
    public @Nullable String getPermission() {
        return config().getString("command.permission");
    }

    @Override
    public @Nullable String getAdminPermission() {
        return config().getString("command.admin-permission");
    }

    @Override
    public @Nullable BigDecimal getMin() {
        String min = config().getString("min");
        return (min == null) ? null : new BigDecimal(min);
    }

    @Override
    public @Nullable BigDecimal getMax() {
        String max = config().getString("max");
        return (max == null || max.equals("-1")) ? null : new BigDecimal(max);
    }

    @Override
    public void onRedeem(@NotNull Player player, @NotNull BigDecimal value, int amount) {
        new Actions(config().getStringList("events.redeem"))
                .placeholder("amount", FormatUtils.integer(amount))
                .placeholder("total", format(value.multiply(new BigDecimal(amount))))
                .placeholder("value", format(value))
                .run(player);
    }

    @Override
    public void onWithdraw(@NotNull Player player, @NotNull BigDecimal value, int amount) {
        new Actions(config().getStringList("events.withdraw"))
                .placeholder("amount", FormatUtils.integer(amount))
                .placeholder("total", format(value.multiply(new BigDecimal(amount))))
                .placeholder("value", format(value))
                .run(player);
    }

    @Override
    public @NotNull ItemStack createItem(@NotNull BigDecimal value, @Nullable String owner) {
        Map<String, String> placeholders = new HashMap<>();
        placeholders.put("%value_long%", format(value));
        placeholders.put("%value_short%", NumberUtils.abbreviate(value));

        if (owner != null) {
            placeholders.put("%owner%", owner);
        }

        ConfigurationSection itemCs = CUtils.getOrCreateCS(config(), "items." + (owner == null ? "no-owner" : "owner"));
        return ItemUtils.fromCS(itemCs, placeholders);
    }
}
