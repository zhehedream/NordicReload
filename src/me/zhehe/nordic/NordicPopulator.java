/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.zhehe.nordic;

import eu.over9000.nordic.Nordic;
import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.generator.BlockPopulator;

/**
 *
 * @author Zhehe
 */
public class NordicPopulator extends BlockPopulator {
    @Override
    public void populate(final World world, final Random random, final Chunk source) {
        int chunkX = source.getX(), chunkZ = source.getZ();
        if(
                isValidChunk(world, chunkX - 1, chunkZ - 1) > 0 &&
                isValidChunk(world, chunkX - 1, chunkZ) > 0 &&
                isValidChunk(world, chunkX - 1, chunkZ + 1) > 0 &&
                isValidChunk(world, chunkX, chunkZ - 1) > 0 &&
                isValidChunk(world, chunkX, chunkZ + 1) > 0 &&
                isValidChunk(world, chunkX + 1, chunkZ - 1) > 0 &&
                isValidChunk(world, chunkX + 1, chunkZ) > 0 &&
                isValidChunk(world, chunkX + 1, chunkZ + 1) > 0
        )
            actualPopulate(world, random, source, true);
    }
    
    private void actualPopulate(final World world, final Random random, final Chunk source, final boolean spread) {
        for(BlockPopulator bp : Nordic.populators) {
            bp.populate(world, random, source);
        }
        source.getBlock(1, 0, 1).setType(Material.BEDROCK);
        if(spread) {
            int chunkX = source.getX(), chunkZ = source.getZ();
            if(isValidChunk(world, chunkX - 1, chunkZ - 1) == 1) 
                actualPopulate(world, random, world.getChunkAt(chunkX - 1, chunkZ - 1), false);
            if(isValidChunk(world, chunkX - 1, chunkZ) == 1) 
                actualPopulate(world, random, world.getChunkAt(chunkX - 1, chunkZ), false);
            if(isValidChunk(world, chunkX - 1, chunkZ + 1) == 1) 
                actualPopulate(world, random, world.getChunkAt(chunkX - 1, chunkZ + 1), false);
            
            if(isValidChunk(world, chunkX, chunkZ - 1) == 1) 
                actualPopulate(world, random, world.getChunkAt(chunkX, chunkZ - 1), false);
            if(isValidChunk(world, chunkX, chunkZ + 1) == 1) 
                actualPopulate(world, random, world.getChunkAt(chunkX, chunkZ + 1), false);
            
            if(isValidChunk(world, chunkX + 1, chunkZ - 1) == 1) 
                actualPopulate(world, random, world.getChunkAt(chunkX + 1, chunkZ - 1), false);
            if(isValidChunk(world, chunkX + 1, chunkZ) == 1) 
                actualPopulate(world, random, world.getChunkAt(chunkX + 1, chunkZ), false);
            if(isValidChunk(world, chunkX + 1, chunkZ + 1) == 1) 
                actualPopulate(world, random, world.getChunkAt(chunkX + 1, chunkZ + 1), false);
        }
    }
    
    private int isValidChunk(final World world, final int chunkX, int chunkZ) {
        if(!world.isChunkGenerated(chunkX, chunkZ)) return 0;
        if(world.getChunkAt(chunkX, chunkZ).getBlock(1, 0, 1).getType() == Material.GLASS) return 1;
        return 2;
    }
}
