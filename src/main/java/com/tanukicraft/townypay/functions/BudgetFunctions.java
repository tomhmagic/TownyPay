package com.tanukicraft.townypay.functions;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.metadata.NationBudgetMetaDataController;
import com.tanukicraft.townypay.metadata.TownBudgetMetaDataController;
import com.tanukicraft.townypay.settings.NationSettings;
import com.tanukicraft.townypay.settings.TownSettings;
import com.tanukicraft.townypay.util.GeneralUtil;
import com.tanukicraft.townypay.util.MessageUtil;

import java.util.List;

public class BudgetFunctions {
    public static void resetTownBudgets () {
        List<Town> towns = TownyAPI.getInstance().getTowns();
        int budgetInt = 0;
        MessageUtil.logStatus(Translatable.of("townypay.status.log.townbudget.start"));
        for (Town town : towns) {
            double upkeep = TownySettings.getTownUpkeepCost(town);

            if (town.hasMayor() & upkeep > 0.0) {
                double balance = town.getAccount().getHoldingBalance();
                double budget = calculateTownBudget(upkeep, balance);
                budgetInt = (int) budget;
            }
            MessageUtil.logStatus(Translatable.of("townypay.status.log.settingbudget", town, budgetInt, 0));

            //set Metadata
            TownBudgetMetaDataController.setBudgetData(town, budgetInt);
            TownBudgetMetaDataController.setSpendData(town, 0);

        }
        MessageUtil.logStatus(Translatable.of("townypay.status.log.townbudget.end"));
    }
    public static void resetNationBudgets () {
        List<Nation> nations = TownyAPI.getInstance().getNations();
        int budgetInt = 0;
        MessageUtil.logStatus(Translatable.of("townypay.status.log.nationbudget.start"));
        for (Nation nation : nations) {
            double upkeep = TownySettings.getNationUpkeepCost(nation);

            if (nation.hasKing() & upkeep > 0.0) {
                double balance = nation.getAccount().getHoldingBalance();
                double budget = calculateNationBudget(upkeep, balance);
                budgetInt = (int) budget;
            }
            MessageUtil.logStatus(Translatable.of("townypay.status.log.settingbudget", nation, budgetInt, 0));

            //set Metadata
            NationBudgetMetaDataController.setBudgetData(nation, budgetInt);
            NationBudgetMetaDataController.setSpendData(nation, 0);
        }
        MessageUtil.logStatus(Translatable.of("townypay.status.log.nationbudget.end"));
    }
    private static double calculateTownBudget (Double upkeep, Double bal){
        double townBudget = TownSettings.getBudgetAmount();
        if (TownSettings.isBudgetFixed()) {
            return townBudget;
        } else {
            double profit = GeneralUtil.calcProfit(bal, upkeep, TownSettings.getProfitMultiplier());
            double budget;
            if (profit > TownSettings.getBudgetMinimumBal()) {
                budget = (profit / 100) * townBudget;
                if (budget >= 1.0) {
                    return budget;
                }
                return 0.0;
            }
            return 0.0;
        }
    }

    private static double calculateNationBudget (Double upkeep, Double bal){
        double townBudget = NationSettings.getBudgetAmount();
        if (NationSettings.isBudgetFixed()) {
            return townBudget;
        } else {
            double profit = GeneralUtil.calcProfit(bal, upkeep, NationSettings.getProfitMultiplier());
            double budget;
            if (profit > NationSettings.getBudgetMinimumBal()) {
                budget = (profit / 100) * townBudget;
                if (budget >= 1.0) {
                    return budget;
                }
                return 0.0;
            }
            return 0.0;
        }
    }
}
