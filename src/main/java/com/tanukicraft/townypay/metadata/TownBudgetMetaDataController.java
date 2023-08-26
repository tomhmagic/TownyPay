package com.tanukicraft.townypay.metadata;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;
public class TownBudgetMetaDataController {
    private static IntegerDataField townBudgetidf = new IntegerDataField("TownyPayBudget", 0);
    private static IntegerDataField townSpendidf = new IntegerDataField("TownyPaySpend", 0);

    public static boolean hasBudgetData(Town town){
      return MetaDataUtil.hasMeta(town,townBudgetidf);
    }

    public static boolean hasSpendData(Town town){
        return MetaDataUtil.hasMeta(town,townSpendidf);
    }

    public static int getBudgetData(Town town){
        return MetaDataUtil.getInt(town, townBudgetidf);
    }

    public static int getSpendData(Town town){
        return MetaDataUtil.getInt(town, townSpendidf);
    }

    public static void setBudgetData(Town town, Integer data){
        MetaDataUtil.setInt(town, townBudgetidf, data, true);
    }

    public static void setSpendData(Town town, Integer data){
        MetaDataUtil.setInt(town, townSpendidf, data, true);
    }
}
