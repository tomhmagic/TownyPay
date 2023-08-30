package com.tanukicraft.townypay.listeners;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.event.NewDayEvent;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.economy.Account;
import com.tanukicraft.townypay.metadata.KingPayMetaDataController;
import com.tanukicraft.townypay.metadata.MayorPayMetaDataController;
import com.tanukicraft.townypay.util.TownyPayMessageUtil;
import com.tanukicraft.townypay.metadata.TownBudgetMetaDataController;
import com.tanukicraft.townypay.metadata.NationBudgetMetaDataController;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import java.util.List;
import com.tanukicraft.townypay.settings.TownSettings;
import com.tanukicraft.townypay.settings.NationSettings;

public class TownyNewDayEventListener implements Listener {

    @EventHandler
    public static void onNewDay(NewDayEvent event) {

        //check if there are any towns
        if (TownyAPI.getInstance().getTowns().isEmpty()) {
            TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.notowns"));
        } else {
            //Is Mayor Pay Enabled
            if (TownSettings.isPayEnabled()) {
                payMayors();
            } else {
                TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.notowns"));
            }
            resetTownBudgets();
        }

        //check if there are any nations
        if (TownyAPI.getInstance().getNations().isEmpty()) {
            TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.nonations"));
        } else {
            //Is King Pay Enabled
            if (NationSettings.isPayEnabled()) {
                payKings();
            } else {
                TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.kingpaydisabled"));
            }
            resetNationBudgets();
        }
    }

    public static void payMayors() {
        List<Town> towns = TownyAPI.getInstance().getTowns();
        double pay;
        double tax;
        double mayorPay = TownSettings.getPayValue();

        double townProfitCalc = TownSettings.getProfitMultiplier();
        double mayorPayTax = TownSettings.getPayTax();

        TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.mayorpay.start"));

        for (Town town : towns) {
            Boolean enabled = true;

            if (MayorPayMetaDataController.hasToggleData(town)) {
                enabled = MayorPayMetaDataController.getToggleData(town);
            } else {
                MayorPayMetaDataController.setToggleData(town, true);
            }

            if (enabled) {
                double upkeep = TownySettings.getTownUpkeepCost(town);
                double bal = town.getAccount().getHoldingBalance();
                double profit = calcProfit(bal, upkeep, townProfitCalc);
                if (TownSettings.canSetPay()) {
                    if (!MayorPayMetaDataController.hasPayData(town)) {
                        MayorPayMetaDataController.setPayData(town, TownSettings.getPayValue());
                    }
                    mayorPay = MayorPayMetaDataController.getPayData(town);
                }
                if (town.hasMayor()) {
                    if (bal > TownSettings.getPayMinimumBal()) {
                        Account player = town.getMayor().getAccount();

                        //check if MayorPay is fixed amount or percentage
                        if (TownSettings.isPayFixed()) {
                            pay = mayorPay;
                            tax = mayorPayTax;
                        } else {
                            pay = (profit / 100) * mayorPay;
                            tax = (pay / 100) * mayorPayTax;
                        }

                        //pay Mayor
                        double payFloor = Math.floor(pay);
                        double taxFloor = Math.floor(tax);
                        town.getAccount().payTo(payFloor, player, String.valueOf(Translatable.of("townypay.town.PayReason")));
                        assert player != null;
                        player.withdraw(taxFloor, String.valueOf(Translatable.of("townypay.town.PayTaxReason")));

                        TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.mayorpay", town, payFloor, player.getName(), taxFloor));
                    } else {
                        TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.nobalance.town", town));
                    }
                } else {
                    TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.nomayor", town));
                }
            } else {
                TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.payDisabled.Mayor", town));
            }
        }
        TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.mayorpay.end"));
    }


