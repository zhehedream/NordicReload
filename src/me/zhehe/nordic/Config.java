/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zhehe.nordic;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author Zhehe
 */
public class Config {
    private static class ConfigFile {
        public boolean GenerateWindMill = true;
    }
    
    public static ConfigFile config = new ConfigFile();
    
    public static boolean generateWindMill() {
        return config.GenerateWindMill;
    }
    
    public static void reload(JavaPlugin plugin) {
        File dir = plugin.getDataFolder();
        if(!dir.exists()) dir.mkdir();
        
        File file = new File(plugin.getDataFolder().toString() + File.separator + "config.json");
        if(file.exists()) {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF8"))) {
                StringBuilder sb = new StringBuilder();
                String line = reader.readLine();
                while (line != null) {
                    sb.append(line);
                    line = reader.readLine();
                }
                config = (new Gson()).fromJson(sb.toString(), ConfigFile.class);
            } catch (Exception ex) {
                Bukkit.getLogger().log(Level.SEVERE, "[NordicReload] Error while loading config file.");
                return;
            }
        }
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(config);
        try(OutputStreamWriter oStreamWriter = new OutputStreamWriter(new FileOutputStream(file), "utf-8")) {
            oStreamWriter.append(json);
            oStreamWriter.close();
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "[NordicReload] Error while saving config file.");
        }
    }
}
