package com.tanukicraft.townypay.metadata;

import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

public class SavingsMetaDataController {
    private static IntegerDataField savingsIDF = new IntegerDataField("TownyPaySavings", 0);
    private static IntegerDataField holdingsIDF = new IntegerDataField("TownyPayHoldings", 0);


    public static boolean hasSavingsData(Resident resident){
        return MetaDataUtil.hasMeta(resident,savingsIDF);
    }
    public static int getSavingsData(Resident resident){
        return MetaDataUtil.getInt(resident, savingsIDF);
    }
    public static void setSavingsData(Resident resident, Integer data){
        MetaDataUtil.setInt(resident, savingsIDF, data, true);
    }

    public static boolean hasHoldingsData(Resident resident){
        return MetaDataUtil.hasMeta(resident,holdingsIDF);
    }
    public static int getHoldingsData(Resident resident){
        return MetaDataUtil.getInt(resident, holdingsIDF);
    }
    public static void setHoldingsData(Resident resident, Integer data){
        MetaDataUtil.setInt(resident, holdingsIDF, data, true);
    }
}
