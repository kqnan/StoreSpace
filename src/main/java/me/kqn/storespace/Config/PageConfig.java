package me.kqn.storespace.Config;

import me.kqn.storespace.StoreSpace;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PageConfig {
    public static String getMoeny_unlock(int slot) {
        return moeny_unlock.replace("%slot%",String.valueOf(slot));
    }

    public static String getPerm_unlock(int slot) {
        return perm_unlock.replace("%slot%",String.valueOf(slot));
    }

    public static List<String> getMsg_noperm(int slot) {
        List<String> msg=new ArrayList<>();
        for (String s : msg_noperm) {
            msg.add(s.replace("%slot%",String.valueOf(slot)));
        }
        return msg;
    }

    public static Sound getUnlock_sound() {
        return unlock_sound;
    }

    public static Sound getNoperm_sound() {
        return noperm_sound;
    }

    public static Sound getPage_sound() {
        return page_sound;
    }

    public static List<String> getMsg_nomoney(int slot) {
        List<String> msg=new ArrayList<>();
        for (String s : msg_nomoney) {
            msg.add(s.replace("%slot%",String.valueOf(slot)));
        }
        return msg;
    }

    private static String moeny_unlock;
    private static  String perm_unlock;
    private static List<String> msg_noperm;
    private static Sound unlock_sound;
    private static  Sound noperm_sound;
    private static Sound page_sound;
    private static List<String> msg_nomoney;
    private static YamlConfiguration file;
    public static void read(){
        try {
            StoreSpace.plugin.saveResource("PageConfig.yml",false);
            file=YamlConfiguration.loadConfiguration(new File("plugins\\StoreSpace\\PageConfig.yml"));
            moeny_unlock=file.getString("money_unlock");
            perm_unlock=file.getString("perm_unlock");
            msg_noperm=file.getStringList("msg_noperm");
            unlock_sound=Sound.valueOf(file.getString("sound_unlock").toUpperCase());
            noperm_sound= Sound.valueOf(file.getString("sound_noperm").toUpperCase());
            page_sound=Sound.valueOf(file.getString("sound_page").toUpperCase());
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
}
