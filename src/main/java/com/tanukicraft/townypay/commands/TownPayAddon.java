package com.tanukicraft.townypay.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.metadata.TownBudgetMetaDataController;
import com.tanukicraft.townypay.settings.TownSettings;
import com.tanukicraft.townypay.util.GeneralUtil;
import com.tanukicraft.townypay.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;


public class TownPayAddon extends BaseCommand implements CommandExecutor, TabCompleter {
    
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Resident res = TownyAPI.getInstance().getResident((Player) commandSender);
        if (res.hasPermissionNode("townypay.command.town.pay") & res.hasTown()) { //check for perm

            String target = null;
            try {
                target = TownyAPI.getInstance().getResident(strings[0]).toString();
            } catch (Exception e) {
            }
            if(strings.length == 2){ //check player and value was given
                if (target == null) { //if no valid player was given
                    MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.PlayerNotFound"));
                    return false;
                } else if (GeneralUtil.isNotInteger(strings[1])){ //check value is an int
                    MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.ValueError"));
                    return false;

                } else { //pay target checks

                    Resident targetRes = TownyAPI.getInstance().getResident(target);
                    Resident senderRes = TownyAPI.getInstance().getResident((Player) commandSender);

                    Town targetTown = GeneralUtil.getTown(targetRes);
                    Town senderTown = GeneralUtil.getTown(senderRes);

                    //if no budget data, set 0
                    if (!TownBudgetMetaDataController.hasBudgetData(senderTown)){
                        TownBudgetMetaDataController.setBudgetData(senderTown,0);
                    }
                    //if no spend data, set 0
                    if (!TownBudgetMetaDataController.hasSpendData(senderTown)){
                        TownBudgetMetaDataController.setSpendData(senderTown,0);
                    }

                    if (targetRes == senderRes){ //stop payments to self
                        MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.PayCommandFail.Self"));
                        return false;
                    } else if (targetTown != null & GeneralUtil.isSameTown(targetTown, senderTown) & targetRes.isMayor()) { // stop payments to the town mayor
                        MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.PayCommandFail.Mayor"));
                        return false;
                    } else {
                        //main logic
                        int budget = TownBudgetMetaDataController.getBudgetData(senderTown);
                        int spend = TownBudgetMetaDataController.getSpendData(senderTown);
                        int remaining = budget - spend;
                        int pay = Integer.parseInt(strings[1]);

                        if (pay > budget){ //if pay amount is more than the actual budget
                            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.OverBudget"));
                            return false;
                        } else if (pay > remaining){ //if the pay amount is more than the remaining budget
                            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NotEnoughRemaining"));
                            return false;
                        } else if (pay > senderTown.getAccount().getHoldingBalance()){ //if the pay amount is more than the bank balance
                            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NotEnoughBalance"));
                            return false;
                        } else { //pay given player
                            double tax = TownSettings.getOutsiderTax();
                            if(targetTown != null & GeneralUtil.isSameTown(targetTown, senderTown)) { //if part of town
                                tax = TownSettings.getResidentTax();
                            }
                            
                            double calcTax = (pay / 100) * tax;

                            //remove money from the town bank and send it to the player
                            senderTown.getAccount().payTo(pay, targetRes.getAccount(), String.valueOf(Translatable.of("townypay.town.BudgetPaymentReason")));
                            //remove the tax from the player
                            targetRes.getAccount().withdraw(calcTax, String.valueOf(Translatable.of("townypay.town.BudgetPaymentTaxReason")));
                            //message the player
                            MessageUtil.sendResidentMsg(targetRes,Translatable.of("townypay.general.PaymentReceived",senderTown, pay, tax));
                            //message the sender
                            MessageUtil.sendMsg(commandSender,Translatable.of("townypay.general.PaymentSend", targetRes, pay));

                            //update spend data
                            int updatedSpend = TownBudgetMetaDataController.getSpendData(senderTown) + pay;
                            TownBudgetMetaDataController.setSpendData(senderTown, updatedSpend);
                            MessageUtil.logStatus(Translatable.of("townypay.status.log.town.paymentsent", senderTown, targetRes, pay, tax));
                        }
                    }
                }
            } else { //syntax error
                MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.town.CommandFail.pay"));
                return false;
            }

        }else { //no perms
            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NoPermission"));
            return false;
        }
        return false;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length==0){
            return Collections.emptyList();
        }
        return getTownyStartingWith(args[0], "r");
    }
}
