/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.over9000.nordic.populators;

import eu.over9000.nordic.Nordic;
import java.util.Random;
import ops.zhehe.Util.NordicSchematic;
import ops.zhehe.Util.Tools;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

/**
 *
 * @author zhiqiang.hao
 */
public class PopulatorWindMill extends BlockPopulator {
    
    private static NordicSchematic[] windmill = null;
    private Nordic plugin;
    
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
        if (random.nextInt(2000) < 10) {
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
