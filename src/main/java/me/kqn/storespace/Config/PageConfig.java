package me.kqn.storespace.Config;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PageConfig {
    public static ArrayList<String> TitlePages=new ArrayList<>();
    public static ArrayList<ArrayList<Icon>> unlockedIcon=new ArrayList<>();
    public static ArrayList<ArrayList<Icon>> prePageIcon=new ArrayList<>();
    public static ArrayList<ArrayList<Icon>> nextPageIcon=new ArrayList<>();
    public static ArrayList<ArrayList<Icon>> slidePageIcon=new ArrayList<>();

    public static Icon getUnlockIcon(int UnlockPages,int pageID){
        Icon icon=new Icon();
        icon.name="未解锁";

        icon.lore=Arrays.asList("1","2");
        icon.custommodeldata=0;
        icon.material=Material.PAPER;
        return icon;
    }
    public static Icon getPreIcon(int UnlockPages,int pageID){
        Icon icon=new Icon();
        icon.name="上一页";
        icon.lore=Arrays.asList("1","2");
        icon.custommodeldata=0;
        icon.material=Material.GRASS_BLOCK;
        return icon;
    }
    public static Icon getNextIcon(int UnlockPages,int pageID){
        Icon icon=new Icon();
        icon.name="下一页";
        icon.lore= Arrays.asList("1","2");
        icon.custommodeldata=0;
        icon.material=Material.GRASS_BLOCK;
        return icon;
    }
    public static Icon getSlideIcon(int UnlockPages,int pageID){
        Icon icon=new Icon();
        icon.name="未解锁";
        icon.lore= Arrays.asList("1","2");
        icon.custommodeldata=0;
        icon.material=Material.ACACIA_LEAVES;
        return icon;
    }
    public  static class Icon{
        public String name;
        public List<String> lore;
        public    int custommodeldata;
        public   Material material;
    }
}
