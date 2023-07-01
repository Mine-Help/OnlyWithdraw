package me.cable.onlywithdraw.command;

import me.cable.onlycore.command.CustomCommand;
import me.cable.onlycore.util.CUtils;
import me.cable.onlywithdraw.OnlyWithdraw;
import me.cable.onlywithdraw.handler.CurrencyManager;
import me.cable.onlywithdraw.handler.Messages;
import me.cable.onlywithdraw.handler.Settings;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MainCommand extends CustomCommand<OnlyWithdraw> {

    public static final String COMMAND_PATH = "onlywithdraw";

    private final Settings settings;
    private final Messages messages;

    public MainCommand(@NotNull OnlyWithdraw onlyWithdraw) {
        super(onlyWithdraw);
        this.settings = onlyWithdraw.getSettings();
        this.messages = onlyWithdraw.getMessages();
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        String path = COMMAND_PATH;
        int length = args.length;

        if (length == 0) {
            PluginDescriptionFile desc = plugin.getDescription();
            messages.command(path, "plugin-information")
                    .placeholder("command", label)
                    .placeholder("name", desc.getName())
                    .placeholder("version", desc.getVersion())
                    .run(sender);
            return true;
        }

        switch (args[0]) {
            case "help": {
                messages.command(path, "help")
                        .placeholder("command", label)
                        .run(sender);
                break;
            }
            case "reload": {
                long millis = System.currentTimeMillis();
                Player player = (sender instanceof Player) ? (Player) sender : null;

                messages.load(player);
                settings.load(player);
                CurrencyManager.reloadCurrencies();

                messages.command(path, "reload")
                        .placeholder("millis", Long.toString(System.currentTimeMillis() - millis))
                        .run(sender);
                break;
            }
            default: {
                messages.command(path, "unknown-command")
                        .placeholder("command", label)
                        .run(sender);
            }
        }

        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        final List<String> result = new ArrayList<>();
        final int length = args.length;

        if (length == 1) {
            for (String a : CUtils.list("help", "reload")) {
                if (a.startsWith(args[0])) {
                    result.add(a);
                }
            }
        }

        return result;
    }
}
