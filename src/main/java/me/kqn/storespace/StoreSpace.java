package me.kqn.storespace;

import me.kqn.storespace.Command.MainCommand;
import me.kqn.storespace.Config.*;
import me.kqn.storespace.Data.DataSource.DataSource;
import me.kqn.storespace.Data.DataSource.Database;
import me.kqn.storespace.Data.DataSource.JsonFile;
import me.kqn.storespace.Data.DataSource.Mysql;
import me.kqn.storespace.Data.Listeners;
import me.kqn.storespace.Data.PlayerData;
import me.kqn.storespace.Integretion.Drop.DropDefault;
import me.kqn.storespace.Integretion.Drop.DropIntegretion;
import me.kqn.storespace.Integretion.Economy.Economy;
import me.kqn.storespace.Integretion.Economy.GemsEco;
import me.kqn.storespace.Integretion.Economy.Vault;
import me.kqn.storespace.Integretion.Permission.DefaultPerm;
import me.kqn.storespace.Integretion.Permission.Permission;
import me.kqn.storespace.Utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public final class StoreSpace extends JavaPlugin {
public static StoreSpace plugin;
    public Economy economy;
    public Permission permission;
    public DataSource dataSource;
    private BukkitTask autosaveTask;
    public List<DropIntegretion> dropIntegretion;
    //保存数据:自动保存，玩家退出时保存
    //加载数据：玩家进入时保存
    //如果不小心重载了->缓存清除了->玩家退出时但是玩家数据isprepared=false->不会保存
    //自动保存没有考虑isprepared ->不小心重载了会保存空数据
    @Override
    public void onEnable() {
        plugin=this;
        Bukkit.getPluginCommand("StoreSpace").setExecutor(new MainCommand());
        Bukkit.getPluginManager().registerEvents(new Listeners(),this);
        Bukkit.getPluginManager().registerEvents(new DropListener(),this);
        loadConfig();
        initIntegretion();
        
        //设定数据源
        if(Config.getDatasource().equalsIgnoreCase("file")){
            dataSource=new JsonFile();
        }
        else if(Config.getDatasource().equalsIgnoreCase("mysql")){
            dataSource=new Database();
        }
        //在插件开始时异步加载一次玩家数据。
      //  loadPlayerData_Async();
        //开始定时保存
        if(Config.getAutosave_interval()>=1){
             autosaveTask=autoSave((long) Config.getAutosave_interval());
        }

    }
    private BukkitTask autoSave(long interval){
        return Bukkit.getScheduler().runTaskTimerAsynchronously(this,()->{
            Map<UUID,PlayerData> dataMap=PlayerData.getData();
            for (UUID uuid : dataMap.keySet()) {
                if(!dataMap.get(uuid).isPrepared)dataSource.write(dataMap.get(uuid),uuid);
            }
            Msg.Log("[StoreSpace]自动保存");
        },20,interval*60L*20L);
    }

    public void loadConfig(){
        PageIcon.read();
        PageConfig.read();
        MessageConfig.read();
        Config.read();
        DropConfig.read();
        if(Config.getDatasource().equalsIgnoreCase("file")){
            dataSource=new JsonFile();
        }
        else if(Config.getDatasource().equalsIgnoreCase("mysql")){
            dataSource=new Database();
        }
       if(autosaveTask!=null) {
           autosaveTask.cancel();
           autosaveTask=autoSave((long) Config.getAutosave_interval());
       }

    }
    private void loadPlayerData_Async(){
        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
            UUID uuid=offlinePlayer.getUniqueId();
            Bukkit.getScheduler().runTaskAsynchronously(this,()->{
                PlayerData playerData=dataSource.readToPlayerData(uuid);
                playerData.isPrepared=true;
                PlayerData.setData(uuid,playerData);
            });
        }
    }
    public void initIntegretion(){
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")){
            economy=new Vault();
            Msg.Log("Vault挂钩");
        }
        if(Bukkit.getPluginManager().isPluginEnabled("GemsEconomy")){
            economy=new GemsEco();
            Msg.Log("GemsEconomy挂钩");
        }
        permission=new DefaultPerm();
        dropIntegretion=new ArrayList<>();
        dropIntegretion.add(new DropDefault());
    }
    @Override
    public void onDisable() {
        if(autosaveTask!=null)autosaveTask.cancel();
        dataSource.onDisable();
        for (OfflinePlayer operator : Bukkit.getOperators()) {
            if(operator.isOnline()){
                Msg.msg(operator.getUniqueId(),"[StoreSpace]检测到插件关闭，作者不建议热重载或热加载");
            }
        }
        Msg.Log("[StoreSpace]检测到插件关闭，作者不建议热重载或热加载");
    }

}
