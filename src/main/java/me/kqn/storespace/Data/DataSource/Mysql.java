package me.kqn.storespace.Data.DataSource;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import me.kqn.storespace.Config.Config;
import me.kqn.storespace.Data.PlayerData;
import me.kqn.storespace.StoreSpace;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Map;
import java.util.UUID;

public class Mysql implements DataSource {
    private static String SAVE = "INSERT INTO StoreSpace(UUID,Data) VALUE(?,?) ON DUPLICATE KEY UPDATE Data=?";
    private String username;
    private String userpw;
    private String url;
    private String database;
    private Connection connection;
    private BukkitTask timer;
    public Mysql(){
        this.username= Config.getMysql_username();
        this.userpw=Config.getMysql_password();
        this.database=Config.getMysql_database();
        this.url="jdbc:mysql://"+Config.getMysql_host()+":"+Config.getMysql_port()+"/"+database+"?autoReconnect=true";

        try {
            connection= DriverManager.getConnection(url,username,userpw);
            Bukkit.getScheduler().runTaskAsynchronously(StoreSpace.plugin,this::createTable);
            //每5分钟发送一次请求，确保连接不超时
            timer=Bukkit.getScheduler().runTaskTimerAsynchronously(StoreSpace.plugin,()->{
                String SELECT = "SELECT * FROM StoreSpace WHERE UUID=123";
                try {
                    PreparedStatement statement = connection.prepareStatement(SELECT);
                    ResultSet resultSet= statement.executeQuery();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            },6000,6000);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

        private void createTable(){

            Statement statement = null;
            try {
                statement = connection.createStatement();
                statement.executeUpdate("CREATE TABLE IF NOT EXISTS StoreSpace(UUID varchar(100) primary key,Data blob);");
                statement.executeUpdate("ALTER TABLE StoreSpace MODIFY Data blob;");
                statement.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        }
    @Override
    public PlayerData readToPlayerData(UUID uuid) {
        String SELECT = "SELECT * FROM StoreSpace WHERE UUID=?";
        byte[] bytes=new byte[0];
        try {
            PreparedStatement statement = connection.prepareStatement(SELECT);
            statement.setString(1,uuid.toString());
            ResultSet result=  statement.executeQuery();
            if(result.next()){
              bytes=  result.getBytes("Data");
              statement.close();
            }
            else {//如果查询不到
              statement.close();
              return new PlayerData(uuid);
            }

        }catch (Exception e){
            e.printStackTrace();
            return new PlayerData(uuid);
        }
        String json=DataSource.Gunzip(bytes);

        Gson gson=new Gson();
        JsonArray jsonArray=gson.fromJson(json, JsonArray.class);
        return PlayerData.fromJson(jsonArray);
    }

    @Override
    public void write(PlayerData playerData,UUID uuid) {
        String json=PlayerData.toJson(playerData).toString();
        byte[] bytes=DataSource.Gzip(json);
        try {
            PreparedStatement statement = connection.prepareStatement(SAVE);
            statement.setString(1,uuid.toString());
            statement.setBytes(2, bytes);
            statement.setBytes(3,bytes);
            statement.execute();
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void onDisable(){
        if(this.timer!=null)this.timer.cancel();
        Map<UUID,PlayerData> dataMap=PlayerData.getData();
        for (UUID uuid : dataMap.keySet()) {
            write(dataMap.get(uuid),uuid);
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
