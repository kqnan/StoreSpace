package me.kqn.storespace.Data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import de.tr7zw.changeme.nbtapi.NBTContainer;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.kqn.storespace.Config.Config;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class PlayerData   {
    // TODO: 2022/11/10 多线程问题，storepages 
    private static HashMap<UUID,PlayerData> pDatas=new HashMap<>();
    public static PlayerData getPlayerData(Player player){
        return getPlayerData(((OfflinePlayer)player).getUniqueId());
    }

    public static PlayerData getPlayerData(UUID uuid){
        if(!pDatas.containsKey(uuid)){
            PlayerData playerData=new PlayerData();
            playerData.storePages[0]=new StorePage(uuid);
            pDatas.put(uuid,playerData);

        }
        return pDatas.get(uuid);
    }



    public static void setData(UUID player,PlayerData playerData) {
        pDatas.put(player,playerData);
    }
    public PlayerData (){

    }
    public StorePage[] storePages=new StorePage[1];
    public static void addPage(UUID pID){
        PlayerData playerData=pDatas.get(pID);
        if(playerData.storePages.length>= Config.getMaxPages())return;
        StorePage[] tmp=new StorePage[playerData.storePages.length+1];
        for (int i = 0; i < playerData.storePages.length; i++) {
            tmp[i]=playerData.storePages[i];
        }
        tmp[playerData.storePages.length]=new StorePage(pID,0);
        playerData.storePages=tmp;
    }
    public static JsonElement toJson(PlayerData playerData){
        JsonArray jsonArray=new JsonArray();
        for (StorePage storePage : playerData.storePages) {
            JsonObject jsonPage=new JsonObject();
            jsonPage.addProperty("pID",storePage.pID.toString());
            jsonPage.addProperty("amount_unlock",storePage.amount_unlock);
            JsonArray unlocked=new JsonArray();
            for (boolean b : storePage.unlocked) {
                unlocked.add(b);
            }
            jsonPage.add("unlocked",unlocked);
            JsonArray contents=new JsonArray();
            for (ItemStack content : storePage.contents) {
                contents.add(NBTItem.convertItemtoNBT(content).toString());
            }
            jsonPage.add("contents",contents);
            jsonArray.add(jsonPage);
        }
        return jsonArray;
    }
    public static PlayerData fromJson(JsonElement jsonElement){

        JsonArray jsonArray=(JsonArray) jsonElement;
        PlayerData playerData=new PlayerData();
        playerData.storePages=new StorePage[jsonArray.size()];
        int j=0;
        for (JsonElement element : jsonArray) {
            JsonObject objPage=(JsonObject)element;
            StorePage page=new StorePage();
            page.pID=UUID.fromString(objPage.get("pID").getAsString());
            page.amount_unlock=objPage.get("amount_unlock").getAsInt();
            page.unlocked=new boolean[page.amount_unlock];
            JsonArray unlock=objPage.getAsJsonArray("unlocked");
            int i=0;
            for (JsonElement jsonElement1 : unlock) {
                page.unlocked[i++]=jsonElement1.getAsBoolean();
            }
            page.contents=new ItemStack[page.amount_unlock];
            JsonArray contents=objPage.getAsJsonArray("contents");
            i=0;
            for (JsonElement content : contents) {
                page.contents[i++]=NBTItem.convertNBTtoItem(new NBTContainer(content.getAsString()));
            }
            playerData.storePages[j++]=page;
        }
        return playerData;
    }
}
