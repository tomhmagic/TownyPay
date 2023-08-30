package com.tanukicraft.townypay.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.metadata.KingPayMetaDataController;
import com.tanukicraft.townypay.settings.NationSettings;
import com.tanukicraft.townypay.util.GeneralUtil;
import com.tanukicraft.townypay.util.TownyPayMessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NationPaySetAddon extends BaseCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        if (NationSettings.canSetPay()) {
            Resident res = TownyAPI.getInstance().getResident((Player) commandSender);

            if (res.hasPermissionNode("townypay.command.nation.setpay")) {
                if (strings.length == 1) { //check value was given
                    if (GeneralUtil.isNotInteger(strings[0])){
                        TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.ValueError"));
                    } else {
                        //Check given value is between the set min and max
                        Integer value = Integer.parseInt(strings[0]);
                        Integer min = NationSettings.getPayMin();
                        Integer max = NationSettings.getPayMax();
                        Nation nation;
                        try {
                            nation = res.getNation();
                        } catch (TownyException e) {
                            throw new RuntimeException(e);
                        }

                        if (!(value >= min && value <= max)) {
                            TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.SetCommandFail", min, max));
                        } else {
                            //Command Logic
                            if (nation != null) {
                                KingPayMetaDataController.setPayData(nation, value);
                                TownyPayMessageUtil.sendMsg(commandSender,Translatable.of("townypay.nation.PaySet", value));
                            }
                        }
                    }
                } else { //syntax error
                    TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.nation.CommandFail.set"));
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
