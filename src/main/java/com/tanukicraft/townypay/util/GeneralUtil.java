package com.tanukicraft.townypay.util;

import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

import java.time.LocalDate;
import java.time.ZoneId;

public class GeneralUtil {
    public static boolean isNotInteger(String input) {
        try {
            Integer.parseInt(input);
            return false; // It's an integer
        } catch (NumberFormatException e) {
            return true; // It's not an integer
        }
    }


    public static Town getTown(Resident resident){
        Town town = null;
        if (resident.hasTown()){
            try {
                town = resident.getTown();
            } catch (TownyException e) {
                throw new RuntimeException(e);
            }
        }
        return town;
    }

    public static Nation getNation(Resident resident){
        Nation nation = null;
        if (resident.hasNation()){
            try {
                nation = resident.getNation();
            } catch (TownyException e) {
                throw new RuntimeException(e);
            }
        }
        return nation;
    }

    public  static boolean isSameTown(Town firstTown, Town secondTown){
        if (firstTown == null | secondTown == null){
            return false;
        } else return firstTown == secondTown;
    }

    public  static boolean isSameNation(Nation firstNation, Nation secondNation){
        if (firstNation == null | secondNation == null){
            return false;
        } else return firstNation == secondNation;
    }

    public static double calcProfit (Double balance, Double upkeep, Double profitCac){
        return balance - ((upkeep / 100) * profitCac);
    }

    public static boolean isWithinLastOnline(Resident resident, Integer days) {
        long timestamp = resident.getLastOnline();

        // Calculate the date that is daysToSubtract before the current date
        LocalDate currentDate = LocalDate.now();
        LocalDate targetDate = currentDate.minusDays(days);

        // Convert the target date to a timestamp
        long targetTimestamp = targetDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        // Check if the given timestamp is within the specified range
        return timestamp >= targetTimestamp;
    }
}
