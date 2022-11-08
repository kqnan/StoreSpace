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
            PlayerData playerData=new PlayerData();
            playerData.storePages[0]=new StorePage();
            pDatas.put(uuid,playerData);

        }
        return pDatas.get(uuid);
    }
    public StorePage[] storePages=new StorePage[1];

}
