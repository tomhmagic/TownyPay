package com.tanukicraft.townypay.functions;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.metadata.SavingsMetaDataController;
import com.tanukicraft.townypay.settings.SavingsSettings;
import com.tanukicraft.townypay.util.GeneralUtil;
import com.tanukicraft.townypay.util.MessageUtil;

import java.util.List;
import java.util.Random;

public class SavingsFunctions {
    private static Double baseInterest = SavingsSettings.getBaseInterest();
    private static Double minimalInterest = SavingsSettings.getTownBalanceInterestRates("Minimal");
    private static Double lowInterest = SavingsSettings.getTownBalanceInterestRates("Low");
    private static Double moderateInterest = SavingsSettings.getTownBalanceInterestRates("Moderate");
    private static Double highInterest = SavingsSettings.getTownBalanceInterestRates("High");
    private static Double maximumInterest = SavingsSettings.getTownBalanceInterestRates("Maximum");
    private static final Double variableModifier = SavingsSettings.getVariableModifier();


    public static void paySavings(){

        boolean doVariableModifier = variableModifier > 0.0;

        if (doVariableModifier) {
            minimalInterest = calcVariableModifier(minimalInterest);
            lowInterest = calcVariableModifier(lowInterest);
            moderateInterest = calcVariableModifier(moderateInterest);
            highInterest = calcVariableModifier(highInterest);
            maximumInterest = calcVariableModifier(maximumInterest);
            baseInterest = calcVariableModifier(baseInterest);
        }

        List<Resident> residents = TownyAPI.getInstance().getResidents();
        MessageUtil.logStatus(Translatable.of("townypay.status.log.Savings.start"));
        for (Resident resident: residents){
            double interest = baseInterest;
            if (doVariableModifier) {
                if (resident.hasTown()) {
                    double balance;
                    int residentCount;
                    try {
                        balance = resident.getTown().getAccount().getHoldingBalance();
                        residentCount = resident.getTown().getResidents().size();
                    } catch (TownyException e) {
                        throw new RuntimeException(e);
                    }
                    double averageBalance = balance / residentCount;
                    interest = getThresholdInterest(averageBalance,minimalInterest,lowInterest,moderateInterest,highInterest,maximumInterest);
                }
            }
            if (GeneralUtil.isWithinLastOnline(resident,SavingsSettings.getLastOnline())) {
                addInterest(resident, interest);
                moveHoldings(resident);
            }
        }
        MessageUtil.logStatus(Translatable.of("townypay.status.log.Savings.end"));
    }

    private static void addInterest(Resident resident, double interest){
        if (!SavingsMetaDataController.hasSavingsData(resident)){
            SavingsMetaDataController.setSavingsData(resident,0);
        }
        int currentSavings = SavingsMetaDataController.getSavingsData(resident);
        if (currentSavings >= SavingsSettings.getSavingLimitsMin()){
            if (currentSavings < SavingsSettings.getSavingLimitsCap() || SavingsSettings.getSavingLimitsCap() == -1) {
                SavingsMetaDataController.setSavingsData(resident, currentSavings + (int) interest);
                MessageUtil.logStatus(Translatable.of("townypay.status.log.Savings.InterestPayment", resident, interest, currentSavings));
            }
        }
    }
    private static void moveHoldings(Resident resident){
        if (!SavingsMetaDataController.hasHoldingsData(resident)){
            SavingsMetaDataController.setHoldingsData(resident,0);
        }
        if (SavingsMetaDataController.getHoldingsData(resident) > 0){
            int holdings = SavingsMetaDataController.getHoldingsData(resident);
            int currentSavings = SavingsMetaDataController.getSavingsData(resident);
            int newSavings = currentSavings + holdings;
            SavingsMetaDataController.setSavingsData(resident, newSavings);
            SavingsMetaDataController.setHoldingsData(resident,0);
            MessageUtil.logStatus(Translatable.of("townypay.status.log.Savings.HoldingsTransfer",holdings, resident, newSavings));
        }
    }

    private static double calcVariableModifier(double baseInterest){
        double minValue = (baseInterest - SavingsFunctions.variableModifier);
        double maxValue = (baseInterest + SavingsFunctions.variableModifier);

        // Create a random number generator
        Random random = new Random();

        // Calculate the range
        double range = maxValue - minValue;

        // Generate a random number within the specified range
        double randomValue = minValue + (random.nextDouble() * range);

        // Round the randomValue to two decimal places
        randomValue = Math.round(randomValue * 100.0) / 100.0;

        return randomValue;
    }

    private static double getThresholdInterest(double averageBalance, double minimalInterest, double lowInterest, double moderateInterest, double highInterest, double maximumInterest){
        double minimal = SavingsSettings.getTownBalanceInterestThresholds("Minimal");
        double low = SavingsSettings.getTownBalanceInterestThresholds("Low");
        double moderate = SavingsSettings.getTownBalanceInterestThresholds("Moderate");
        double high = SavingsSettings.getTownBalanceInterestThresholds("High");
        double maximum = SavingsSettings.getTownBalanceInterestThresholds("Maximum");

        if (averageBalance >= minimal && averageBalance < low){
            return minimalInterest;
        } else if (averageBalance >= low && averageBalance < moderate){
            return lowInterest;
        } else if (averageBalance >= moderate && averageBalance < high){
            return moderateInterest;
        } else if (averageBalance >= high && averageBalance < maximum){
            return highInterest;
        } else if (averageBalance >= maximum){
            return maximumInterest;
        } else {
            return baseInterest;
        }
    }
}
