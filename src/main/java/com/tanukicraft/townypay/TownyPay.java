package com.tanukicraft.townypay;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.exceptions.KeyAlreadyRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.exceptions.initialization.TownyInitException;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.TranslationLoader;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.util.Colors;
import com.palmergames.bukkit.util.Version;
import com.tanukicraft.townypay.commands.*;
import com.tanukicraft.townypay.listeners.TownyNewDayEventListener;
import com.tanukicraft.townypay.settings.NationSettings;
import com.tanukicraft.townypay.settings.TownSettings;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


public final class TownyPay extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;
    private static Version requiredTownyVersion = Version.fromString("0.99.5.0");
    private static TownyPay plugin;


    public TownyPay() {
        plugin = this;
    }
    public static TownyPay getPlugin() {
        return plugin;
    }


    // Called when the plugin first loads.
    @Override
    public void onLoad() {
        loadCustomDataField();
    }
    @Override
    public void onEnable() {
        // Plugin startup logic

        if (!loadAll())
            onDisable();
    }
    @Override
    public void onDisable() {
    }
    /**
     * Load towny pay
     *
     * @return true if load succeeded
     */
    public boolean loadAll() {
        try {
            printArt();
            townyVersionCheck();
            loadConfig();
            //Load languages
            loadLocalization(false);

            //Load commands and listeners
            registerCommands();
            registerListeners();

        } catch (TownyException te) {
            severe(te.getMessage());
            severe("TownyPay failed to load! Disabling!");
            return false;
        } catch (Exception e) {
            severe(e.getMessage());
            e.printStackTrace();
            severe("TownyPay failed to load! Disabling!");
            return false;
        }
        info("TownyPay loaded successfully.");
        return true;
    }
    public void createPluginFolder(){
        File f = new File(this.getDataFolder() + "/");
        if(!f.exists())
            f.mkdir();
    }
    private void loadLocalization(boolean reload) throws TownyException {
        try {
            Plugin plugin = getPlugin();
            Path langFolderPath = Paths.get(plugin.getDataFolder().getPath()).resolve("lang");
            TranslationLoader loader = new TranslationLoader(langFolderPath, plugin, TownyPay.class);
            loader.load();
            TownyAPI.getInstance().addTranslations(plugin, loader.getTranslations());
        } catch (TownyInitException e) {
            throw new TownyException("Locale files failed to load! Disabling!");
        }
        if (reload) {
            info(Translatable.of("msg_reloaded_lang").defaultLocale());
        }
    }
    private void printArt() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(              "       ----------------------------------------  ");
        Bukkit.getConsoleSender().sendMessage(Colors.Gold + "          ┌╫─ ╺┳╸┏━┓╻ ╻┏┓╻╻ ╻  ┏━┓┏━┓╻ ╻ ┌╫─     ");
        Bukkit.getConsoleSender().sendMessage(Colors.Gold + "          └╫┐  ┃ ┃ ┃┃╻┃┃┃┃┗┳┛  ┣━┛┣━┫┗┳┛ └╫┐     ");
        Bukkit.getConsoleSender().sendMessage(Colors.Gold + "          ─╫┘  ╹ ┗━┛┗┻┛╹┗┛ ╹   ╹  ╹ ╹ ╹  ─╫┘     ");
        Bukkit.getConsoleSender().sendMessage(              "       ----------------------------------------  ");
        Bukkit.getConsoleSender().sendMessage("");
    }

    private void registerListeners() {
        PluginManager pm = Bukkit.getServer().getPluginManager();
        pm.registerEvents(new TownyNewDayEventListener(), this);
    }

    private void registerCommands() {
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "finance", new TownFinanceAddon());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "pay", new TownPayAddon());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.NATION, "finance", new NationFinanceAddon());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.NATION, "pay", new NationPayAddon());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.RESIDENT, "pay", new ResidentPayAddon());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.RESIDENT, "savings", new ResidentSavingsAddon());

        if (TownSettings.canSetPay()){
            TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN_SET, "pay", new TownPaySetAddon());
        }
        if (NationSettings.canSetPay()){
            TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN_SET, "pay", new NationPaySetAddon());
        }
        if (TownSettings.canTogglePay()){
            TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN_TOGGLE, "pay", new TownPayToggleAddon());
        }
        if (NationSettings.canTogglePay()){
            TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.NATION_TOGGLE, "pay", new NationPayToggleAddon());
        }

    }

    private void loadConfig(){
        createPluginFolder();
        this.saveDefaultConfig();
    }

    private String getTownyVersion() {
        return Bukkit.getPluginManager().getPlugin("Towny").getDescription().getVersion();
    }
    private void townyVersionCheck() throws TownyException{
        if (!(Version.fromString(getTownyVersion()).compareTo(requiredTownyVersion) >= 0))
            throw new TownyException("Towny version does not meet required minimum version: " + requiredTownyVersion.toString());
    }
    public static void severe(String message) {
        plugin.getLogger().severe(message);
    }
    public static void info(String message) {
        plugin.getLogger().info(message);
    }


    // Parts of a datafield

    // Use those parts to create a new data field to store an integer
    private static IntegerDataField TownyPayBudgetField = new IntegerDataField("TownyPayBudget", 0, "Budget");
    private static IntegerDataField TownyPaySpendField = new IntegerDataField("TownyPaySpend", 0, "Spend");
    private static IntegerDataField TownyPaySetPayField = new IntegerDataField("TownyPaySetPay", 0, "Pay");
    private static BooleanDataField TownyPayToggleField = new BooleanDataField("TownyPayToggle", true, "Pay Toggle");
    private static IntegerDataField TownyPaySavingsField = new IntegerDataField("TownyPaySavings", 0, "Savings");
    private static IntegerDataField TownyPayHoldingsField = new IntegerDataField("TownyPayHoldings", 0, "Holdings");

    private void loadCustomDataField(){
        // (Optional) Try to globally register the data field.
        // Globally registering data fields allow them to be modified in-game by administrators.
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPayBudgetField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPaySpendField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPaySetPayField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPayToggleField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPaySavingsField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPayHoldingsField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }

        getLogger().info("Custom data fields successfully registered!");
    }

}
