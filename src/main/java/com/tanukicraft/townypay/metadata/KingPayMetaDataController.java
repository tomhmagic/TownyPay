package com.tanukicraft.townypay.metadata;

import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

public class KingPayMetaDataController {
    private static IntegerDataField payIDF = new IntegerDataField("TownyPaySetPay", 0);
    private static BooleanDataField toggleBDF = new BooleanDataField("TownyPayToggle", true);

    public static boolean hasPayData(Nation nation){
        return MetaDataUtil.hasMeta(nation,payIDF);
    }
    public static boolean hasToggleData(Nation nation){
        return MetaDataUtil.hasMeta(nation,toggleBDF);
    }
    public static int getPayData(Nation nation){
        return MetaDataUtil.getInt(nation, payIDF);
    }
    public static boolean getToggleData(Nation nation){
        return MetaDataUtil.getBoolean(nation, toggleBDF);
    }
    public static void setPayData(Nation nation, Integer data){
        MetaDataUtil.setInt(nation, payIDF, data, true);
    }
    public static void setToggleData(Nation nation, Boolean bool){ MetaDataUtil.setBoolean(nation, toggleBDF, bool, true);}
}
