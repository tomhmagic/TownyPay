package com.tanukicraft.townypay.settings;

import com.tanukicraft.townypay.TownyPay;
import org.bukkit.configuration.file.FileConfiguration;

public class SavingsSettings {
    private static FileConfiguration config = TownyPay.getPlugin(TownyPay.class).getConfig();
    public static double getBaseInterest(){
        return config.getDouble("SavingsSettings.BaseInterest");
    }
    public static double getVariableModifier(){
        return config.getDouble("SavingsSettings.VariableModifier");
    }
    public static int getLastOnline(){
        return config.getInt("SavingsSettings.LastOnline");
    }
    public static int getSavingLimitsMin(){
        return config.getInt("SavingsSettings.Limits.Savings.Minimum");
    }
    public static int getSavingLimitsMax(){
        return config.getInt("SavingsSettings.Limits.Savings.Maximum");
    }
    public static int getSavingLimitsCap(){
        return config.getInt("SavingsSettings.Limits.Savings.Cap");
    }
    public static int getHoldingsLimitsMin(){
        return config.getInt("SavingsSettings.Limits.Holdings.Minimum");
    }
    public static int getHoldingsLimitsMax(){
        return config.getInt("SavingsSettings.Limits.Holdings.Maximum");
    }
    public static boolean isTownBalanceInterestEnabled(){
        return config.getBoolean("SavingsSettings.TownBalanceInterest.Enabled");
    }
    public static double getTownBalanceInterestThresholds(String threshold) {
        switch (threshold) {
            case "Minimal":
                return config.getDouble("SavingsSettings.TownBalanceInterest.Thresholds.Minimal");
            case "Low":
                return config.getDouble("SavingsSettings.TownBalanceInterest.Thresholds.Low");
            case "Moderate":
                return config.getDouble("SavingsSettings.TownBalanceInterest.Thresholds.Moderate");
            case "High":
                return config.getDouble("SavingsSettings.TownBalanceInterest.Thresholds.High");
            case "Maximum":
                return config.getDouble("SavingsSettings.TownBalanceInterest.Thresholds.Maximum");
            default:
                throw new IllegalArgumentException("Invalid setting: " + threshold);
        }
    }
    public static double getTownBalanceInterestRates(String rates) {
        switch (rates) {
            case "Minimal":
                return config.getDouble("SavingsSettings.TownBalanceInterest.Rates.Minimal");
            case "Low":
                return config.getDouble("SavingsSettings.TownBalanceInterest.Rates.Low");
            case "Moderate":
                return config.getDouble("SavingsSettings.TownBalanceInterest.Rates.Moderate");
            case "High":
                return config.getDouble("SavingsSettings.TownBalanceInterest.Rates.High");
            case "Maximum":
                return config.getDouble("SavingsSettings.TownBalanceInterest.Rates.Maximum");
            default:
                throw new IllegalArgumentException("Invalid setting: " + rates);
        }
    }
}
