package com.tanukicraft.townypay.util;

import com.palmergames.bukkit.towny.TownyMessaging;
import com.palmergames.bukkit.towny.object.Resident;
import com.tanukicraft.townypay.settings.MessageCacheSettings;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MessageCacheUtil {


    public static void cacheMsg(Resident resident, String message) {
        List<String> cache = new ArrayList<>();
        String resID = String.valueOf(resident.getUUID());

        boolean blockMsg = false;
        if (hasMsgCache(resident)) {
            cache = CustomConfig.get().getStringList(resID);
            if (MessageCacheSettings.getMaxCache() != -1) {
                if (cache.size() == MessageCacheSettings.getMaxCache()) {
                    switch (Objects.requireNonNull(MessageCacheSettings.getExtraMSG())) {
                        case "FIRST":
                            cache.remove(0);
                            break;
                        case "LAST":
                            cache.remove(cache.size() - 1);
                            break;
                        case "BLOCK":
                            blockMsg = true;
                            break;
                        case "default":
                            break;
                    }
                }
            }
        }
        if (!blockMsg) {
            cache.add(message);
            CustomConfig.get().set(resID,cache);
            CustomConfig.save();
        }
    }
    public static void sendMsgCache(Resident resident){
        String resID = String.valueOf(resident.getUUID());
        List<String> msgCache = CustomConfig.get().getStringList(resID);

        for (String msg : msgCache){
            TownyMessaging.sendMsg(resident.getPlayer(), msg);
        }

        clearMsgCache(resident);
    }
    public static void clearMsgCache(Resident resident){
        String resID = String.valueOf(resident.getUUID());
        CustomConfig.get().set(resID,null);
        CustomConfig.save();
    }

    public static boolean hasMsgCache(Resident resident){
        String resID = String.valueOf(resident.getUUID());
        List<String> cache = CustomConfig.get().getStringList(resID);
        return !cache.isEmpty();
    }

}
