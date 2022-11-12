package me.kqn.storespace.Config;

import me.kqn.storespace.StoreSpace;
import me.kqn.storespace.Utils.Msg;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DropConfig {
    private static List<String> lore;
    private static int drop_prob=50;
    private static int drop_percent=50;
    private static YamlConfiguration file;

    public static List<String> getLore() {
        return lore;
    }

    public static int getDrop_prob() {
        return drop_prob;
    }

    public static int getDrop_percent() {
        return drop_percent;
    }

    public static void read() {
        try {
            StoreSpace.plugin.saveResource("DropConfig.yml", false);
            file = YamlConfiguration.loadConfiguration(new File("plugins\\StoreSpace\\DropConfig.yml"));
            drop_prob=Integer.parseInt(file.getString("drop_prob"));
            drop_percent=Integer.parseInt(file.getString("drop_percent"));
            lore=file.getStringList("key_lore");
        } catch (Exception e) {
            Msg.Log("[StoreSpace]读取DropConfig.yml时出错，启用内置默认值");
            e.printStackTrace();
            drop_prob=50;
            drop_percent=50;
            lore=new ArrayList<>();
        }
    }
}
