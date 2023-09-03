package com.tanukicraft.townypay.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.metadata.NationBudgetMetaDataController;
import com.tanukicraft.townypay.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class NationFinanceAddon implements CommandExecutor {


    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {

        Resident res = TownyAPI.getInstance().getResident((Player) commandSender);

        if (res.hasPermissionNode("townypay.command.nation.finance") & res.hasNation()){
            Nation nation;
            try {
                nation = res.getNation();
            } catch (TownyException e) {
                throw new RuntimeException(e);
            }
            int budget = NationBudgetMetaDataController.getBudgetData(nation);
            int spend = NationBudgetMetaDataController.getSpendData(nation);
            int remaining = budget - spend;
            MessageUtil.sendMsg(commandSender, Translatable.of("townypay.general.finance", budget, spend, remaining));
            return true;
        } else {
            MessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NoPermission"));
            return false;
        }
    }
}
