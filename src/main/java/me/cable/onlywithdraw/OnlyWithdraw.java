package me.cable.onlywithdraw;

import me.cable.onlywithdraw.command.MainCommand;
import me.cable.onlywithdraw.currency.provided.ExperienceCurrency;
import me.cable.onlywithdraw.currency.provided.MoneyCurrency;
import me.cable.onlywithdraw.handler.Messages;
import me.cable.onlywithdraw.handler.Settings;
import me.cable.onlywithdraw.handler.WithdrawItemHandler;
import me.cable.onlywithdraw.listener.inventory.InventoryClick;
import me.cable.onlywithdraw.listener.inventory.PrepareItemCraft;
import me.cable.onlywithdraw.listener.player.PlayerDeath;
import me.cable.onlywithdraw.listener.player.PlayerInteract;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class OnlyWithdraw extends JavaPlugin {

    private Settings settings;
    private Messages messages;
    private WithdrawItemHandler withdrawItemHandler;

    @Override
    public void onEnable() {
        initializeHandlers();
        registerListeners();
        registerCommands();
        registerCurrencies();
    }

    private void initializeHandlers() {
        settings = new Settings(this);
        messages = new Messages(this);
        withdrawItemHandler = new WithdrawItemHandler(this);
    }

    private void registerListeners() {
        PluginManager pluginManager = getServer().getPluginManager();

        // inventory
        pluginManager.registerEvents(new InventoryClick(this), this);
        pluginManager.registerEvents(new PrepareItemCraft(this), this);

        // player
        pluginManager.registerEvents(new PlayerDeath(this), this);
        pluginManager.registerEvents(new PlayerInteract(this), this);
    }

    private void registerCommands() {
        new MainCommand(this).register(MainCommand.COMMAND_PATH);
    }

    private void registerCurrencies() {
        new ExperienceCurrency(this).register();
        new MoneyCurrency(this).register();
    }

    public @NotNull Settings getSettings() {
        return settings;
    }

    public @NotNull Messages getMessages() {
        return messages;
    }

    public @NotNull WithdrawItemHandler getWithdrawItemHandler() {
        return withdrawItemHandler;
    }
}
