package com.tanukicraft.townypay.settings;

import com.tanukicraft.townypay.TownyPay;
import org.bukkit.configuration.file.FileConfiguration;

public class NationSettings {
    private static FileConfiguration config = TownyPay.getPlugin(TownyPay.class).getConfig();

    //Profit Settings
    public static double getProfitMultiplier(){ return config.getDouble("NationSettings.Profit.Multiplier"); }

    //Mayor Pay Settings
    public static boolean isPayEnabled(){ return config.getBoolean("NationSettings.KingPay.Enabled"); }
    public static boolean canTogglePay(){ return config.getBoolean("NationSettings.KingPay.CanToggle"); }
    public static boolean canSetPay(){ return config.getBoolean("NationSettings.KingPay.CanSet"); }
    public static boolean isPayFixed(){ return config.getBoolean("NationSettings.KingPay.PayFixedAmount"); }
    public static int getPayValue(){
        return config.getInt("NationSettings.KingPay.Pay.Value");
    }
    public static int getPayMin(){
        return config.getInt("NationSettings.KingPay.Pay.Min");
    }
    public static int getPayMax(){
        return config.getInt("NationSettings.KingPay.Pay.Max");
    }
    public static double getPayTax(){
        return config.getDouble("NationSettings.KingPay.Tax");
    }
    public static double getPayMinimumBal(){
        return config.getDouble("NationSettings.KingPay.MinimumBal");
    }

    //Budget Settings
    public static boolean isBudgetFixed(){ return config.getBoolean("NationSettings.BudgetSettings.FixedBudget"); }
    public static double getBudgetAmount(){
        return config.getDouble("NationSettings.BudgetSettings.Budget");
    }
    public static double getBudgetMinimumBal(){
        return config.getDouble("NationSettings.BudgetSettings.MinimumBal");
    }

    //Tax Settings
    public static double getResidentTax(){
        return config.getDouble("NationSettings.Tax.Resident");
    }
    public static double getOutsiderTax(){
        return config.getDouble("NationSettings.Tax.Outsider");
    }
}