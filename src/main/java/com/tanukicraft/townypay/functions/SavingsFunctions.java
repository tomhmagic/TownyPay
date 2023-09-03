package com.tanukicraft.townypay.functions;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.metadata.SavingsMetaDataController;
import com.tanukicraft.townypay.settings.SavingsSettings;
import com.tanukicraft.townypay.util.MessageUtil;

import java.time.LocalDate;
import java.time.ZoneId;
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

        boolean doVariableModifer = false;

        if (variableModifier > 0.0) {
            doVariableModifer = true;
        }

        if (doVariableModifer) {
            minimalInterest = calcVariableModifier(minimalInterest, variableModifier);
            lowInterest = calcVariableModifier(lowInterest, variableModifier);
            moderateInterest = calcVariableModifier(moderateInterest, variableModifier);
            highInterest = calcVariableModifier(highInterest, variableModifier);
            maximumInterest = calcVariableModifier(maximumInterest, variableModifier);
            baseInterest = calcVariableModifier(baseInterest, variableModifier);
        }

        List<Resident> residents = TownyAPI.getInstance().getResidents();
        for (Resident resident: residents){
            double interest = baseInterest;
            if (doVariableModifer) {
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
            if (isWithinLastOnline(resident)) {
                MessageUtil.logStatus(Translatable.of("townypay.status.log.Savings.start"));
                addInterest(resident, interest);
                moveHoldings(resident);
                MessageUtil.logStatus(Translatable.of("townypay.status.log.Savings.end"));
            }
        }
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

    private static double calcVariableModifier(double baseInterest, double modifier){
        double minValue = (baseInterest - modifier);
        double maxValue = (baseInterest + modifier);

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
    private static boolean isWithinLastOnline(Resident resident) {
        long timestamp = resident.getLastOnline();
        int daysToSubtract = SavingsSettings.getLastOnline();

        // Calculate the date that is daysToSubtract before the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate targetDate = currentDate.minusDays(daysToSubtract);

        // Convert the target date to a timestamp
        long targetTimestamp = targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // Check if the given timestamp is within the specified range
        return timestamp >= targetTimestamp;
    }


}
