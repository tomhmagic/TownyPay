package com.tanukicraft.townypay.metadata;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

public class NationBudgetMetaDataController {
    private static IntegerDataField budgetIDF = new IntegerDataField("TownyPayBudget", 0);
    private static IntegerDataField spedIDF = new IntegerDataField("TownyPaySpend", 0);

    public static boolean hasBudgetData(Nation nation){
        return MetaDataUtil.hasMeta(nation,budgetIDF);
    }

    public static boolean hasSpendData(Nation nation){
        return MetaDataUtil.hasMeta(nation,spedIDF);
    }

    public static int getBudgetData(Nation nation){
        return MetaDataUtil.getInt(nation, budgetIDF);
    }

    public static int getSpendData(Nation nation){
        return MetaDataUtil.getInt(nation, spedIDF);
    }

    public static void setBudgetData(Nation nation, Integer data){
        MetaDataUtil.setInt(nation, budgetIDF, data, true);
    }

    public static void setSpendData(Nation nation, Integer data){
        MetaDataUtil.setInt(nation, spedIDF, data, true);
    }
}
