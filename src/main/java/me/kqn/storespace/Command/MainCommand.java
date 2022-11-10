package me.kqn.storespace.Command;


import me.kqn.storespace.Gui.Gui;
import me.kqn.storespace.StoreSpace;
import me.kqn.storespace.Utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainCommand implements CommandExecutor , TabCompleter {
    String prefix="[StoreSpace]";
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length>1&&args[0].equalsIgnoreCase("open")){
            if((sender instanceof Player && sender.isOp())||(sender instanceof ConsoleCommandSender)){
                Player player=Bukkit.getPlayerExact(args[1]);
                if(player==null){
                    Msg.msg(sender,prefix+"该玩家不存在");
                }
                else {
                    Gui gui=new Gui(player);
                    gui.showPage(0);
                }
            }
        }
        if(args.length==1&&args[0].equalsIgnoreCase("reload")){
            if((sender instanceof Player && sender.isOp())||(sender instanceof ConsoleCommandSender)){
                StoreSpace.plugin.loadConfig();
                StoreSpace.plugin.initIntegretion();
                Msg.msg(sender,prefix+"重新载入了配置文件和挂钩");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if((sender instanceof Player && sender.isOp())){
            if(args.length<2){
                return Arrays.asList("open","reload");
            }
            else{
                ArrayList<String> playername=new ArrayList<>();
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    playername.add(onlinePlayer.getName());
                }
                return playername;
            }
        }
        return null;
    }
}
