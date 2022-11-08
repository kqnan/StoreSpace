package me.kqn.storespace;

import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;

import me.kqn.storespace.Command.MainCommand;
import me.kqn.storespace.Data.PlayerData;
import me.kqn.storespace.Gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class StoreSpace extends JavaPlugin {

    @Override
    public void onEnable() {
        ChestGui gui=new ChestGui(6,"asd");
        OutlinePane pane=new OutlinePane(9,6);
        pane.setOnClick(x->{
            System.out.println("click");
        });
        gui.addPane(pane);
        gui.setOnClose(x->{
            gui.getPanes().get(0).getItems().forEach(y-> System.out.println(y.getItem()));
        });
        gui.show(Bukkit.getPlayer("Kurt_Kong"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
