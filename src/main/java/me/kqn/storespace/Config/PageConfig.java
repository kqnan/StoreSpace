package me.kqn.storespace.Config;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class PageConfig {
    public static ArrayList<String> TitlePages=new ArrayList<>();
    public static ArrayList<ArrayList<Icon>> unlockedIcon=new ArrayList<>();
    public static ArrayList<ArrayList<Icon>> prePageIcon=new ArrayList<>();
    public static ArrayList<ArrayList<Icon>> nextPageIcon=new ArrayList<>();
    public static ArrayList<ArrayList<Icon>> slidePageIcon=new ArrayList<>();


    private static class Icon{
        String name;
        ArrayList<String> lore;
        int custommodeldata;
        Material material;
    }
}
