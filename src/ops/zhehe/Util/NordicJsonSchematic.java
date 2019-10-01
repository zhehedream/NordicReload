/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ops.zhehe.Util;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;

/**
 *
 * @author Zhehe
 */
public class NordicJsonSchematic {
    public static class IBlock {
        int[] pos = new int[3];
        int state = 0;
    }
    public IBlock[] blocks = new IBlock[0];
    public int[] size = new int[3];
    public String author = "";
    public String[] palette = new String[0];
    private transient BlockData[] bd = new BlockData[0];
    
    private NordicJsonSchematic() {
        
    }
    
    public NordicJsonSchematic(InputStream stream) {
        try {
            load(stream);
        } catch (IOException ex) {
            this.blocks = null;
        }
    }
    
    public Block place(World world, Chunk chunk) {
        if(blocks == null) return null;
        int x = 2;
        int z = 2;
        int y = 48;
        if(x + size[0] >= 15 && z + size[2] >= 15 && y + size[1] >= 100) return null;
        Block res = null;
        for(IBlock block : blocks) {
            int bx = x + block.pos[0];
            int by = y + block.pos[1];
            int bz = z + block.pos[2];
            chunk.getBlock(bx, by, bz).setBlockData(bd[block.state], true);
            if(bd[block.state].getMaterial() == Material.CHEST) res = chunk.getBlock(bx, by, bz);
        }
        return res;
    }
    
    private void load(InputStream stream) throws IOException {
        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        String line;
        StringBuilder json = new StringBuilder();
        while( (line=reader.readLine())!=null) {
            json.append(line);
        }
        
        NordicJsonSchematic tmp = (new Gson()).fromJson(json.toString(), NordicJsonSchematic.class);
        this.blocks = tmp.blocks;
        this.author = tmp.author;
        this.size = tmp.size;
        this.palette = tmp.palette;
        
        this.bd = new BlockData[palette.length];
        for(int i = 0; i < bd.length; i++) {
            bd[i] = Bukkit.createBlockData(palette[i]);
        }
    }
}
