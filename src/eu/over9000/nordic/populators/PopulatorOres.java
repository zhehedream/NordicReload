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
import org.bukkit.generator.BlockPopulator;

import java.util.Random;

/**
 * Populates the world with ores.
 *
 * @author Nightgunner5
 * @author Markus Persson
 *         modified by simplex
 */
public class PopulatorOres extends BlockPopulator {
	private static final int[] iterations = new int[]{10, 16, 20, 20, 2, 8, 1, 1, 1};
	private static final int[] amount = new int[]{32, 32, 16, 8, 8, 7, 7, 6};
	private static final Material[] type = new Material[]{Material.GRAVEL, Material.SAND, Material.COAL_ORE, Material.IRON_ORE, Material.GOLD_ORE, Material.REDSTONE_ORE, Material.DIAMOND_ORE, Material.LAPIS_ORE};
	private static final int[] maxHeight = new int[]{128, 45, 128, 128, 32, 32, 32, 32, 16, 16, 32};

	/**
	 * @see org.bukkit.generator.BlockPopulator#populate(org.bukkit.World, java.util.Random, org.bukkit.Chunk)
	 */
	@Override
	public void populate(final World world, final Random random, final Chunk source) {
		for (int i = 0; i < type.length; i++) {
			for (int j = 0; j < iterations[i]; j++) {
				internal(source, random, random.nextInt(16), random.nextInt(maxHeight[i]), random.nextInt(16), amount[i], type[i]);
			}
		}
	}

	private static void internal(final Chunk source, final Random random, final int originX, final int originY, final int originZ, final int amount, final Material type) {
		for (int i = 0; i < amount; i++) {
			int x = originX + random.nextInt(amount / 2) - amount / 4;
			final int y = originY + random.nextInt(amount / 4) - amount / 8;
			int z = originZ + random.nextInt(amount / 2) - amount / 4;
			x &= 0xf;
			z &= 0xf;
			if (y > 127 || y < 0) {
				continue;
			}
			final Block block = source.getBlock(x, y, z);
			if (block.getType() == Material.STONE) {
				block.setType(type, false);
			}
		}
	}
}
