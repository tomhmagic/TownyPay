package com.tanukicraft.townypay.listeners;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.event.NewDayEvent;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.functions.BudgetFunctions;
import com.tanukicraft.townypay.functions.PayFunctions;
import com.tanukicraft.townypay.functions.SavingsFunctions;
import com.tanukicraft.townypay.settings.MessageCacheSettings;
import com.tanukicraft.townypay.util.GeneralUtil;
import com.tanukicraft.townypay.util.MessageCacheUtil;
import com.tanukicraft.townypay.util.MessageUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import com.tanukicraft.townypay.settings.TownSettings;
import com.tanukicraft.townypay.settings.NationSettings;

import java.util.List;

public class TownyNewDayEventListener implements Listener {

    @EventHandler
    public static void onNewDay(NewDayEvent event) {

        //check if there are any towns
        if (TownyAPI.getInstance().getTowns().isEmpty()) {
            MessageUtil.logStatus(Translatable.of("townypay.status.log.notowns"));
        } else {
            //Is Mayor Pay Enabled
            if (TownSettings.isPayEnabled()) {
               PayFunctions.payMayors();
            } else {
                MessageUtil.logStatus(Translatable.of("townypay.status.log.mayorpaydisabled"));
            }
            BudgetFunctions.resetTownBudgets();
        }

        //check if there are any nations
        if (TownyAPI.getInstance().getNations().isEmpty()) {
            MessageUtil.logStatus(Translatable.of("townypay.status.log.nonations"));
        } else {
            //Is King Pay Enabled
            if (NationSettings.isPayEnabled()) {
                PayFunctions.payKings();
            } else {
                MessageUtil.logStatus(Translatable.of("townypay.status.log.kingpaydisabled"));
            }
            BudgetFunctions.resetNationBudgets();
        }

        SavingsFunctions.paySavings();

        if (MessageCacheSettings.getClearCache() != -1){
            checkResidentCacheMsg();
        }
    }

    private static void checkResidentCacheMsg(){
        List<Resident> residents = TownyAPI.getInstance().getResidents();
        int lastOnlineDays = MessageCacheSettings.getClearCache();
        for (Resident resident : residents){
            if(GeneralUtil.isWithinLastOnline(resident, lastOnlineDays)){
                MessageCacheUtil.clearMsgCache(resident);
            }
        }
    }
}
