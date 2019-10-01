/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.over9000.nordic.populators;

import eu.over9000.nordic.Nordic;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import ops.zhehe.Util.NordicJsonSchematic;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.generator.BlockPopulator;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 *
 * @author Zhehe
 */
public class PopulatorBoat extends BlockPopulator {
    private static NordicJsonSchematic boat = null;
    private final Nordic plugin;
    private static List<Material> items;
    static {
        items = new ArrayList<>();
        items.add(Material.OAK_SAPLING);
        items.add(Material.DARK_OAK_SAPLING);
        items.add(Material.BIRCH_SAPLING);
        items.add(Material.JUNGLE_SAPLING);
        items.add(Material.ACACIA_SAPLING);
        items.add(Material.BEETROOT_SEEDS);
        items.add(Material.MELON_SEEDS);
        items.add(Material.PUMPKIN_SEEDS);
        items.add(Material.BAMBOO);
        items.add(Material.CACTUS);
        items.add(Material.VINE);
        items.add(Material.SUGAR_CANE);
    }
    public PopulatorBoat(Nordic plugin) {
        this.plugin = plugin;
        if(boat == null) {
            boat = new NordicJsonSchematic(plugin.getResource("ship.json"));
        }
    }
    @Override
    public void populate(final World world, final Random random, final Chunk source) {
        if(random.nextInt(300) > 0) return;
        int rx = random.nextInt(16) + 16 * source.getX();
        int rz = random.nextInt(16) + 16 * source.getZ();
        
        if(world.getBiome(rx, rz) != Biome.COLD_OCEAN) return;
        Block chest = boat.place(world, source);
        if(chest != null) {
            InventoryHolder ih = (InventoryHolder) chest.getState();
            Inventory inv = ih.getInventory();
            for(Material mat : items) {
                if(random.nextInt(4) <= 1) {
                    int amount = 1 + random.nextInt(5);
                    ItemStack item = new ItemStack(mat, amount);
                    int slot = random.nextInt(27);
                    inv.setItem(slot, item);
                }
            }
        }
    }
}