    public static void payKings() {
        List<Nation> nations = TownyAPI.getInstance().getNations();
        double pay;
        double tax;
        double kingPay = NationSettings.getPayValue();
        double nationProfitCalc = NationSettings.getProfitMultiplier();
        double kingPayTax = NationSettings.getPayTax();

        TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.kingpay.start"));

        for (Nation nation : nations) {
            Boolean enabled = true;

            if (KingPayMetaDataController.hasToggleData(nation)) {
                enabled = KingPayMetaDataController.getToggleData(nation);
            } else {
                KingPayMetaDataController.setToggleData(nation, true);
            }
            double upkeep = TownySettings.getNationUpkeepCost(nation);
            double bal = nation.getAccount().getHoldingBalance();
            double profit = calcProfit(bal, upkeep, nationProfitCalc);
            if (enabled) {
                if (TownSettings.canSetPay()) {
                    if (!KingPayMetaDataController.hasPayData(nation)) {
                        KingPayMetaDataController.setPayData(nation, TownSettings.getPayValue());
                    }
                    kingPay = KingPayMetaDataController.getPayData(nation);
                }
                if (nation.hasKing()) {

                    if (bal > NationSettings.getPayMinimumBal()) {
                        Account player = nation.getKing().getAccount();

                        //check if KingPay is fixed amount or percentage
                        if (NationSettings.isPayFixed()) {
                            pay = kingPay;
                            tax = kingPayTax;
                        } else {
                            pay = (profit / 100) * kingPay;
                            tax = (pay / 100) * kingPayTax;
                        }

                        //Pay King
                        double payFloor = Math.floor(pay);
                        double taxFloor = Math.floor(tax);
                        nation.getAccount().payTo(payFloor, player, String.valueOf(Translatable.of("townypay.nation.PayReason")));
                        assert player != null;
                        player.withdraw(taxFloor, String.valueOf(Translatable.of("townypay.nation.PayTaxReason")));

                        TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.kingpay", nation, payFloor, player.getName(), taxFloor));
                    } else {
                        TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.nobalance.nation", nation));
                    }
                } else {
                    TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.noking", nation));
                }
            }else {
                TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.payDisabled.King", nation));
            }
        }
        TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.kingpay.end"));
    }


        public static void resetTownBudgets () {
            List<Town> towns = TownyAPI.getInstance().getTowns();
            int budgetInt = 0;
            TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.townbudget.start"));
            for (Town town : towns) {
                double upkeep = TownySettings.getTownUpkeepCost(town);

                if (town.hasMayor() & upkeep > 0.0) {
                    double balance = town.getAccount().getHoldingBalance();
                    double budget = calculateTownBudget(upkeep, balance);
                    budgetInt = (int) budget;
                }
                TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.settingbudget", town, budgetInt, 0));

                //set Metadata
                TownBudgetMetaDataController.setBudgetData(town, budgetInt);
                TownBudgetMetaDataController.setSpendData(town, 0);

            }
            TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.townbudget.end"));
        }

        public static void resetNationBudgets () {
            List<Nation> nations = TownyAPI.getInstance().getNations();
            int budgetInt = 0;
            TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.nationbudget.start"));
            for (Nation nation : nations) {
                double upkeep = TownySettings.getNationUpkeepCost(nation);

                if (nation.hasKing() & upkeep > 0.0) {
                    double balance = nation.getAccount().getHoldingBalance();
                    double budget = calculateNationBudget(upkeep, balance);
                    budgetInt = (int) budget;
                }
                TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.settingbudget", nation, budgetInt, 0));

                //set Metadata
                NationBudgetMetaDataController.setBudgetData(nation, budgetInt);
                NationBudgetMetaDataController.setSpendData(nation, 0);
            }
            TownyPayMessageUtil.logStatus(Translatable.of("townypay.status.log.nationbudget.end"));
        }

        public static double calculateTownBudget (Double upkeep, Double bal){
            double townBudget = TownSettings.getBudgetAmount();
            if (TownSettings.isBudgetFixed()) {
                return townBudget;
            } else {
                double profit = calcProfit(bal, upkeep, TownSettings.getProfitMultiplier());
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

        public static double calculateNationBudget (Double upkeep, Double bal){
            double townBudget = NationSettings.getBudgetAmount();
            if (NationSettings.isBudgetFixed()) {
                return townBudget;
            } else {
                double profit = calcProfit(bal, upkeep, NationSettings.getProfitMultiplier());
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

        public static double calcProfit (Double balance, Double upkeep, Double profitCac){
            return balance - ((upkeep / 100) * profitCac);
        }
}
