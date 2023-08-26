package com.tanukicraft.townypay.util;

import com.palmergames.adventure.text.Component;
import com.palmergames.adventure.text.format.NamedTextColor;
import com.palmergames.bukkit.towny.TownyMessaging;
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
        if(sender != null)
            TownyMessaging.sendMessage(sender, Translatable.of("townypay.plugin_prefix").append(Component.text("", NamedTextColor.RED)).append(message));
    }

    public static void sendMsg(CommandSender sender, Translatable message) {
        //Ensure the sender is not null (i.e. is an online player who is not an npc)
        if(sender != null)
            TownyMessaging.sendMessage(sender, Translatable.of("townypay.plugin_prefix").append(Component.text("", NamedTextColor.WHITE)).append(message));
    }

    public static void sendPlayerMsg(Player player, Translatable message){
        if(player != null)
            TownyMessaging.sendMessage(player, Translatable.of("townypay.plugin_prefix").append(Component.text("", NamedTextColor.WHITE)).append(message));
    }
}
