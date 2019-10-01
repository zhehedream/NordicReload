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
package eu.over9000.nordic;

import eu.over9000.nordic.noise.Voronoi;
import eu.over9000.nordic.noise.Voronoi.DistanceMetric;
import java.util.ArrayList;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.util.noise.SimplexOctaveGenerator;

import java.util.List;
import java.util.Random;
import me.zhehe.nordic.NordicPopulator;
import me.zhehe.nordic.WorldGenBaseOld;
import me.zhehe.nordic.WorldGenCavesOld;

/**
 * The ChunkGenerator of this Plugin
 *
 * @author simplex
 */
public class NordicChunkGenerator extends ChunkGenerator {

	private SimplexOctaveGenerator genHighland;
	private SimplexOctaveGenerator genBase1;
	private SimplexOctaveGenerator genBase2;
	private SimplexOctaveGenerator genHills;
	private SimplexOctaveGenerator genGround;

	private Voronoi voronoiGenBase1;
	private Voronoi voronoiGenBase2;
	private Voronoi voronoiGenMountains;

	private final List<BlockPopulator> populators;

	private long usedSeed;

	/**
	 * Default Constructor.
	 */
	public NordicChunkGenerator(final List<BlockPopulator> populators) {

		this.usedSeed = 1337L;
		this.populators = populators;
//                this.populators = new ArrayList<>();
//                this.populators.add(new NordicPopulator());

		changeSeed(usedSeed);
	}

	/**
	 * Sets the Material at the given Location
	 */
	private static void setMaterialAt(final ChunkData chunk_data, final int x, final int y, final int z, final Material material) {
                chunk_data.setBlock(x, y, z, material);
	}

	private static Material getMaterialAt(final ChunkData chunk_data, final int x, final int y, final int z) {
                return chunk_data.getType(x, y, z);
	}

	@Override
	//public byte[][] generateBlockSections(final World world, final Random random, final int x_chunk, final int z_chunk, final BiomeGrid biomes) {
        public ChunkData generateChunkData(World world, Random random, int chunkx, int chunkz, BiomeGrid biomes) {
		checkSeed(world.getSeed());

		final ChunkData result = createChunkData(world);

		int currheight;

		for (int x = 0; x < 16; x++) {
			for (int z = 0; z < 16; z++) {

				// ############################## Build the Heightmap
				// ##############################

				// Min-Underground height
				currheight = 29;

				// Base-Run 1
				currheight = Math.max(currheight, genBase(x, z, chunkx, chunkz, genBase1, voronoiGenBase1));

				// Base-Run 2
				currheight = Math.max(currheight, genBase(x, z, chunkx, chunkz, genBase2, voronoiGenBase2));

				// Underwater ground
				currheight = Math.max(currheight, genGround(x, z, chunkx, chunkz, genGround));

				// Some shiny hills
				currheight = Math.max(currheight, genHills(x, z, chunkx, chunkz, currheight, genHills));

				// Some mountains
				currheight = Math.max(currheight, genMountains(x, z, chunkx, chunkz, currheight, voronoiGenMountains));

				// Some highlands
				currheight = Math.max(currheight, genHighlands(x, z, chunkx, chunkz, currheight, genHighland));

				// ############################## Heightmap end
				// ##############################

				// ############################## Build the chunk
				// ##############################

				// Apply Heightmap
				applyHeightMap(x, z, result, currheight);

				// Build bedrock floor
				genFloor(x, z, result);

				// Add a layer of grass and dirt & blank mountains
				genTopLayer(x, z, result, currheight);
                                
                                biomes.setBiome(x, z, Biome.FOREST);

				// Put the water in
				genWater(biomes, x, z, result);

				// ############################## Build the chunk end
				// ##############################

				// ############################## Set the biome
				// ##############################

				

				// ############################## Set the biome end
				// ##############################
			}
		}
                generateCaves(world, chunkx, chunkz, result);
//                result.setBlock(1, 0, 1, Material.GLASS);
		return result;
	}
        
        private WorldGenBaseOld caves = null;
        
        public void generateCaves(World world, int x, int z, ChunkGenerator.ChunkData data) {
            if(caves == null) caves = new WorldGenCavesOld();
            caves.generate(world, x, z, data);
        }

	/**
	 * Writes the Value from the heightmap to the Chunk byte array
	 */
	private void applyHeightMap(final int x, final int z, final ChunkData chunk_data, final int currheight) {
		for (int y = 0; y <= currheight; y++) {
			setMaterialAt(chunk_data, x, y, z, Material.STONE);
		}
	}

	/**
	 * Generates the bedrock floor
	 */
	private void genFloor(final int x, final int z, final ChunkData chunk_data) {
		for (int y = 0; y < 5; y++) {
			if (y < 3) { // build solid bedrock floor
				setMaterialAt(chunk_data, x, y, z, Material.BEDROCK);
			} else { // build 2 block height mix floor
				final int rnd = new Random().nextInt(100);
				if (rnd < 40) {
					setMaterialAt(chunk_data, x, y, z, Material.BEDROCK);
				} else {
					setMaterialAt(chunk_data, x, y, z, Material.STONE);
				}
			}
		}
	}

