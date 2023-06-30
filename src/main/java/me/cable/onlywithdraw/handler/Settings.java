package me.cable.onlywithdraw.handler;

import me.cable.onlycore.handler.ConfigHandler;
import me.cable.onlywithdraw.OnlyWithdraw;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class Settings extends ConfigHandler<OnlyWithdraw> {

    public Settings(@NotNull OnlyWithdraw onlyWithdraw) {
        super(onlyWithdraw);
    }

    public boolean cancelCrafting() {
        return bool("cancel-crafting");
    }

    public boolean cancelVillagerTrading() {
        return bool("cancel-villager-trading");
    }

    public boolean experienceOnDeathEnabled() {
        return bool("experience-on-death.enabled");
    }

    public @Nullable String experienceOnDeathCurrencyId() {
        return string("experience-on-death.currency-id");
    }

    public boolean experienceOnDeathFullAmount() {
        return bool("experience-on-death.full-amount");
    }

    public @Nullable BigDecimal experienceOnDeathMin() {
        return bigDecimal("experience-on-death.min");
    }

    public @NotNull Map<String, BigDecimal> numberSymbols() {
        Map<String, BigDecimal> map = new HashMap<>();
        String path = "number-symbols";
        ConfigurationSection cs = cs(path);

        if (cs != null) {
            for (String key : cs.getKeys(false)) {
                BigDecimal val = bigDecimal(path + "." + key, BigDecimal.ZERO);
                map.put(key, val);
            }
        }

        return map;
    }
}
