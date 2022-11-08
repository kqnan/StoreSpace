package me.kqn.storespace.Gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.MasonryPane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.Label;
import com.github.stefvanschie.inventoryframework.pane.component.Slider;
import com.github.stefvanschie.inventoryframework.pane.component.ToggleButton;
import me.kqn.storespace.Config.PageConfig;
import me.kqn.storespace.Data.PlayerData;
import me.kqn.storespace.Data.StorePage;
import me.kqn.storespace.StoreSpace;
import me.kqn.storespace.Utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.DragType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import javax.swing.*;
import java.util.Iterator;

public class Gui {
    Player player;
    PlayerData pData;
    int page_current=0;

    public Gui(Player player){
        this.player=player;
        this.pData=PlayerData.getPlayerData(player);
    }

    public void showPage(int pageID){
        if(pData.storePages.length<=pageID||pageID<0)return;
        ChestGui gui=new ChestGui(6,"储存空间");
        float percent=(float)(pageID +1)/(float)pData.storePages.length;
        int slidepos=(int)(4.0*percent);
        if(slidepos==0)slidepos=1;
        int finalSlidepos = slidepos;
        gui.setOnGlobalClick(x->{
            Bukkit.getScheduler().runTaskLater(StoreSpace.plugin,()->{
                Inventory inv=gui.getInventory();
                if(getInv(inv,8,1)!=null&&!getInv(inv,8,1).getType().isAir()&& finalSlidepos !=1){
                    player.getInventory().addItem(inv.getItem(17));
                    inv.setItem(17,new ItemStack(Material.AIR));
                }
                if(getInv(inv,8,2)!=null&&!getInv(inv,8,2).getType().isAir()&& finalSlidepos !=2){
                    player.getInventory().addItem(inv.getItem(26));
                    inv.setItem(26,new ItemStack(Material.AIR));
                }
                if(getInv(inv,8,3)!=null&&!getInv(inv,8,3).getType().isAir()&& finalSlidepos !=3){
                    player.getInventory().addItem(inv.getItem(35));
                    inv.setItem(35,new ItemStack(Material.AIR));
                }
                if(getInv(inv,8,4)!=null&&!getInv(inv,8,4).getType().isAir()&& finalSlidepos !=4){
                    player.getInventory().addItem(inv.getItem(44));
                    inv.setItem(44,new ItemStack(Material.AIR));
                }
            },1);
        });
        gui.setOnGlobalDrag(x->x.setCancelled(true));
        //读取pData到gui界面，没有对pData进行任何写操作
        //创建窗口主体
        StorePage storePage=pData.storePages[pageID];
            StaticPane page=new StaticPane(8,6);
            for(int j=0;j<storePage.contents.length;j++){
                if(storePage.contents[j]!=null&&storePage.contents[j].getType()!= Material.AIR){
                    page.addItem(new GuiItem(storePage.contents[j]),j%8,j/8);
                }
            }
            GenerateUnlockIcon(storePage,page, pageID);
            gui.addPane(page);
            //创建右边滑块
            StaticPane spane=new StaticPane(8,0,1,6);
            //上一页按钮
            spane.addItem(new GuiItem(preIcon(pageID), x->{if(page_current-1>=0){
               // player.closeInventory();
                page_current--;
                showPage(pageID-1);
            }x.setCancelled(true);}),0,0);
            //下一页按钮
            spane.addItem(new GuiItem(nextIcon(pageID), x->{if(page_current+1<pData.storePages.length){
            //    player.closeInventory();
                page_current++;
                showPage(pageID+1);
            }x.setCancelled(true);}),0,5);




            //滑块
            spane.addItem(new GuiItem(slideIcon(pageID),x->{x.setCancelled(true);}),0,slidepos);
            gui.addPane(spane);
            gui.setOnClose(x->CallonClose(gui,x,page_current));
            gui.show(player);
    }
    private void GenerateUnlockIcon(StorePage storePage,StaticPane page,int pageID){
        int x_lock= storePage.amount_unlock%8;
        int y_lock=storePage.amount_unlock/8;
        for(;y_lock<6;y_lock++){
            for(;x_lock<8;x_lock++){
                page.addItem(new GuiItem(UnlockIcon(pageID),x->{x.setCancelled(true);player.sendMessage("未解锁");}),x_lock,y_lock);

            }
            x_lock=0;
        }
    }
    private ItemStack slideIcon(int pageID){
        PageConfig.Icon icon=PageConfig.getSlideIcon(pData.storePages.length,pageID);
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    private ItemStack preIcon(int pageID){
        PageConfig.Icon icon=PageConfig.getPreIcon(pData.storePages.length,pageID);
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    private ItemStack nextIcon(int pageID){
        PageConfig.Icon icon=PageConfig.getPreIcon(pData.storePages.length,pageID);
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    private ItemStack UnlockIcon(int pageID){

            PageConfig.Icon icon=PageConfig.getUnlockIcon(pData.storePages.length,pageID);
        return new ItemBuilder(icon.material).setLore(icon.lore).setCustomModelData(icon.custommodeldata)
                .setName(icon.name).build();

    }
    //在gui关闭时，保存数据到pData
    private void CallonClose(ChestGui gui,InventoryCloseEvent event,int pageid){
        Inventory pageInv= gui.getInventory();
        StorePage storePage=pData.storePages[pageid];
        storePage.contents=new ItemStack[storePage.amount_unlock];
        for(int i=0;i<storePage.contents.length;i++){
            int x=i%8,y=i/8;
            if(getInv(pageInv,x,y)!=null&&getInv(pageInv,x,y).getType()!=Material.AIR){
                storePage.contents[i]=getInv(pageInv,x,y);
                System.out.println("存储:"+getInv(pageInv,x,y));
            }
        }
    }

    private ItemStack getInv(Inventory inv, int x, int y){
        ItemStack [] itemStacks=inv.getContents();
        return itemStacks[y*9+x];
    }
}
