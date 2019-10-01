/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.over9000.nordic.populators;

import eu.over9000.nordic.Nordic;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import ops.zhehe.Util.NordicSchematic;
import ops.zhehe.Util.Tools;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author zhehe
 */
public class PopulatorWindMill extends BlockPopulator {
    
    private static NordicSchematic[] windmill = null;
    private Nordic plugin;
    
    private static class Node {
            public int x, z;
            public Node(int x, int z) {
                this.x = x;
                this.z = z;
            }
        }
        
        private final static int DISTANCE = 10;
        private final static int MAX_SIZE = 8;
        
        private final static Map<String, List<Node>> dict = new HashMap<>();
        
        private boolean isValid(World world, int chunkX, int chunkZ) {
            synchronized(dict) {
                String name = world.getName();
                if(dict.get(name) == null) {
                    List<Node> l = new ArrayList<>();
                    l.add(new Node(chunkX, chunkZ));
                    dict.put(name, l);
                    return true;
                }
                List<Node> list = dict.get(name);
                for(Node n : list) {
                    int dx = chunkX - n.x;
                    int dz = chunkZ - n.z;
                    dx = dx > 0 ? dx : -dx;
                    dz = dz > 0 ? dz : -dz;
                    int dis = dx + dz;
                    
                    if(dis < DISTANCE) return false;
                }
                list.add(new Node(chunkX, chunkZ));
                if(list.size() > MAX_SIZE) {
                    list.remove(0);
                }
                return true;
            }
        }
    
    public PopulatorWindMill(Nordic p) {
        plugin = p;
        if(windmill == null) {
            windmill = new NordicSchematic[8];
            windmill[0] = new NordicSchematic(plugin.getResource("schematics/windmill_0.txt"), "WindMill01", 3);
            windmill[1] = new NordicSchematic(plugin.getResource("schematics/windmill_90.txt"), "WindMill02", 3);
            windmill[2] = new NordicSchematic(plugin.getResource("schematics/windmill_180.txt"), "WindMill03", 3);
            windmill[3] = new NordicSchematic(plugin.getResource("schematics/windmill_270.txt"), "WindMill04", 3);
            windmill[4] = new NordicSchematic(plugin.getResource("schematics/windmill2_0.txt"), "WindMill05", 3);
            windmill[5] = new NordicSchematic(plugin.getResource("schematics/windmill2_90.txt"), "WindMill06", 3);
            windmill[6] = new NordicSchematic(plugin.getResource("schematics/windmill2_180.txt"), "WindMill07", 3);
            windmill[7] = new NordicSchematic(plugin.getResource("schematics/windmill2_270.txt"), "WindMill08", 3);
        }
    }
    
    @Override
    public void populate(final World world, final Random random, final Chunk source) {
        if(random.nextInt(2000) >= 10) return;
        if(!isValid(world, source.getX(), source.getZ())) return;
        BukkitRunnable run = new BukkitRunnable() {
            @Override
            public void run() {
                place(world, random, source);
                this.cancel();
            }
        };
        run.runTaskTimer(Nordic.instance, 1, 1);
    }
    
    private void place(final World world, final Random random, final Chunk source) {
        if (true) {
            int x = random.nextInt(15);
            int z = random.nextInt(15);
            int realx = source.getX() * 16 + x;
            int realz = source.getZ() * 16 + z;
            
            Block block = world.getHighestBlockAt(realx, realz).getRelative(BlockFace.DOWN);
            if(block.getType() != Material.GRASS_BLOCK) return;
            
            int i = random.nextInt(windmill.length);
            Tools.placeSchematic(windmill[i], world, random, realx, block.getY(), realz);
        }
    }
}
