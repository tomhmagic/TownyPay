package com.tanukicraft.townypay.settings;

import com.tanukicraft.townypay.TownyPay;
import org.bukkit.configuration.file.FileConfiguration;

public class TownSettings {
    private static FileConfiguration config = TownyPay.getPlugin(TownyPay.class).getConfig();

    //Profit Settings
    public static double getProfitMultiplier(){ return config.getDouble("TownSettings.Profit.Multiplier"); }

    //Mayor Pay Settings
    public static boolean isPayEnabled(){ return config.getBoolean("TownSettings.MayorPay.Enabled"); }
    public static boolean canTogglePay(){ return config.getBoolean("TownSettings.MayorPay.CanToggle"); }
    public static boolean canSetPay(){ return config.getBoolean("TownSettings.MayorPay.CanSet"); }
    public static boolean isPayFixed(){ return config.getBoolean("TownSettings.MayorPay.PayFixedAmount"); }
    public static int getPayValue(){
        return config.getInt("TownSettings.MayorPay.Pay.Value");
    }
    public static int getPayMin(){
        return config.getInt("TownSettings.MayorPay.Pay.Min");
    }
    public static int getPayMax(){
        return config.getInt("TownSettings.MayorPay.Pay.Max");
    }
    public static double getPayTax(){
        return config.getDouble("TownSettings.MayorPay.Tax");
    }
    public static double getPayMinimumBal(){
        return config.getDouble("TownSettings.MayorPay.MinimumBal");
    }

    //Budget Settings
    public static boolean isBudgetFixed(){ return config.getBoolean("TownSettings.BudgetSettings.FixedBudget"); }
    public static double getBudgetAmount(){
        return config.getDouble("TownSettings.BudgetSettings.Budget");
    }
    public static double getBudgetMinimumBal(){
        return config.getDouble("TownSettings.BudgetSettings.MinimumBal");
    }

    //Tax Settings
    public static double getResidentTax(){
        return config.getDouble("TownSettings.Tax.Resident");
    }
    public static double getOutsiderTax(){
        return config.getDouble("TownSettings.Tax.Outsider");
    }
}
