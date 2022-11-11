package me.kqn.storespace.Data.DataSource;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.kqn.storespace.Data.PlayerData;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class JsonFile implements DataSource {
    String folder="plugins\\StoreSpace\\data";

    @Override
    public PlayerData readToPlayerData(UUID uuid) {
        File fileFolder=new File(folder);
        if(!fileFolder.exists())fileFolder.mkdir();
        File[] datafiles=fileFolder.listFiles();
        if(datafiles==null)return new PlayerData();
        for (File datafile : datafiles) {
            if(datafile.getName().replace(".json","").equals(uuid.toString())){
                try {
                    FileInputStream fis=new FileInputStream(datafile);
                    byte[] bytes=new byte[fis.available()];
                    fis.read(bytes);
                    String json;
                    json=DataSource.Gunzip(bytes);
                    System.out.println("读入："+json);
                    Gson gson=new Gson();
                    JsonArray jsondata= gson.fromJson(json,JsonArray.class);
                    fis.close();
                    return PlayerData.fromJson(jsondata);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    public void write(PlayerData playerData,UUID uuid) {
        File filefolder=new File(folder);
        if(!filefolder.exists())filefolder.mkdir();
        File datafile=new File(folder+"\\"+uuid.toString()+".json");
        if(!datafile.exists()) {
            try {
                datafile.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try {
            String json=PlayerData.toJson(playerData).toString();
            System.out.println("输出："+json);
            byte[] bytes=DataSource.Gzip(json);
            FileOutputStream fos=new FileOutputStream(datafile);
            fos.write(bytes);
            fos.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
