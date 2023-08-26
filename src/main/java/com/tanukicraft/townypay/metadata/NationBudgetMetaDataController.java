package com.tanukicraft.townypay.metadata;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

public class NationBudgetMetaDataController {
    private static IntegerDataField nationBudgetidf = new IntegerDataField("TownyPayBudget", 0);
    private static IntegerDataField nationSpendidf = new IntegerDataField("TownyPaySpend", 0);

    public static boolean hasBudgetData(Nation nation){
        return MetaDataUtil.hasMeta(nation,nationBudgetidf);
    }

    public static boolean hasSpendData(Nation nation){
        return MetaDataUtil.hasMeta(nation,nationSpendidf);
    }

    public static int getBudgetData(Nation nation){
        return MetaDataUtil.getInt(nation, nationBudgetidf);
    }

    public static int getSpendData(Nation nation){
        return MetaDataUtil.getInt(nation, nationSpendidf);
    }

    public static void setBudgetData(Nation nation, Integer data){
        MetaDataUtil.setInt(nation, nationBudgetidf, data, true);
    }

    public static void setSpendData(Nation nation, Integer data){
        MetaDataUtil.setInt(nation, nationSpendidf, data, true);
    }
}
