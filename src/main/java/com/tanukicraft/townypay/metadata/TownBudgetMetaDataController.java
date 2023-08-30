package com.tanukicraft.townypay.metadata;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
public class TownBudgetMetaDataController {
    private static IntegerDataField budgetIDF = new IntegerDataField("TownyPayBudget", 0);
    private static IntegerDataField spedIDF = new IntegerDataField("TownyPaySpend", 0);

    public static boolean hasBudgetData(Town town){
      return MetaDataUtil.hasMeta(town,budgetIDF);
    }

    public static boolean hasSpendData(Town town){
        return MetaDataUtil.hasMeta(town,spedIDF);
    }

    public static int getBudgetData(Town town){
        return MetaDataUtil.getInt(town, budgetIDF);
    }

    public static int getSpendData(Town town){
        return MetaDataUtil.getInt(town, spedIDF);
    }

    public static void setBudgetData(Town town, Integer data){
        MetaDataUtil.setInt(town, budgetIDF, data, true);
    }

    public static void setSpendData(Town town, Integer data){
        MetaDataUtil.setInt(town, spedIDF, data, true);
    }
}
