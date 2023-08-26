package com.tanukicraft.townypay.settings;

import com.tanukicraft.townypay.TownyPay;
import org.bukkit.configuration.file.FileConfiguration;

public class TownyPaySettings {
    private static FileConfiguration config = TownyPay.getPlugin(TownyPay.class).getConfig();

    //General Settings
    public static boolean isMayorPayEnabled(){ return config.getBoolean("EnableMayorPay"); }
    public static boolean isKingPayEnabled(){
        return config.getBoolean("EnableKingPay");
    }

    //Town Settings
    public static double getTownProfitCalc(){ return config.getDouble("TownProfitCalc"); }
    public static boolean isMayorPayFixedAmount(){
        return config.getBoolean("MayorPayFixedAmount");
    }
    public static double getMayorPay(){
        return config.getDouble("MayorPay");
    }
    public static double getMayorPayTax(){
        return config.getDouble("MayorPayTax");
    }
    public static double getTownMinBal(){
        return config.getDouble("TownMinBal");
    }
    public static boolean isTownFixedBudget(){
        return config.getBoolean("TownFixedBudget");
    }
    public static double getTownBudget(){
        return config.getDouble("TownBudget");
    }
    public static double getTownMinProfit(){
        return config.getDouble("TownMinProfit");
    }
    public static double getTownPayTax(){
        return config.getDouble("TownPayTax");
    }
    public static double getTownPayOutsiderTax(){
        return config.getDouble("TownPayOutsiderTax");
    }

    //Nation Settings
    public static double getNationProfitCalc(){ return config.getDouble("NationProfitCalc"); }
    public static boolean isKingPayFixedAmount(){
        return config.getBoolean("KingPayFixedAmount");
    }
    public static double getKingPay(){
        return config.getDouble("KingPay");
    }
    public static double getKingPayTax(){
        return config.getDouble("KingPayTax");
    }
    public static double getNationMinBal(){
        return config.getDouble("NationMinBal");
    }
    public static boolean isNationFixedBudget(){
        return config.getBoolean("NationFixedBudget");
    }
    public static double getNationBudget(){
        return config.getDouble("NationBudget");
    }
    public static double getNationMinProfit(){
        return config.getDouble("NationMinProfit");
    }
    public static double getNationPayTax(){
        return config.getDouble("NationPayTax");
    }
    public static double getNationPayOutsiderTax(){
        return config.getDouble("NationPayOutsiderTax");
    }

}
