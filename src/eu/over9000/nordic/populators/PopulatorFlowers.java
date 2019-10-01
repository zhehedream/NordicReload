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
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;

public class PopulatorFlowers extends BlockPopulator {

	@Override
	public void populate(final World world, final Random random, final Chunk source) {
		final int chance = random.nextInt(100);
		if (chance < 10) {
			final int flowercount = random.nextInt(3) + 2;
			final int type = random.nextInt(270);
                        final int choose = random.nextInt(2);
                        if(choose == 1) {
                            for (int t = 0; t <= flowercount; t++) {
                                    final int flower_x = 1 + random.nextInt(14);
                                    final int flower_z = 1 + random.nextInt(14);

                                    final Block handle = world.getBlockAt(flower_x + source.getX() * 16, world.getHighestBlockYAt(flower_x + source.getX() * 16, flower_z + source.getZ() * 16), flower_z + source.getZ() * 16);
                                    if (handle.getRelative(BlockFace.DOWN).getType().equals(Material.GRASS_BLOCK)) {
                                            if (type < 40) {
                                                handle.setType(Material.POPPY);
                                            } else if(type < 120) {
                                                handle.setType(Material.DANDELION);
                                            } else if(type < 130) {
                                                handle.setType(Material.BLUE_ORCHID);
                                            } else if(type < 140) {
                                                handle.setType(Material.ALLIUM);
                                            } else if(type < 150) {
                                                handle.setType(Material.AZURE_BLUET);
                                            } else if(type < 160) {
                                                handle.setType(Material.RED_TULIP);
                                            } else if(type < 170) {
                                                handle.setType(Material.ORANGE_TULIP);
                                            } else if(type < 180) {
                                                handle.setType(Material.WHITE_TULIP);
                                            } else if(type < 190) {
                                                handle.setType(Material.PINK_TULIP);
                                            } else if(type < 200) {
                                                handle.setType(Material.OXEYE_DAISY);
                                            } else if(type < 220) {
                                                handle.setType(Material.LILY_OF_THE_VALLEY);
                                            } else if(type < 240) {
                                                handle.setType(Material.CORNFLOWER);
                                            } else {
                                                handle.setType(Material.SWEET_BERRY_BUSH);
                                            }
                                    }
                            }
                        } else {
                            for (int t = 0; t <= flowercount; t++) {
                                    final int flower_x = random.nextInt(15);
                                    final int flower_z = random.nextInt(15);

                                    Block handle = world.getBlockAt(flower_x + source.getX() * 16, world.getHighestBlockYAt(flower_x + source.getX() * 16, flower_z + source.getZ() * 16), flower_z + source.getZ() * 16);
                                    if (handle.getRelative(BlockFace.DOWN).getType().equals(Material.GRASS_BLOCK)) {
                                        if(type < 50) {
                                            BlockData data = createBlockData(Material.SUNFLOWER);
                                            ((Bisected)data).setHalf(Bisected.Half.BOTTOM);
                                            handle.setBlockData(data, false);
                                            
                                            handle = handle.getRelative(BlockFace.UP);
                                            data = createBlockData(Material.SUNFLOWER);
                                            ((Bisected)data).setHalf(Bisected.Half.TOP);
                                            handle.setBlockData(data, false);
                                        } else if(type < 100) {
                                            BlockData data = createBlockData(Material.LILAC);
                                            ((Bisected)data).setHalf(Bisected.Half.BOTTOM);
                                            handle.setBlockData(data, false);
                                            
                                            handle = handle.getRelative(BlockFace.UP);
                                            data = createBlockData(Material.LILAC);
                                            ((Bisected)data).setHalf(Bisected.Half.TOP);
                                            handle.setBlockData(data, false);
                                        } else if(type < 150) {
                                            BlockData data = createBlockData(Material.LARGE_FERN);
                                            ((Bisected)data).setHalf(Bisected.Half.BOTTOM);
                                            handle.setBlockData(data, false);
                                            
                                            handle = handle.getRelative(BlockFace.UP);
                                            data = createBlockData(Material.LARGE_FERN);
                                            ((Bisected)data).setHalf(Bisected.Half.TOP);
                                            handle.setBlockData(data, false);
                                        } else if(type < 200) {
                                            BlockData data = createBlockData(Material.ROSE_BUSH);
                                            ((Bisected)data).setHalf(Bisected.Half.BOTTOM);
                                            handle.setBlockData(data, false);
                                            
                                            handle = handle.getRelative(BlockFace.UP);
                                            data = createBlockData(Material.ROSE_BUSH);
                                            ((Bisected)data).setHalf(Bisected.Half.TOP);
                                            handle.setBlockData(data, false);
                                        } else {
                                            BlockData data = createBlockData(Material.PEONY);
                                            ((Bisected)data).setHalf(Bisected.Half.BOTTOM);
                                            handle.setBlockData(data, false);
                                            
                                            handle = handle.getRelative(BlockFace.UP);
                                            data = createBlockData(Material.PEONY);
                                            ((Bisected)data).setHalf(Bisected.Half.TOP);
                                            handle.setBlockData(data, false);
                                        }
                                    }
                        }
		}
	}
}}
