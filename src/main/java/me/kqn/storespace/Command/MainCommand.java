package me.kqn.storespace.Command;

import me.kqn.storespace.Gui.Gui;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Gui gui=new Gui(Bukkit.getPlayerExact("Kurt_Kong"));
        gui.showPage(0);
        return true;
    }
}
