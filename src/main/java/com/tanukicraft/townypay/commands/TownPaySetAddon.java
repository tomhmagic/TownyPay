package com.tanukicraft.townypay.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.metadata.MayorPayMetaDataController;
import com.tanukicraft.townypay.settings.TownSettings;
import com.tanukicraft.townypay.util.GeneralUtil;
import com.tanukicraft.townypay.util.TownyPayMessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class TownPaySetAddon extends BaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (TownSettings.canSetPay()) {
            Resident res = TownyAPI.getInstance().getResident((Player) commandSender);

            if (res.hasPermissionNode("townypay.command.town.setpay")) {
                if (strings.length == 1) { //check value was given
                    if (GeneralUtil.isNotInteger(strings[0])){
                        TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.ValueError"));
                    } else {
                        //Check given value is between the set min and max
                        Integer value = Integer.parseInt(strings[0]);
                        Integer min = TownSettings.getPayMin();
                        Integer max = TownSettings.getPayMax();
                        Town town;
                        try {
                            town = res.getTown();
                        } catch (TownyException e) {
                            throw new RuntimeException(e);
                        }

                        if (!(value >= min && value <= max)) {
                            TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.SetCommandFail", min, max));
                        } else {
                            //Command Logic
                            if (town != null) {
                                MayorPayMetaDataController.setPayData(town, value);
                                TownyPayMessageUtil.sendMsg(commandSender,Translatable.of("townypay.town.PaySet", value));
                            }
                        }
                    }
                } else { //syntax error
                    TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.town.CommandFail.set"));
                    return false;
                }
            } else { //no perms
                TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NoPermission"));
                return false;
            }
        } else {
            TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.CommandDisabled"));
            return false;
        }
        return false;
    }

}
