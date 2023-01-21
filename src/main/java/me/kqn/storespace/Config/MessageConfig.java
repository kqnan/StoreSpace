package me.kqn.storespace.Config;

import me.kqn.storespace.Data.StorePage;
import me.kqn.storespace.StoreSpace;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class MessageConfig {
    public static String cannot_unlock;
    public static String unlock;
    private static YamlConfiguration file;
    public static  void read(){
        try {
            StoreSpace.plugin.saveResource("Message.yml",false);
            file= YamlConfiguration.loadConfiguration(new File("plugins\\StoreSpace\\Message.yml"));
            cannot_unlock=file.getString("cannot_unlock");
            unlock=file.getString("unlock");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
