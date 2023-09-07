package com.tanukicraft.townypay.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.tanukicraft.townypay.metadata.SavingsMetaDataController;
import com.tanukicraft.townypay.settings.SavingsSettings;
import com.tanukicraft.townypay.util.GeneralUtil;
import com.tanukicraft.townypay.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ResidentSavingsAddon extends BaseCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Resident res = TownyAPI.getInstance().getResident((Player) commandSender);
        assert res != null;
        if (res.hasPermissionNode("townypay.command.resident.savings")) {


            int amount;
            if (strings.length !=0) {
                String arg = strings[0];
                switch (arg) {
                    case "info":
                        sendSavingsInfo(commandSender, res);
                        break;
                    case "deposit":
                        if (!GeneralUtil.isNotInteger(strings[1])) {
                            amount = Integer.parseInt(strings[1]);
                            savingsDeposit(commandSender, res, amount);
                        } else {
                            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.ValueError"));
                        }
                        break;
                    case "withdraw":
                        if (!GeneralUtil.isNotInteger(strings[1])) {
                            amount = Integer.parseInt(strings[1]);
                            savingsWithdraw(commandSender, res, amount);
                        } else {
                            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.ValueError"));
                        }
                        break;
                    default:
                        MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.resident.CommandFail.savings"));
                }
            } else {
                MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.resident.CommandFail.savings"));
            }
        }else{
            //no perms
            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NoPermission"));
            return false;
        }
        return false;
    }

    private void sendSavingsInfo(CommandSender sender, Resident resident){
        int savings = SavingsMetaDataController.getSavingsData(resident);
        int holdings = SavingsMetaDataController.getHoldingsData(resident);

        MessageUtil.sendMsg(sender, Translatable.of("townypay.Resident.Savings.Info", savings, holdings));
    }

    private void savingsDeposit(CommandSender sender, Resident resident, int amount){
        int currentSavings = SavingsMetaDataController.getSavingsData(resident);
        int currentHoldings = SavingsMetaDataController.getHoldingsData(resident);
        int total = currentSavings + currentHoldings;

        //check total savings including holdings is not more than the saving limit
        if (total >= SavingsSettings.getSavingLimitsMax() && SavingsSettings.getSavingLimitsMax() != -1){
            MessageUtil.sendErrorMsg(sender, Translatable.of("townypay.Savings.CommandFail.total", SavingsSettings.getSavingLimitsMax()));
        } else if (amount > SavingsSettings.getHoldingsLimitsMax() && SavingsSettings.getHoldingsLimitsMax() != -1){
            MessageUtil.sendErrorMsg(sender, Translatable.of("townypay.Savings.CommandFail.holdingsmax", SavingsSettings.getHoldingsLimitsMax()));
        } else if (amount < SavingsSettings.getHoldingsLimitsMin()){
            MessageUtil.sendErrorMsg(sender, Translatable.of("townypay.Savings.CommandFail.holdingsmin", SavingsSettings.getHoldingsLimitsMin()));
        } else {
            int newHoldings = currentHoldings + amount;
            SavingsMetaDataController.setHoldingsData(resident,newHoldings);
            resident.getAccount().withdraw(amount, String.valueOf(Translatable.of("townypay.Savings.Deposit")));
            MessageUtil.sendMsg(sender, Translatable.of("townypay.Resident.Savings.Deposit", amount, newHoldings));
        }
    }

    private void savingsWithdraw(CommandSender sender, Resident resident, int amount){
        if (amount <= 0){
            MessageUtil.sendErrorMsg(sender, Translatable.of("townypay.Savings.CommandFail.Withdraw.zero"));
        } else if (amount > SavingsMetaDataController.getSavingsData(resident)) {
            MessageUtil.sendErrorMsg(sender, Translatable.of("townypay.Savings.CommandFail.Withdraw.toomuch"));
        } else{
            int currentSavings = SavingsMetaDataController.getSavingsData(resident);
            int newSavings = currentSavings - amount;

            SavingsMetaDataController.setSavingsData(resident,newSavings);
            resident.getAccount().deposit(amount, String.valueOf(Translatable.of("townypay.Savings.Withdraw")));
            MessageUtil.sendMsg(sender, Translatable.of("townypay.Resident.Savings.Withdraw", amount, newSavings));
        }
    }

    private static final List<String> townyPayTabCompletes = Arrays.asList("info", "deposit", "withdraw");
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return NameUtil.filterByStart(townyPayTabCompletes, args[0]);
        else
            return Collections.emptyList();
    }
}
