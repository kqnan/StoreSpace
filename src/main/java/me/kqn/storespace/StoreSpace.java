package me.kqn.storespace;

import me.kqn.storespace.Command.MainCommand;
import me.kqn.storespace.Config.Config;
import me.kqn.storespace.Config.MessageConfig;
import me.kqn.storespace.Config.PageConfig;
import me.kqn.storespace.Config.PageIcon;
import me.kqn.storespace.Data.DataSource.DataSource;
import me.kqn.storespace.Data.DataSource.JsonFile;
import me.kqn.storespace.Data.DataSource.Mysql;
import me.kqn.storespace.Integretion.Economy.Economy;
import me.kqn.storespace.Integretion.Economy.Vault;
import me.kqn.storespace.Integretion.Permission.DefaultPerm;
import me.kqn.storespace.Integretion.Permission.Permission;
import me.kqn.storespace.Utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class StoreSpace extends JavaPlugin {
public static StoreSpace plugin;
    public Economy economy;
    public Permission permission;
    public DataSource dataSource;
    @Override
    public void onEnable() {
        plugin=this;
        loadConfig();
        initIntegretion();
        if(Config.getDatasource().equalsIgnoreCase("file")){
            dataSource=new JsonFile();
        }
        else if(Config.getDatasource().equalsIgnoreCase("mysql")){
            dataSource=new Mysql();
        }
        Bukkit.getPluginCommand("StoreSpace").setExecutor(new MainCommand());

    }
    public void loadConfig(){
        PageIcon.read();
        PageConfig.read();
        MessageConfig.read();
        Config.read();
    }
    public void initIntegretion(){
        if(Bukkit.getPluginManager().isPluginEnabled("Vault")){
            economy=new Vault();
            Msg.Log("Vault挂钩");
        }
        permission=new DefaultPerm();
    }
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
