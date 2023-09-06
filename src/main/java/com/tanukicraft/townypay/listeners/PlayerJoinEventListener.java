package com.tanukicraft.townypay.listeners;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.Resident;
import com.palmergames.bukkit.towny.object.Translatable;
import com.tanukicraft.townypay.util.MessageCacheUtil;
import com.tanukicraft.townypay.util.MessageUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {

    @EventHandler

    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        Resident resident = TownyAPI.getInstance().getResident(player);

        assert resident != null;
        if (MessageCacheUtil.hasMsgCache(resident)){
            MessageUtil.sendMsg(player, Translatable.of("townypay.general.OfflineMsg"));

            MessageCacheUtil.sendMsgCache(resident);
        }
    }
}
