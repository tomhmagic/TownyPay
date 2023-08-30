package com.tanukicraft.townypay.util;

import com.palmergames.adventure.text.Component;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.Bukkit.getServer;

public class TownyPayMessageUtil {
    public static void logStatus(Translatable message){
        getServer().getConsoleSender().sendMessage(String.valueOf(Translatable.of("townypay.plugin_prefix").append(Component.text(""))) + ChatColor.WHITE + message);
    }
    public static void logError(Translatable message){
        getServer().getConsoleSender().sendMessage(String.valueOf(Translatable.of("townypay.plugin_prefix").append(Component.text(""))) + ChatColor.RED + message);
    }

    public static void sendErrorMsg(CommandSender sender, Translatable message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if (sender != null) {
            TownyMessaging.sendErrorMsg(sender, message);
        }
    }

    public static void sendMsg(CommandSender sender, Translatable message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if (sender != null) {
            TownyMessaging.sendMsg(sender, message);
        }
    }

    public static void sendPlayerMsg(Player player, Translatable message) {
        if (player != null) {
            Resident res = TownyAPI.getInstance().getResident(player);
            TownyMessaging.sendMsg(res, message);
        }
    }
}
