/*
 * Copyright 2012 s1mpl3x
 * 
 * This file is part of Nordic.
 * 
 * Nordic is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * Nordic is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Nordic If not, see <http://www.gnu.org/licenses/>.
 */
package eu.over9000.nordic.populators;

import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.BlockPopulator;

import java.util.Random;
import static org.bukkit.Bukkit.createBlockData;
import org.bukkit.block.Biome;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;

public class PopulatorLonggrass extends BlockPopulator {

	@Override
	public void populate(final World world, final Random random, final Chunk source) {
		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {
				final int chance = random.nextInt(100);
				if (chance < 33) {
					Block handle = world.getHighestBlockAt(x + source.getX() * 16, z + source.getZ() * 16);
					if (handle.getRelative(BlockFace.DOWN).getType().equals(Material.GRASS_BLOCK)) {
                                                int r = random.nextInt(10);
                                                if(r > 8) {
                                                    handle.setBlockData(createBlockData(Material.FERN), false);
                                                } else if(r > 2) {
                                                    handle.setBlockData(createBlockData(Material.GRASS), false);
                                                } else {
                                                    BlockData data = createBlockData(Material.TALL_GRASS);
                                                    ((Bisected)data).setHalf(Bisected.Half.BOTTOM);
                                                    handle.setBlockData(data, false);

                                                    handle = handle.getRelative(BlockFace.UP);
                                                    data = createBlockData(Material.TALL_GRASS);
                                                    ((Bisected)data).setHalf(Bisected.Half.TOP);
                                                    handle.setBlockData(data, false);
                                                }
					} else if(handle.getRelative(BlockFace.DOWN).getType().equals(Material.WATER)) {
                                            handle = handle.getRelative(BlockFace.DOWN);
                                            while(handle.getType() == Material.WATER) {
                                                handle = handle.getRelative(BlockFace.DOWN);
                                            }
                                            handle = handle.getRelative(BlockFace.UP);
                                            if(world.getBiome(handle.getX(), handle.getZ()) == Biome.COLD_OCEAN && random.nextInt(10) < 2) {
                                                handle.setBlockData(createBlockData(Material.KELP_PLANT), false);
                                                continue;
                                            }
                                            if(random.nextInt(10) > 2) {
                                                handle.setBlockData(createBlockData(Material.SEAGRASS), false);
                                            } else {
                                                if(handle.getRelative(BlockFace.UP).getType() == Material.WATER) {
                                                    BlockData data = createBlockData(Material.TALL_SEAGRASS);
                                                    ((Bisected)data).setHalf(Bisected.Half.BOTTOM);
                                                    handle.setBlockData(data, false);

                                                    handle = handle.getRelative(BlockFace.UP);
                                                    data = createBlockData(Material.TALL_SEAGRASS);
                                                    ((Bisected)data).setHalf(Bisected.Half.TOP);
                                                    handle.setBlockData(data, false);
                                                }
                                            }
                                        }
				}
			}
		}
	}
}
