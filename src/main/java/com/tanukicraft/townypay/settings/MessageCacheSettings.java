package com.tanukicraft.townypay.settings;

import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.TownyPay;
import com.tanukicraft.townypay.util.MessageUtil;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Objects;

public class MessageCacheSettings {
    private static final FileConfiguration config = TownyPay.getPlugin(TownyPay.class).getConfig();

    public static int getClearCache(){
        return config.getInt("MessageCacheSettings.ClearCache");
    }
    public static int getMaxCache(){
        return config.getInt("MessageCacheSettings.MaxCache");
    }
    public static String getExtraMSG(){
        String extraMSG = config.getString("MessageCacheSettings.ExtraMSG");

        if (Objects.equals(extraMSG, "FIRST") || Objects.equals(extraMSG, "LAST") || Objects.equals(extraMSG, "BLOCK")){
            return extraMSG;
        } else {
            MessageUtil.logError(Translatable.of("townypay.ConfigError.MessageCacheSettings.ExtraMSG", extraMSG));
            return null;
        }
    }
}

