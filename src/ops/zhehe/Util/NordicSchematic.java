/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ops.zhehe.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import org.bukkit.Bukkit;
import static org.bukkit.Bukkit.createBlockData;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;

/**
 *
 * @author zhiqiang.hao
 */
public class NordicSchematic {
    
    private static EntityType[] et = null;
    
    BlockData bd[][][];
    boolean isEntity[][][];
    List<EntityType> entity = new ArrayList<>();
    String name;
    public NordicSchematic(InputStream stream, String name, int ignore_layer) {
        this.name = name;
        
        if(et == null) {
            et = new EntityType[1];
            et[0] = EntityType.VILLAGER;
        }
        
        try {
            load(stream,ignore_layer);
        } catch (IOException ex) {
            bd = null;
        }
    }
    
    private void load(InputStream stream, int ignore_layer) throws IOException {
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        String line;
        List<List<List<String>>> res = new ArrayList<>();
        List<List<String>> sub = new ArrayList<>();
        
        while( (line=reader.readLine())!=null) {
            line = line.trim();
            if(line.length() == 0) continue;
            if(line.equals("====")) {
                res.add(sub);
                sub = new ArrayList<>();
            } else {
                String[] array = line.split(" ");
                List<String> ssub = new ArrayList<>(array.length);
                ssub.addAll(Arrays.asList(array));
                sub.add(ssub);
            }
        }
        
        reader.close();
        isr.close();
        stream.close();
        
        int sizey = res.size();
        if(sizey == 0) {
            bd = null;
            return;
        }
        int sizex = res.get(0).size();
        if(sizex == 0) {
            bd = null;
            return;
        }
        int sizez = res.get(0).get(0).size();
        if(sizez == 0) {
            bd = null;
            return;
        }
        bd = new BlockData[sizex][sizey][sizez];
        isEntity = new boolean[sizex][sizey][sizez];
        
        for(int y = 0; y < sizey; y++) {
            for(int x = 0; x < sizex; x++) {
                for(int z = 0; z < sizez; z++) {
                    String tmp = res.get(y).get(x).get(z);
                    isEntity[x][y][z] = false;
                    if(tmp.length() == 0) {
                        bd[x][y][z] = createBlockData(Material.AIR);
                        continue;
                    }
                    if(tmp.charAt(0) == '#') {
                        bd[x][y][z] = createBlockData(Material.AIR);
                        int num = Integer.parseInt(tmp.substring(1));
                        if(num >= et.length) {
                            continue;   
                        }
                        
                        isEntity[x][y][z] = true;
                        entity.add(et[num]);
                        
                    } else {
                        bd[x][y][z] = createBlockData(tmp);
                        if(y < ignore_layer && bd[x][y][z].getMaterial() == Material.AIR || bd[x][y][z].getMaterial() == Material.GRASS_BLOCK || bd[x][y][z].getMaterial() == Material.DIRT) bd[x][y][z] = null;
                    }
                }
            }
        }
    }
}
