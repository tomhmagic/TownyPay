package com.tanukicraft.townypay.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.metadata.TownBudgetMetaDataController;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.tanukicraft.townypay.util.TownyPayMessageUtil;

public class TownFinanceAddon implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        Resident res = TownyAPI.getInstance().getResident((Player) commandSender);

        if (res.hasPermissionNode("townypay.command.town.finance")){
            Town town;
            try {
                town = res.getTown();
            } catch (NotRegisteredException e) {
                throw new RuntimeException(e);
            }
            int budget = TownBudgetMetaDataController.getBudgetData(town);
            int spend = TownBudgetMetaDataController.getSpendData(town);
            int remaining = budget - spend;
            TownyPayMessageUtil.sendMsg(commandSender,Translatable.of("townypay.general.finance", budget, spend, remaining));
            return true;
        } else {
            TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NoPermission"));
            return false;
        }
    }
}
