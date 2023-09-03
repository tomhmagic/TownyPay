package com.tanukicraft.townypay.metadata;

import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.utils.MetaDataUtil;

public class MayorPayMetaDataController {
    private static IntegerDataField payIDF = new IntegerDataField("TownyPaySetPay", 0);
    private static BooleanDataField toggleBDF = new BooleanDataField("TownyPaySetPay", true);

    public static boolean hasPayData(Town town){

        return MetaDataUtil.hasMeta(town,payIDF);
    }
    public static boolean hasToggleData(Town town){

        return MetaDataUtil.hasMeta(town,toggleBDF);
    }
    public static int getPayData(Town town){

        return MetaDataUtil.getInt(town, payIDF);
    }
    public static boolean getToggleData(Town town){

        return MetaDataUtil.getBoolean(town, toggleBDF);
    }
    public static void setPayData(Town town, Integer data){
        MetaDataUtil.setInt(town, payIDF, data, true);
    }
    public static void setToggleData(Town town, Boolean bool){
        MetaDataUtil.setBoolean(town, toggleBDF, bool, true);
    }

}
