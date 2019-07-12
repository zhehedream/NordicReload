/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ops.zhehe.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

/**
 *
 * @author zhiqiang.hao
 */
public class Tools {
    
    private static class Node {
        public Location loc;
        public EntityType et;
        public Node(Location loc, EntityType et) {
            this.loc = loc;
            this.et = et;
        }
    }
    
    private static class LightNode {
        public Location loc;
        public BlockData bd;
        public LightNode(Location loc, BlockData bd) {
            this.loc = loc;
            this.bd = bd;
        }
    }
    
    public static void placeSchematic(NordicSchematic schematic, World world, Random random, int x, int y, int z) {
        if(schematic.bd == null) return;
        
        int sizex = schematic.bd.length;
        int sizey = schematic.bd[0].length;
        int sizez = schematic.bd[0][0].length;
        
        x = x - sizex / 2;
        z = z - sizez / 2;
        
        int entity_count = 0;
        List<Node> list = new ArrayList<>();
        List<LightNode> light = new ArrayList<>();
        
        for(int yy = 0; yy < sizey; yy++) {
            for(int xx = 0; xx < sizex; xx++) {
                for(int zz = 0; zz < sizez; zz++) {
                    if(schematic.isEntity[xx][yy][zz]) {
                        world.getBlockAt(x + xx, y + yy, z + zz).setType(Material.AIR, true);
                        if(entity_count >= schematic.entity.size()) continue;
                        
                        EntityType et = schematic.entity.get(entity_count);
                        entity_count++;
                        list.add(new Node(new Location(world, x + xx, y + yy, z + zz), et));
                                
                        /*Entity e = world.spawnEntity(new Location(world, x + xx, y + yy, z + zz), et);
                        Bukkit.getLogger().log(Level.INFO, (new Location(world, xx, yy, zz)).toString());
                        if(et == EntityType.VILLAGER) {
                            Villager v = (Villager) e;
                            Villager.Profession[] array = Villager.Profession.values();
                            v.setProfession(array[random.nextInt(array.length)]);
                        }*/
                        
                        continue;
                    }
                    if(schematic.bd[xx][yy][zz] == null) continue;
                    world.getBlockAt(x + xx, y + yy, z + zz).setBlockData(schematic.bd[xx][yy][zz],true);
                    Material tmp = schematic.bd[xx][yy][zz].getMaterial();
                    if(tmp == Material.WALL_TORCH || tmp == Material.TORCH || tmp == Material.SEA_LANTERN || tmp == Material.GLOWSTONE || tmp == Material.LAVA || tmp == Material.REDSTONE_LAMP) {
                        light.add(new LightNode(new Location(world, x + xx, y + yy, z + zz), schematic.bd[xx][yy][zz]));
                    }
                }
            }
        }
        
        for(LightNode node : light) {
            world.getBlockAt(node.loc).setBlockData(node.bd);
        }
        
        for(Node node : list) {
            Entity e = world.spawnEntity(node.loc, node.et);
        }
    }
}
