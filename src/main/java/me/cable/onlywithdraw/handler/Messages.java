package me.cable.onlywithdraw.handler;

import me.cable.onlycore.action.Actions;
import me.cable.onlycore.handler.MessagesHandler;
import me.cable.onlywithdraw.OnlyWithdraw;
import org.jetbrains.annotations.NotNull;

public class Messages extends MessagesHandler<OnlyWithdraw> {

    private static final String ERROR = "error";

    public Messages(@NotNull OnlyWithdraw onlyWithdraw) {
        super(onlyWithdraw);
    }

    public @NotNull Actions aboveMax(@NotNull String max) {
        return get(ERROR + ".above-max")
                .placeholder("max", max);
    }

    public @NotNull Actions insufficientBalance(@NotNull String balance, @NotNull String difference, @NotNull String target) {
        return get(ERROR + ".insufficient-balance")
                .placeholder("balance", balance)
                .placeholder("difference", difference)
                .placeholder("target", target);
    }

    public @NotNull Actions invalidPlayer(@NotNull String provided) {
        return get(ERROR + ".invalid-player")
                .placeholder("provided", provided);
    }

    public @NotNull Actions noPermission() {
        return get(ERROR + ".no-permission");
    }

    public @NotNull Actions onlyPlayers() {
        return get(ERROR + ".only-players");
    }

    public @NotNull Actions underMin(@NotNull String min) {
        return get(ERROR + ".under-min")
                .placeholder("min", min);
    }
}
