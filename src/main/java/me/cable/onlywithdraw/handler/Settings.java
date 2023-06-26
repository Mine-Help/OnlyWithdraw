package me.cable.onlywithdraw.handler;

import me.cable.onlycore.handler.ConfigHandler;
import me.cable.onlywithdraw.OnlyWithdraw;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;

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
}
