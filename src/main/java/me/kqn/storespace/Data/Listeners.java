package me.kqn.storespace.Data;

import me.kqn.storespace.StoreSpace;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class Listeners implements Listener {
    @EventHandler
    public void loadOnLogin(PlayerLoginEvent event){
        UUID uuid=((OfflinePlayer)event.getPlayer()).getUniqueId();
        System.out.println(uuid);
        Bukkit.getScheduler().runTaskAsynchronously(StoreSpace.plugin,()-> {
            PlayerData playerData=StoreSpace.plugin.dataSource.readToPlayerData(uuid);//第一次进游戏会read不到，需要给一个默认值
            playerData.isPrepared=true;//设置玩家数据准备好了
            PlayerData.setData(uuid,playerData);//把玩家数据加入到表中。
        });
    }
    @EventHandler
    public void saveOnLogout(PlayerQuitEvent event){
        UUID uuid=((OfflinePlayer)event.getPlayer()).getUniqueId();
        Bukkit.getScheduler().runTaskAsynchronously(StoreSpace.plugin,()->{
           PlayerData playerData=PlayerData.getPlayerData(uuid);//获取玩家的数据
            if(!playerData.isPrepared)return;//查看玩家的数据是否是准备好的,如果是未准备好的数据，说明玩家数据还在加载，这时候不应该保存
            playerData.isPrepared=false;//如果是加载好的数据，把玩家数据设置为不可用状态
            StoreSpace.plugin.dataSource.write(playerData,uuid);//保存玩家的数据
        });
    }
}
