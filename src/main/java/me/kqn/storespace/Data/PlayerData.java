package me.kqn.storespace.Data;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData {
    private static HashMap<UUID,PlayerData> pDatas=new HashMap<>();
    public static PlayerData getPlayerData(Player player){
        return getPlayerData(player.getUniqueId());
    }
    public static PlayerData getPlayerData(UUID uuid){
        if(!pDatas.containsKey(uuid)){
            pDatas.put(uuid,new PlayerData());
        }
        return pDatas.get(uuid);
    }
    StorePage[] storePages=null;

}
