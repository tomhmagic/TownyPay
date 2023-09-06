package com.tanukicraft.townypay.util;

import com.palmergames.adventure.text.Component;
import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.bukkit.Bukkit.getServer;

public class MessageUtil {
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

    public static void sendResidentMsg(Resident resident, Translatable message) {
        assert resident != null;
        if (resident.isOnline()) {
                TownyMessaging.sendMsg(resident, message);
        } else {
            // Get the current date and time
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Define a custom date and time format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Format the date as a string
            String formattedDate = currentDateTime.format(formatter);

            MessageCacheUtil.cacheMsg(resident, "[" + formattedDate + "] " + message);
        }
    }


    public static void playerDebug(Player player, String message){
        player.sendMessage(ChatColor.YELLOW + "DEBUG: " + message);
    }
    public static void consoleDebug(String message){
        getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "DEBUG: " + message);
    }
}
