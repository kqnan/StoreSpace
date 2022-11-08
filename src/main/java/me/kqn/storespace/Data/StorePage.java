package me.kqn.storespace.Data;

import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class StorePage {
    public int amount_unlock=1;
    public boolean[] unlocked=null;
    public ItemStack[] contents=null;
    public StorePage(){
        unlocked=new boolean[48];
        Arrays.fill(unlocked, false);
        for(int i=0;i<7;i++)unlocked[i]=true;
        contents=new ItemStack[amount_unlock];
    }
}
