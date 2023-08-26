package com.tanukicraft.townypay.util;

public class GeneralUtil {
    public static boolean isNotInteger(String input) {
        try {
            Integer.parseInt(input);
            return false; // It's an integer
        } catch (NumberFormatException e) {
            return true; // It's not an integer
        }
    }
}
