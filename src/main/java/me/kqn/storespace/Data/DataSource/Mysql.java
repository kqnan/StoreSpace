package me.kqn.storespace.Data.DataSource;


import me.kqn.storespace.Config.Config;
import me.kqn.storespace.Data.PlayerData;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Map;
import java.util.UUID;

public class Mysql implements DataSource {
    private String username;
    private String userpw;
    private String url;
    private String database;
    public Mysql(){
        this.username= Config.getMysql_username();
        this.userpw=Config.getMysql_password();
        this.database=Config.getMysql_database();
        this.url="jdbc:mysql://"+Config.getMysql_host()+":"+Config.getMysql_port()+"/"+database+"?autoReconnect=true";
        
        Connection connection= DriverManager.getConnection(url,username,userpw);
    }

    @Override
    public PlayerData readToPlayerData(UUID uuid) {
        return null;
    }

    @Override
    public void write(PlayerData playerData,UUID uuid) {

    }
}
