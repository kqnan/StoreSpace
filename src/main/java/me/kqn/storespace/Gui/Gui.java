package me.kqn.storespace.Gui;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.MasonryPane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;
import com.github.stefvanschie.inventoryframework.pane.component.Slider;
import me.kqn.storespace.Config.PageConfig;
import me.kqn.storespace.Data.PlayerData;
import me.kqn.storespace.Data.StorePage;
import me.kqn.storespace.Utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
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

    public void show(){
        ChestGui gui=new ChestGui(6,"储存空间");
        PaginatedPane pagePane=new PaginatedPane(8,6);
       // StaticPane[] pages=new StaticPane[pData.storePages.length];
        //读取pData到gui界面，没有对pData进行任何写操作
        //创建窗口主体
        for (int i = 0; i < pData.storePages.length; i++) {
            StorePage storePage=pData.storePages[i];
            StaticPane page=new StaticPane(8,6);
            for(int j=0;j<storePage.contents.length;j++){
                if(storePage.contents[j]!=null&&storePage.contents[j].getType()!= Material.AIR){
                    page.addItem(new GuiItem(storePage.contents[j]),j%8,j/8);
                }
            }

            GenerateUnlockIcon(storePage,page,i);
            pagePane.addPane(i,page);
        }
        //创建右边滑块
        PaginatedPane slider=new PaginatedPane(8,0,1,6);
        for(int i=0;i<pData.storePages.length;i++){
            StaticPane spane=new StaticPane(1,6);
            //下一页按钮
            spane.addItem(new GuiItem(preIcon(i),x->{if(page_current-1>=0){slider.setPage(page_current-1);
            pagePane.setPage(page_current-1);page_current--;gui.update();}x.setCancelled(true);}),0,0);
            //上一页按钮
            spane.addItem(new GuiItem(nextIcon(i),x->{if(page_current+1<pData.storePages.length){
                pagePane.setPage(page_current+1);slider.setPage(page_current+1);page_current++;
            }x.setCancelled(true);gui.update();}),0,5);

            float percent=(float)(i+1)/(float)pData.storePages.length;

            int slidepos=(int)(4.0*percent);

            if(slidepos==0)slidepos=1;
            //滑块
            spane.addItem(new GuiItem(slideIcon(i),x->x.setCancelled(true)),0,slidepos);
            slider.addPane(i,spane);
        }


        gui.setOnClose(x->CallonClose(gui,x));
        gui.addPane(slider);
        gui.addPane(pagePane);
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
    private void CallonClose(ChestGui gui,InventoryCloseEvent event){
        PaginatedPane pagePane=(PaginatedPane) gui.getPanes().get(0);
        Iterator<Pane> iterator= pagePane.getPanes().iterator();
        for (int i = 0; i < pagePane.getPanes().size(); i++) {
           StaticPane page= (StaticPane) iterator.next();
           StorePage storePage=pData.storePages[i];



           storePage.contents=new ItemStack[storePage.amount_unlock];

            GuiItem[] pageItem=new GuiItem[page.getItems().size()];
            page.getItems().toArray(pageItem);

        }
    }
}
