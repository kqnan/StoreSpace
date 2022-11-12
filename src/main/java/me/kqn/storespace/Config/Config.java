package me.kqn.storespace.Config;

import me.kqn.storespace.StoreSpace;
import me.kqn.storespace.Utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class Config {

    private static String datasource;
    private static int maxPages;
    private static YamlConfiguration file;

    public static int getMaxPages() {
        return maxPages;
    }

    public static String getMysql_host() {
        return mysql_host;
    }

    public static String getMysql_port() {
        return mysql_port;
    }

    public static String getMysql_database() {
        return mysql_database;
    }

    public static String getMysql_username() {
        return mysql_username;
    }

    public static String getMysql_password() {
        return mysql_password;
    }

    public static double getAutosave_interval() {
        return autosave_interval;
    }

    private static double autosave_interval;
    private static String mysql_host;
    private static String mysql_port;
    private static String mysql_database;
    private static String mysql_username;
    private static String mysql_password;

    public static String getDatasource() {
        if(datasource==null){
            return "file";
        }
        return datasource;
    }

    public static void read() {
        try {
            StoreSpace.plugin.saveResource("Config.yml", false);
            file = YamlConfiguration.loadConfiguration(new File("plugins\\StoreSpace\\Config.yml"));
            datasource = file.getString("datasource");
            maxPages=Integer.parseInt(file.getString("maxPages"));
            mysql_host=file.getString("mysql.host");
            mysql_port=file.getString("mysql.port");
            mysql_database=file.getString("mysql.database");
            mysql_username=file.getString("mysql.username");
            mysql_password=file.getString("mysql.password");
            autosave_interval=Double.parseDouble(file.getString("autosave.interval_minutes"));
        } catch (Exception e) {
            Msg.Log("[StoreSpace]读取Config.yml时出错，启用内置默认值");
            e.printStackTrace();
            maxPages=3;
            autosave_interval=1;
        }
    }
}