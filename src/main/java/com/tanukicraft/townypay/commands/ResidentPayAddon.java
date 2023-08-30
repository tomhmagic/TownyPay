package com.tanukicraft.townypay.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.settings.ResidentSettings;
import com.tanukicraft.townypay.util.GeneralUtil;
import com.tanukicraft.townypay.util.TownyPayMessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ResidentPayAddon extends BaseCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings){
        Resident res = TownyAPI.getInstance().getResident((Player) commandSender);
        if (res.hasPermissionNode("townypay.command.resident.pay")) { //check for perm

            String target = null;
            try {
                target = TownyAPI.getInstance().getResident(strings[0]).toString();
            } catch (Exception e) {
            }
            if(strings.length == 2){ //check player and value was given
                if (target == null) { //if no valid player was given
                    TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.PlayerNotFound"));
                    return false;
                } else if (GeneralUtil.isNotInteger(strings[1])){ //check value is an int
                    TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.ValueError"));
                    return false;

                } else { //pay target checks

                    Resident targetRes = TownyAPI.getInstance().getResident(target);
                    Resident senderRes = TownyAPI.getInstance().getResident((Player) commandSender);


                    if (targetRes == senderRes){ //stop payments to self
                        TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.PayCommandFail.Self"));
                        return false;
                    } else {
                        //main logic
                        int pay = Integer.parseInt(strings[1]);

                        if (pay > res.getAccount().getHoldingBalance()){ //if the pay amount is more than the players balance
                            TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NotEnoughMoney"));
                            return false;
                        } else { //pay given player

                            Town targetTown = GeneralUtil.getTown(targetRes);
                            Town senderTown = GeneralUtil.getTown(senderRes);
                            Nation targetNation = GeneralUtil.getNation(targetRes);
                            Nation senderNation = GeneralUtil.getNation(senderRes);

                            double tax = ResidentSettings.getOutsiderTax();
                            if(targetTown == null | targetNation == null) { //if not part of town or nation
                                if (GeneralUtil.isSameTown(targetTown, senderTown) | GeneralUtil.isSameNation(targetNation, senderNation)){
                                    tax = ResidentSettings.getResidentTax();
                                }
                            }

                            double calcTax = (pay / 100) * tax;

                            //send money
                            senderRes.getAccount().payTo(pay, targetRes.getAccount(), String.valueOf(Translatable.of("townypay.Resident.PaymentReason")));

                            //remove the tax from the player
                            targetRes.getAccount().withdraw(calcTax, String.valueOf(Translatable.of("townypay.Resident.PaymentTaxReason")));
                            //message the player
                            Player targetPlayer = targetRes.getPlayer();
                            TownyPayMessageUtil.sendPlayerMsg(targetPlayer,Translatable.of("townypay.general.PaymentReceived",senderRes, pay, tax));
                            //message the sender
                            TownyPayMessageUtil.sendMsg(commandSender,Translatable.of("townypay.general.PaymentSend", targetRes, pay));

                        }
                    }
                }
            } else { //syntax error
                TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.Resident.CommandFail.pay"));
                return false;
            }

        }else { //no perms
            TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NoPermission"));
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
