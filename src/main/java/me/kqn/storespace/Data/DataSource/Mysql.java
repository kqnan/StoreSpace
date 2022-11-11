package me.kqn.storespace.Data.DataSource;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import me.kqn.storespace.Config.Config;
import me.kqn.storespace.Data.PlayerData;

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
    public Mysql(){
        this.username= Config.getMysql_username();
        this.userpw=Config.getMysql_password();
        this.database=Config.getMysql_database();
        this.url="jdbc:mysql://"+Config.getMysql_host()+":"+Config.getMysql_port()+"/"+database+"?autoReconnect=true";
        System.out.println(this.url);
        try {
            connection= DriverManager.getConnection(url,username,userpw);
           createTable();
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
            }
            statement.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        String json=DataSource.Gunzip(bytes);
        System.out.println(json);
        System.out.println(json.length());
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
}
