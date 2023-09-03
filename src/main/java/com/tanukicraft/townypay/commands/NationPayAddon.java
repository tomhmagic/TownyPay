package com.tanukicraft.townypay.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.metadata.NationBudgetMetaDataController;
import com.tanukicraft.townypay.settings.NationSettings;
import com.tanukicraft.townypay.util.GeneralUtil;
import com.tanukicraft.townypay.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import java.util.Collections;
import java.util.List;

public class NationPayAddon extends BaseCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Resident res = TownyAPI.getInstance().getResident((Player) commandSender);
        if (res.hasPermissionNode("townypay.command.nation.pay") & res.hasTown()) { //check for perm

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

                    Nation targetNation = GeneralUtil.getNation(targetRes);
                    Nation senderNation = GeneralUtil.getNation(senderRes);

                    //if no budget data, set 0
                    if (!NationBudgetMetaDataController.hasBudgetData(senderNation)){
                        NationBudgetMetaDataController.setBudgetData(senderNation,0);
                    }
                    //if no spend data, set 0
                    if (!NationBudgetMetaDataController.hasSpendData(senderNation)){
                        NationBudgetMetaDataController.setSpendData(senderNation,0);
                    }

                    if (targetRes == senderRes){ //stop payments to self
                        MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.PayCommandFail.Self"));
                        return false;
                    } else if (targetNation != null & GeneralUtil.isSameNation(targetNation, senderNation) & targetRes.isMayor()) { // stop payments to the town mayor
                        MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.PayCommandFail.Mayor"));
                        return false;
                    } else {
                        //main logic
                        int budget = NationBudgetMetaDataController.getBudgetData(senderNation);
                        int spend = NationBudgetMetaDataController.getSpendData(senderNation);
                        int remaining = budget - spend;
                        int pay = Integer.parseInt(strings[1]);

                        if (pay > budget){ //if pay amount is more than the actual budget
                            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.OverBudget"));
                            return false;
                        } else if (pay > remaining){ //if the pay amount is more than the remaining budget
                            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NotEnoughRemaining"));
                            return false;
                        } else if (pay > senderNation.getAccount().getHoldingBalance()){ //if the pay amount is more than the bank balance
                            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NotEnoughBalance"));
                            return false;
                        } else { //pay given player
                            double tax = NationSettings.getOutsiderTax();
                            if(targetNation != null & GeneralUtil.isSameNation(targetNation, senderNation)) { //if part of town
                                tax = NationSettings.getResidentTax();
                            }

                            double calcTax = (pay / 100) * tax;

                            //remove money from the town bank and send it to the player
                            senderNation.getAccount().payTo(pay, targetRes.getAccount(), String.valueOf(Translatable.of("townypay.nation.BudgetPaymentReason")));
                            //remove the tax from the player
                            targetRes.getAccount().withdraw(calcTax, String.valueOf(Translatable.of("townypay.nation.BudgetPaymentTaxReason")));
                            //message the player
                            Player targetPlayer = targetRes.getPlayer();
                            MessageUtil.sendPlayerMsg(targetPlayer,Translatable.of("townypay.general.PaymentReceived",senderNation, pay, tax));
                            //message the sender
                            MessageUtil.sendMsg(commandSender,Translatable.of("townypay.general.PaymentSend", targetRes, pay));

                            //update spend data
                            int updatedSpend = NationBudgetMetaDataController.getSpendData(senderNation) + pay;
                            NationBudgetMetaDataController.setSpendData(senderNation, updatedSpend);
                            MessageUtil.logStatus(Translatable.of("townypay.status.log.nation.paymentsent", senderNation, targetRes, pay, tax));
                        }
                    }
                }
            } else { //syntax error
                MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.nation.CommandFail.pay"));
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
