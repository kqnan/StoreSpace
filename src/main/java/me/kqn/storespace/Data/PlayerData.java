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
            playerData.storePages[0]=new StorePage(uuid);
            playerData.storePages[1]=new StorePage(uuid);
            playerData.storePages[2]=new StorePage(uuid);
            pDatas.put(uuid,playerData);

        }
        return pDatas.get(uuid);
    }
    public StorePage[] storePages=new StorePage[3];
    public static void addPage(UUID pID){
        PlayerData playerData=pDatas.get(pID);
        StorePage[] tmp=new StorePage[playerData.storePages.length+1];
        for (int i = 0; i < playerData.storePages.length; i++) {
            tmp[i]=playerData.storePages[i];
        }
        tmp[playerData.storePages.length]=new StorePage(pID);
        playerData.storePages=tmp;
    }
}
