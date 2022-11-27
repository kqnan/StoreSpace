package me.kqn.storespace.Config;

import me.kqn.storespace.StoreSpace;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class PageConfig {
    public static String getMoeny_unlock(int slot) {
        return moeny_unlock.replace("%slot%",String.valueOf(slot));
    }

    public static String getPerm_unlock(int slot) {
        for (Pair pair : permissions.keySet()) {
            if(pair.p1<=slot&&slot<=pair.p2){
                return permissions.get(pair);
            }
        }
        return null;
    }

    public static List<String> getMsg_noperm(int slot,String permission) {
        List<String> msg=new ArrayList<>();
        for (String s : msg_noperm) {
            msg.add(s.replace("%slot%",String.valueOf(slot)).replace("%permission%",permission));
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

    public static List<String> getMsg_nomoney(int slot,double money) {
        List<String> msg=new ArrayList<>();
        for (String s : msg_nomoney) {
            msg.add(s.replace("%slot%",String.valueOf(slot)).replace("%money%",String.valueOf(money)));
        }
        return msg;
    }

    private static String moeny_unlock;
    private static  List<String> perm_unlock;
    private static List<String> msg_noperm;
    private static Sound unlock_sound;
    private static  Sound noperm_sound;
    private static Sound page_sound;
    private static List<String> msg_nomoney;
    private static YamlConfiguration file;
    private static HashMap<Pair,String> permissions;
    public static void read(){
        try {
            StoreSpace.plugin.saveResource("PageConfig.yml",false);
            file=YamlConfiguration.loadConfiguration(new File("plugins\\StoreSpace\\PageConfig.yml"));
            moeny_unlock=file.getString("money_unlock");
            perm_unlock=file.getStringList("perm_unlock");
            msg_noperm=file.getStringList("msg_noperm");
            parsePermission();
            unlock_sound=Sound.valueOf(file.getString("sound_unlock").toUpperCase());
            noperm_sound= Sound.valueOf(file.getString("sound_noperm").toUpperCase());
            page_sound=Sound.valueOf(file.getString("sound_page").toUpperCase());
            msg_nomoney=file.getStringList("msg_nomoney");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static void parsePermission(){
        permissions=new HashMap<>();
        for (String s : perm_unlock) {
            String perm=s.split(":")[1];
            String range=s.split(":")[0];
            int lo=Integer.parseInt(range.split("-")[0]);
            int hi=Integer.parseInt(range.split("-")[1]);
            Pair pair=new Pair(lo,hi);
            permissions.put(pair,perm);

        }
    }
    private static class  Pair{
        public int p1;
        public int p2;
        public Pair(int pa1,int pa2){
            p1=pa1;p2=pa2;
        }

        @Override
        public int hashCode() {
            return p1*7+p2*31;
        }
    }
}
