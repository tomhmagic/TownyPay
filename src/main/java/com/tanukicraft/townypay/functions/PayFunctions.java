package com.tanukicraft.townypay.functions;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownySettings;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.object.economy.Account;
import com.tanukicraft.townypay.metadata.KingPayMetaDataController;
import com.tanukicraft.townypay.metadata.MayorPayMetaDataController;
import com.tanukicraft.townypay.settings.NationSettings;
import com.tanukicraft.townypay.settings.TownSettings;
import com.tanukicraft.townypay.util.GeneralUtil;
import com.tanukicraft.townypay.util.MessageUtil;

import java.util.List;

public class PayFunctions {
    public static void payMayors() {
        List<Town> towns = TownyAPI.getInstance().getTowns();
        double pay;
        double tax;
        double mayorPay = TownSettings.getPayValue();

        double townProfitCalc = TownSettings.getProfitMultiplier();
        double mayorPayTax = TownSettings.getPayTax();

        MessageUtil.logStatus(Translatable.of("townypay.status.log.mayorpay.start"));

        for (Town town : towns) {
            boolean enabled = true;

            if (MayorPayMetaDataController.hasToggleData(town)) {
                enabled = MayorPayMetaDataController.getToggleData(town);
            } else {
                MayorPayMetaDataController.setToggleData(town, true);
            }

            if (enabled) {
                double upkeep = TownySettings.getTownUpkeepCost(town);
                double bal = town.getAccount().getHoldingBalance();
                double profit = GeneralUtil.calcProfit(bal, upkeep, townProfitCalc);
                if (TownSettings.canSetPay()) {
                    String stringCheck = String.valueOf(MayorPayMetaDataController.getPayData(town));
                    if (!MayorPayMetaDataController.hasPayData(town) || !GeneralUtil.isNotInteger(stringCheck)) {
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

                        //Message Mayor
                        MessageUtil.sendResidentMsg(town.getMayor(),Translatable.of("townypay.town.PayMsg", payFloor, taxFloor));

                        MessageUtil.logStatus(Translatable.of("townypay.status.log.mayorpay", town, payFloor, player.getName(), taxFloor));
                    } else {
                        MessageUtil.logStatus(Translatable.of("townypay.status.log.nobalance.town", town));
                    }
                } else {
                    MessageUtil.logStatus(Translatable.of("townypay.status.log.nomayor", town));
                }
            } else {
                MessageUtil.logStatus(Translatable.of("townypay.status.log.payDisabled.Mayor", town));
            }
        }
        MessageUtil.logStatus(Translatable.of("townypay.status.log.mayorpay.end"));
    }

    public static void payKings() {
        List<Nation> nations = TownyAPI.getInstance().getNations();
        double pay;
        double tax;
        double kingPay = NationSettings.getPayValue();
        double nationProfitCalc = NationSettings.getProfitMultiplier();
        double kingPayTax = NationSettings.getPayTax();

        MessageUtil.logStatus(Translatable.of("townypay.status.log.kingpay.start"));

        for (Nation nation : nations) {
            boolean enabled = true;

            if (KingPayMetaDataController.hasToggleData(nation)) {
                enabled = KingPayMetaDataController.getToggleData(nation);
            } else {
                KingPayMetaDataController.setToggleData(nation, true);
            }
            double upkeep = TownySettings.getNationUpkeepCost(nation);
            double bal = nation.getAccount().getHoldingBalance();
            double profit = GeneralUtil.calcProfit(bal, upkeep, nationProfitCalc);
            if (enabled) {
                if (TownSettings.canSetPay()) {
                    String stringCheck = String.valueOf(KingPayMetaDataController.getPayData(nation));
                    if (!KingPayMetaDataController.hasPayData(nation) || !GeneralUtil.isNotInteger(stringCheck)) {
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

                        //Message Mayor
                        MessageUtil.sendResidentMsg(nation.getKing(),Translatable.of("townypay.nation.PayMsg", payFloor, taxFloor));

                        MessageUtil.logStatus(Translatable.of("townypay.status.log.kingpay", nation, payFloor, player.getName(), taxFloor));
                    } else {
                        MessageUtil.logStatus(Translatable.of("townypay.status.log.nobalance.nation", nation));
                    }
                } else {
                    MessageUtil.logStatus(Translatable.of("townypay.status.log.noking", nation));
                }
            }else {
                MessageUtil.logStatus(Translatable.of("townypay.status.log.payDisabled.King", nation));
            }
        }
        MessageUtil.logStatus(Translatable.of("townypay.status.log.kingpay.end"));
    }
}
