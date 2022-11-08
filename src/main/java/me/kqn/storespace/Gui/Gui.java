package me.kqn.storespace.Gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.MasonryPane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import me.kqn.storespace.Config.PageConfig;
import me.kqn.storespace.Data.PlayerData;
import me.kqn.storespace.Data.StorePage;
import me.kqn.storespace.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public class Gui {
    Player player;
    PlayerData pData;

    public Gui(Player player){
        this.player=player;
        this.pData=PlayerData.getPlayerData(player);
    }

    public void show(){
        PaginatedPane pagePane=new PaginatedPane(8,6);
       // StaticPane[] pages=new StaticPane[pData.storePages.length];
        //读取pData到gui界面，没有对pData进行任何写操作
        for (int i = 0; i < pData.storePages.length; i++) {
            StorePage storePage=pData.storePages[i];
            StaticPane page=new StaticPane(8,6);
            for(int j=0;j<storePage.contents.length;j++){
                if(storePage.contents[j]!=null&&storePage.contents[j].getType()!= Material.AIR){
                    page.addItem(new GuiItem(storePage.contents[j]),j%8,j/8);
                }
            }
            int x_lock= storePage.amount_unlock%8;
            int y_lock=storePage.amount_unlock/8;
            for(;y_lock<6;y_lock++){
                for(;x_lock<8;x_lock++){
                    page.addItem(new GuiItem(new ItemBuilder(
                            Material.PAPER
                    ).build(),x->{x.setCancelled(true);player.sendMessage("未解锁");}),x_lock,y_lock);

                }
            }
            pagePane.addPane(i,page);
        }
        ChestGui gui=new ChestGui(6,"储存空间");
        gui.setOnClose(x->CallonClose(gui,x));
        gui.addPane(pagePane);
        gui.show(player);
    }
    //在gui关闭时，保存数据到pData
    private void CallonClose(ChestGui gui,InventoryCloseEvent event){
        PaginatedPane pagePane=(PaginatedPane) gui.getPanes().get(0);
        for (int i = 0; i < pagePane.getPanes().size(); i++) {
           StaticPane page= (StaticPane) pagePane.getPanes(i);
           StorePage storePage=pData.storePages[i];
           storePage.contents=new ItemStack[storePage.amount_unlock];
            GuiItem[] storItem=new GuiItem[page.getItems().size()];
            page.getItems().toArray(storItem);
           for(int j=0;j<storePage.amount_unlock;j++){
              storePage.contents[j]=storItem[j].getItem().clone();
           }
        }
    }
}
