package com.tanukicraft.townypay.commands;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.command.BaseCommand;
import com.palmergames.bukkit.towny.exceptions.TownyException;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Translatable;
import com.palmergames.bukkit.towny.utils.NameUtil;
import com.tanukicraft.townypay.metadata.MayorPayMetaDataController;
import com.tanukicraft.townypay.settings.TownSettings;
import com.tanukicraft.townypay.util.TownyPayMessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TownPayToggleAddon extends BaseCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (TownSettings.canTogglePay()){
            Resident res = TownyAPI.getInstance().getResident((Player) commandSender);

            if (res.hasPermissionNode("townypay.command.town.togglepay")) {
                if (strings.length == 1 && (strings[0].equals("on") || strings[0].equals("off")))  { //check value was given
                    //Command logic
                    Town town;
                    try {
                        town = res.getTown();
                    } catch (TownyException e) {
                        throw new RuntimeException(e);
                    }
                    String value = strings[0];

                    switch (value) {
                        case "on":
                            MayorPayMetaDataController.setToggleData(town, true);
                            break;
                        case "off":
                            MayorPayMetaDataController.setToggleData(town, false);
                            break;
                        default:
                            System.out.println("Invalid value");
                    }
                    TownyPayMessageUtil.sendMsg(commandSender,Translatable.of("townypay.town.PayToggled", value));

                }else { //syntax error
                    TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.town.CommandFail.toggle"));
                    return false;
                }
            }else { //no perms
                TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.NoPermission"));
                return false;
            }
        }else {
            TownyPayMessageUtil.sendErrorMsg(commandSender, Translatable.of("townypay.general.CommandDisabled"));
            return false;
        }
        return false;
    }
    private static final List<String> townyPayTabCompletes = Arrays.asList("on", "off");
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1)
            return NameUtil.filterByStart(townyPayTabCompletes, args[0]);
        else
            return Collections.emptyList();
    }
}
