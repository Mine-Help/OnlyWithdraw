package me.cable.onlywithdraw.command;

import me.cable.onlycore.action.Actions;
import me.cable.onlycore.util.CUtils;
import me.cable.onlycore.util.FormatUtils;
import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.currency.Currency;
import me.cable.onlywithdraw.handler.Messages;
import me.cable.onlywithdraw.handler.WithdrawItemHandler;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

public class WithdrawCommand extends Command {

    public static final String COMMAND_PATH = "withdraw";

    private final Messages messages;
    private final WithdrawItemHandler withdrawItemHandler;

    private final Currency currency;

    public WithdrawCommand(@NotNull OnlyWithdraw onlyWithdraw, @NotNull Currency currency) {
        super(currency.getCommandLabel());
        messages = onlyWithdraw.getMessages();
        withdrawItemHandler = onlyWithdraw.getWithdrawItemHandler();

        this.currency = currency;
        setPermission(currency.getPermission());
    }

    private void handleNormal(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        String permission = currency.getPermission();

        if (permission != null && !sender.hasPermission(permission)) {
            messages.noPermission().run(sender);
            return;
        }
        if (!(sender instanceof Player)) {
            messages.onlyPlayers().run(sender);
            return;
        }
        if (args.length == 0) {
            help(sender, label);
            return;
        }

        Player player = (Player) sender;

        /* Value */

        BigDecimal unitValue;
        boolean all = false;

        if (args[0].equals("all")) {
            unitValue = currency.getBalance(player);
            all = true;
        } else {
            try {
                unitValue = FormatUtils.toNumber(args[0]); // TODO: move to withdraw plugin
            } catch (NumberFormatException ex) {
                help(sender, label);
                return;
            }
        }

        /* Amount */

        int amount = 1;

        if (args.length >= 2) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException ex) {
                help(player, label);
                return;
            }
        }

        /* Total Value */

        BigDecimal totalValue;

        if (all) {
            totalValue = unitValue;
            unitValue = totalValue.divide(new BigDecimal(amount), new MathContext(3, RoundingMode.DOWN));
        } else {
            totalValue = unitValue.multiply(new BigDecimal(amount));
        }

        /* Range */

        BigDecimal min = currency.getMin();
        BigDecimal max = currency.getMax();

        if (min != null && unitValue.compareTo(min) < 0) {
            messages.underMin(currency.format(min)).run(sender);
            return;
        }
        if (max != null && unitValue.compareTo(max) > 0) {
            messages.aboveMax(currency.format(max)).run(sender);
            return;
        }

        /* Check Balance */

        BigDecimal balance = currency.getBalance(player);

        if (balance.compareTo(totalValue) < 0) {
            BigDecimal difference = totalValue.subtract(balance);
            messages.insufficientBalance(
                    currency.format(balance),
                    currency.format(difference),
                    currency.format(totalValue)
            ).run(sender);
            return;
        }

        /* Withdraw */

        ItemStack item = withdrawItemHandler.create(currency, unitValue, amount, player.getName());
        CUtils.give(player, item);

        currency.decreaseBalance(player, totalValue);
        currency.onWithdraw(player, unitValue, amount);
    }

    private void handleAdmin(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!isAdmin(sender)) {
            messages.noPermission().run(sender);
            return;
        }

        /* Value */

        BigDecimal value;

        try {
            value = FormatUtils.toNumber(args[0]);
        } catch (NumberFormatException ex) {
            help(sender, label);
            return;
        }

        /* Amount */

        int amount;

        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException ex) {
            help(sender, label);
            return;
        }

        /* Total Value */

        BigDecimal totalValue = value.multiply(new BigDecimal(amount));

        /* Targets */

        List<Player> targets = new ArrayList<>();
        String targetArg = args[2];

        if (targetArg.equals("all")) {
            targets.addAll(Bukkit.getOnlinePlayers());
        } else {
            Player target = Bukkit.getPlayer(targetArg);

            if (target == null) {
                messages.invalidPlayer(targetArg).run(sender);
                return;
            }

            targets.add(target);
        }

        /* Owner */

        String owner = null;

        if (args.length >= 4) {
            owner = args[3];
        }

        /* Give */

        ItemStack item = withdrawItemHandler.create(currency, value, amount, owner);

        for (Player target : targets) {
            CUtils.give(target, item);
            messages.command(COMMAND_PATH, "get")
                    .placeholder("amount", FormatUtils.integer(amount))
                    .placeholder("player", sender.getName())
                    .placeholder("total", currency.format(totalValue))
                    .placeholder("value", currency.format(value))
                    .run(target);
        }

        String targetName = (targets.size() == 1) ? targets.get(0).getName() : "everyone";
        messages.command(COMMAND_PATH, "give")
                .placeholder("amount", FormatUtils.integer(amount))
                .placeholder("target", targetName)
                .placeholder("total", currency.format(totalValue))
                .placeholder("value", currency.format(value))
                .run(sender);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        if (args.length < 3) {
            handleNormal(sender, label, args);
        } else {
            handleAdmin(sender, label, args);
        }

        return true;
    }

    private boolean isAdmin(@NotNull CommandSender commandSender) {
        String permission = currency.getAdminPermission();
        return (permission == null) ? commandSender.isOp() : commandSender.hasPermission(permission);
    }

    private void help(@NotNull CommandSender sender, @NotNull String label) {
        Actions actions;

        if (isAdmin(sender)) {
            actions = messages.command(COMMAND_PATH, "help-admin");
        } else {
            actions = messages.command(COMMAND_PATH, "help");
        }

        actions.placeholder("command", label).run(sender);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, String[] args) {
        List<String> result = new ArrayList<>();
        int length = args.length;

        switch (length) {
            case 1: {
                String arg = args[0];

                if ("all".startsWith(arg)) {
                    result.add("all");
                }

                boolean valid = true;

                try {
                    Double.parseDouble(arg);
                } catch (NumberFormatException e) {
                    valid = false;
                }

                if (valid) {
                    for (Entry<String, BigDecimal> entry : FormatUtils.numberSymbols()) {
                        result.add(arg + entry.getKey());
                    }
                }

                break;
            }
            case 3: {
                if (isAdmin(sender)) {
                    String arg = args[2];

                    if ("all".startsWith(arg)) {
                        result.add("all");
                    }

                    for (Player a : Bukkit.getOnlinePlayers()) {
                        String b = a.getName();

                        if (b.startsWith(arg)) {
                            result.add(b);
                        }
                    }
                }

                break;
            }
        }

        return result;
    }
}
