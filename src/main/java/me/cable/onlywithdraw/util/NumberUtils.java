package me.cable.onlywithdraw.util;

import me.cable.onlycore.util.FormatUtils;
import me.cable.onlywithdraw.OnlyWithdraw;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public final class NumberUtils {

    private static @NotNull OnlyWithdraw plugin() {
        return JavaPlugin.getPlugin(OnlyWithdraw.class);
    }

    /*
        Returns the number symbols in ascending order.
     */
    private static @NotNull List<Entry<String, BigDecimal>> numberSymbols() {
        return plugin().getSettings().numberSymbols().entrySet().stream()
                .sorted(Entry.comparingByValue()).collect(Collectors.toList());
    }

    public static @NotNull String abbreviate(@NotNull BigDecimal arg) {
        Entry<String, BigDecimal> checkpoint = null;

        for (Entry<String, BigDecimal> entry : numberSymbols()) {
            if (arg.compareTo(entry.getValue()) >= 0) {
                checkpoint = entry;
            }
        }

        if (checkpoint == null) {
            return FormatUtils.integer(arg);
        }

        BigDecimal multiplierValue = checkpoint.getValue();
        MathContext context = new MathContext(5, RoundingMode.FLOOR);
        BigDecimal divide = arg.divide(multiplierValue, context);

        return FormatUtils.decimalPotential(divide) + checkpoint.getKey();
    }

    public static @NotNull BigDecimal expand(@NotNull String arg) throws NumberFormatException {
        for (Entry<String, BigDecimal> entry : numberSymbols()) {
            String symbol = entry.getKey();

            if (arg.endsWith(symbol)) {
                String digits = arg.substring(0, arg.length() - symbol.length());

                try {
                    BigDecimal bigDecimal = new BigDecimal(digits);
                    return bigDecimal.multiply(entry.getValue());
                } catch (NumberFormatException e) {
                    // ignored
                }
            }
        }

        return new BigDecimal(arg);
    }
}