	/**
	 * Generates some changes to the Continental Areas
	 *
	 * @return generated height
	 */
	private int genHighlands(final int x, final int z, final int xChunk, final int zChunk, final int current_height, final SimplexOctaveGenerator gen) {
		if (current_height < 50) {
			return 0;
		}
		final double noise = gen.noise((x + xChunk * 16) / 250.0f, (z + zChunk * 16) / 250.0f, 0.6, 0.6) * 25;
		return (int) (34 + noise);
	}

	/**
	 * Generates the Base-Hills
	 *
	 * @return generated height
	 */
	private int genHills(final int x, final int z, final int xChunk, final int zChunk, final int current_height, final SimplexOctaveGenerator gen) {
		final double noise = gen.noise((x + xChunk * 16) / 250.0f, (z + zChunk * 16) / 250.0f, 0.6, 0.6) * 10;
		return (int) (current_height - 2 + noise);
	}

	/**
	 * Generates the Ocean ground
	 */
	private int genGround(final int x, final int z, final int xChunk, final int zChunk, final SimplexOctaveGenerator gen) {
		gen.setScale(1 / 128.0);
		final double noise = gen.noise(x + xChunk * 16, z + zChunk * 16, 0.01, 0.5) * 20;
		return (int) (34 + noise);
	}

	/**
	 * Generates the higher Hills
	 */
	private int genMountains(final int x, final int z, final int xChunk, final int zChunk, final int current_height, final Voronoi noisegen) {
		final double noise = noisegen.get((x + xChunk * 16) / 250.0f, (z + zChunk * 16) / 250.0f) * 100;
		final int limit = (int) (current_height + noise);
		if (limit < 30) {
			return 0;
		}
		return limit;
	}

	/**
	 * Generates the Continental-base
	 */
	private int genBase(final int x, final int z, final int xChunk, final int zChunk, final SimplexOctaveGenerator gen, final Voronoi noisegen) {
		final double noise_raw1 = gen.noise((x + xChunk * 16) / 1200.0f, (z + zChunk * 16) / 1200.0f, 0.5, 0.5) * 600;
		final double noise_raw2 = noisegen.get((x + xChunk * 16) / 800.0f, (z + zChunk * 16) / 800.0f) * 500;
		final double noise = noise_raw1 * 0.5 + noise_raw2 * 0.5;
		double limit = 29 + noise;
		if (limit > 55) {
			limit = 55;
		}
		return (int) limit;
	}

	/**
	 * Puts a Dirt/Grass Layer over the Chunk
	 */
	private void genTopLayer(final int x, final int z, final ChunkData chunk_data, final int height) {
		boolean grass = true;
		if (height < 48) {
			grass = false;
		}
		final Random rnd = new Random();
		if (height > 80) {
			return;
		}
		if (height <= 77 || rnd.nextBoolean()) {
			final int soil_depth = rnd.nextInt(4);
			if (grass) {
				setMaterialAt(chunk_data, x, height, z, Material.GRASS_BLOCK);
			} else {
				setMaterialAt(chunk_data, x, height, z, Material.DIRT);
			}
			for (int y = height - 1; y >= height - soil_depth; y--) {
				setMaterialAt(chunk_data, x, y, z, Material.DIRT);
			}
		}
	}

	/**
	 * Fills the Oceans with water
	 */
	private void genWater(final BiomeGrid biomes, final int x, final int z, final ChunkData chunk_data) {
		int y = 48;
		while (y > 29) {
			if (getMaterialAt(chunk_data, x, y, z) == Material.AIR) {
				setMaterialAt(chunk_data, x, y, z, Material.WATER);
                                biomes.setBiome(x, z, Biome.COLD_OCEAN);
			} else break;
			y--;
		}
                setMaterialAt(chunk_data, x, y, z, Material.GRAVEL);
	}

	@Override
	public List<BlockPopulator> getDefaultPopulators(final World world) {
		return populators;
	}

	/**
	 * Sets the Noise generators to use the specified seed
	 */
	public void changeSeed(final long seed) {
		genHighland = new SimplexOctaveGenerator(new Random(seed), 16);
		genBase1 = new SimplexOctaveGenerator(new Random(seed), 16);
		genBase2 = new SimplexOctaveGenerator(new Random(seed), 16);
		genHills = new SimplexOctaveGenerator(new Random(seed), 4);
		genGround = new SimplexOctaveGenerator(new Random(seed), 16);

		voronoiGenBase1 = new Voronoi(64, true, seed, 16, DistanceMetric.Squared, 4);
		voronoiGenBase2 = new Voronoi(64, true, seed, 16, DistanceMetric.Quadratic, 4);
		voronoiGenMountains = new Voronoi(64, true, seed, 16, DistanceMetric.Squared, 4);
	}

	/**
	 * Checks if the Seed that is currently used by the Noise generators is the
	 * same as the given seed. If not {@link NordicChunkGenerator#changeSeed(long)} is called.
	 */
	private void checkSeed(final long worldSeed) {
		if (worldSeed != usedSeed) {
			changeSeed(worldSeed);
			usedSeed = worldSeed;
		}
	}

}
