package me.kqn.storespace;

import com.github.stefvanschie.inventoryframework.font.util.Font;
import com.github.stefvanschie.inventoryframework.gui.GuiItem;
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui;
import com.github.stefvanschie.inventoryframework.pane.OutlinePane;
import com.github.stefvanschie.inventoryframework.pane.PaginatedPane;
import com.github.stefvanschie.inventoryframework.pane.Pane;
import com.github.stefvanschie.inventoryframework.pane.StaticPane;

import com.github.stefvanschie.inventoryframework.pane.component.Label;
import me.kqn.storespace.Command.MainCommand;
import me.kqn.storespace.Data.PlayerData;
import me.kqn.storespace.Gui.Gui;
import me.kqn.storespace.Gui.GuiListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class StoreSpace extends JavaPlugin {
public static StoreSpace plugin;
    @Override
    public void onEnable() {
        plugin=this;
        Bukkit.getPluginCommand("StoreSpace").setExecutor(new MainCommand());
        Bukkit.getPluginManager().registerEvents(new GuiListener(),this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
