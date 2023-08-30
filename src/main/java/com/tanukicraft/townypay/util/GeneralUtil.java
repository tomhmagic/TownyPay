package com.tanukicraft.townypay.util;

import com.palmergames.bukkit.towny.TownyUniverse;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;

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
        } else if (firstTown == secondTown){
            return true;
        } else {
            return false;
        }
    }

    public  static boolean isSameNation(Nation firstNation, Nation secondNation){
        if (firstNation == null | secondNation == null){
            return false;
        } else if (firstNation == secondNation){
            return true;
        } else {
            return false;
        }
    }
}
