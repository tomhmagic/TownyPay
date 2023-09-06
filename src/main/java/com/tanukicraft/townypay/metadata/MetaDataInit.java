package com.tanukicraft.townypay.metadata;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.KeyAlreadyRegisteredException;
import com.palmergames.bukkit.towny.object.metadata.BooleanDataField;
import com.palmergames.bukkit.towny.object.metadata.IntegerDataField;
import com.palmergames.bukkit.towny.object.metadata.StringDataField;
import static org.bukkit.Bukkit.getLogger;

public class MetaDataInit {
    // Parts of a datafield

    // Use those parts to create a new data field to store an integer
    private static final IntegerDataField TownyPayBudgetField = new IntegerDataField("TownyPayBudget", 0, "Budget");
    private static final IntegerDataField TownyPaySpendField = new IntegerDataField("TownyPaySpend", 0, "Spend");
    private static final IntegerDataField TownyPaySetField = new IntegerDataField("TownyPaySet", 0, "Pay");
    private static final BooleanDataField TownyPayToggleField = new BooleanDataField("TownyPayToggle", true, "Pay Toggle");
    private static final IntegerDataField TownyPaySavingsField = new IntegerDataField("TownyPaySavings", 0, "Savings");
    private static final IntegerDataField TownyPayHoldingsField = new IntegerDataField("TownyPayHoldings", 0, "Holdings");

    public static void loadCustomDataField(){
        // (Optional) Try to globally register the data field.
        // Globally registering data fields allow them to be modified in-game by administrators.
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPayBudgetField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPaySpendField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPaySetField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPayToggleField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPaySavingsField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }
        try {
            TownyAPI.getInstance().registerCustomDataField(TownyPayHoldingsField);
        } catch (KeyAlreadyRegisteredException e) {
            getLogger().warning(e.getMessage()); // A flag with the same key name already exists try again
        }

        getLogger().info("Custom data fields successfully registered!");
    }
}
