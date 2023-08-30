package com.tanukicraft.townypay.settings;

import com.tanukicraft.townypay.TownyPay;
import org.bukkit.configuration.file.FileConfiguration;

public class ResidentSettings {
    private static FileConfiguration config = TownyPay.getPlugin(TownyPay.class).getConfig();
    public static double getResidentTax(){
        return config.getDouble("ResidentSettings.Tax.Resident");
    }
    public static double getOutsiderTax(){
        return config.getDouble("ResidentSettings.Tax.Outsider");
    }
}
