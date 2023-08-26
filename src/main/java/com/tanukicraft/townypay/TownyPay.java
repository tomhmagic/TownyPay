package com.tanukicraft.townypay;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyCommandAddonAPI;
import com.palmergames.bukkit.towny.exceptions.KeyAlreadyRegisteredException;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.exceptions.initialization.TownyInitException;
import com.palmergames.bukkit.towny.object.TownyPermissionChange;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.TranslationLoader;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.tanukicraft.townypay.commands.NationFinanceAddon;
import com.tanukicraft.townypay.commands.NationPayAddon;
import com.tanukicraft.townypay.commands.TownFinanceAddon;
import com.tanukicraft.townypay.commands.TownPayAddon;
import com.tanukicraft.townypay.listeners.TownyNewDayEventListener;
import com.tanukicraft.townypay.util.TownyPayMessageUtil;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;


public final class TownyPay extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;

    // Parts of a datafield
    private static String BudgetKeyName = "TownyPayBudget"; // This key must be unique to your plugin.
    private static String SpendKeyName = "TownyPaySpend";
    private static int defaultVal = 0; // This is the default value your data field will have whenever it's added to an object.
    private static final String Budgetlabel = "Budget"; // Label that will be displayed when the towny object's status is shown.
    private static final String Spendlabel = "Spend";

    private static TownyPay plugin;
    public TownyPay() {
        plugin = this;
    }
    public static TownyPay getPlugin() {
        return plugin;
    }

    // Use those parts to create a new data field to store an integer
    private static IntegerDataField TownyPayBudgetField = new IntegerDataField(BudgetKeyName, defaultVal, Budgetlabel);
    private static IntegerDataField TownyPaySpendField = new IntegerDataField(SpendKeyName, defaultVal, Spendlabel);
    // Called when the plugin first loads.
    @Override
    public void onLoad() {
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

        getLogger().info("Custom data field successfully registered!");
    }
    public static IntegerDataField getTownyPayBudgetField() {
        return TownyPayBudgetField;
    }
    public static IntegerDataField getTownyPaySpendField() {
        return TownyPaySpendField;
    }
    @Override
    public void onEnable() {
        // Plugin startup logic
        createPluginFolder();
        this.saveDefaultConfig();
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "finance", new TownFinanceAddon());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.TOWN, "pay", new TownPayAddon());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.NATION, "finance", new NationFinanceAddon());
        TownyCommandAddonAPI.addSubCommand(TownyCommandAddonAPI.CommandType.NATION, "pay", new NationPayAddon());
        getServer().getPluginManager().registerEvents(new TownyNewDayEventListener(), this);
        try {
            loadLocalization(false);
        } catch (TownyException e) {
            throw new RuntimeException(e);
        }

        TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.plugin.Enabled"));
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getServer().getConsoleSender().sendMessage(ChatColor.GOLD + "[TownyPay]: " + ChatColor.RED + "Plugin Disabled");
        TownyPayMessageUtil.logError(Translatable.of("townypay.status.log.plugin.Disabled"));
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
    public static void info(String message) {
        plugin.getLogger().info(message);
    }
}
