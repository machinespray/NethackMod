package com.machinespray.ROYAL.polymorph;

import com.machinespray.ROYAL.Main;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import scala.Int;

import java.util.ArrayList;
import java.util.List;

public enum PolyBlockConstants {
    BLOCK_SOFT(Blocks.GRASS, 0, Blocks.DIRT, 0, Blocks.SAND, 0, Blocks.GRAVEL, 0, Blocks.CLAY, 0),
    BLOCK_STONE(Blocks.STONE, 0, Blocks.COBBLESTONE, 0, Blocks.SANDSTONE, 0, Blocks.MOSSY_COBBLESTONE, 0),
    BLOCK_PLANT_GROUP_1(Blocks.RED_MUSHROOM, 0, Blocks.BROWN_MUSHROOM, 0, Blocks.YELLOW_FLOWER, 0, Blocks.RED_FLOWER, 0),
    BLOCK_PLANT_GROUP_2(Blocks.DEADBUSH, 0, Blocks.TALLGRASS, 0),
    BLOCK_WOOD(Blocks.LOG, 0, Blocks.LOG2, 0),
    BLOCK_LEAVES(Blocks.LEAVES, 0, Blocks.LEAVES2, 0),
    BLOCK_WOOL(Blocks.WOOL, 0),
    BLOCK_PLANK(Blocks.PLANKS, 0),
    BLOCK_SAPLING(Blocks.SAPLING, 0),
    BLOCK_ORE(Blocks.STONE, 0, Blocks.COAL_ORE, 1, Blocks.GOLD_ORE, 2, Blocks.LAPIS_ORE, 2, Blocks.REDSTONE_ORE, 2, Blocks.IRON_ORE, 3, Blocks.DIAMOND_ORE, 4),
    BLOCK_GLASS(Blocks.GLASS, 0, Blocks.STAINED_GLASS, 0),
    BLOCK_DROPPERS(Blocks.COBBLESTONE, 0, Blocks.DISPENSER, 2, Blocks.DROPPER, 1),
    BLOCK_MUSIC(Blocks.JUKEBOX, 2, Blocks.NOTEBLOCK, 1, Blocks.PLANKS, 0),
    BLOCK_TILL(Blocks.FARMLAND, 0, Blocks.GRASS_PATH, 0),
    BLOCK_SLAB(Blocks.STONE_SLAB, 0, Blocks.STONE_SLAB2, 0),
    BLOCK_DOUBLE_SLAB(Blocks.DOUBLE_STONE_SLAB, 0, Blocks.DOUBLE_STONE_SLAB2, 0),
    BLOCK_BRICK(Blocks.BRICK_BLOCK, 1, Blocks.CLAY, 0);
    public ArrayList<Item> contains = new ArrayList<Item>();
    public ArrayList<Integer> priority = new ArrayList<Integer>();

    PolyBlockConstants(Object... args) {
        for (int i = 0; i < args.length; i++) {
            if (i % 2 == 0)
                if (args[i] instanceof Item) {
                    contains.add((Item) args[i]);
                    priority.add((Integer) args[i + 1]);
                } else if (args[i] instanceof Block) {
                    contains.add(ItemBlock.getItemFromBlock((Block) args[i]));
                    priority.add((Integer) args[i + 1]);
                } else {
                    new Exception("PolyConstant constructor expected a Block or Item instance, but got neither").printStackTrace();
                }

        }
    }

    public static PolyBlockConstants getGroup(Object o) {
        Item i = null;
        if (o instanceof Item)
            i = (Item) o;
        if (o instanceof Block)
            i = ItemBlock.getItemFromBlock((Block) o);
        if (i == null) {
            new Exception("PolyConstant constructor expected a Block or Item instance, but got neither").printStackTrace();
            return null;
        }
        for (PolyBlockConstants c : PolyBlockConstants.values())
            for (Item I : c.contains)
                if (i.equals(I))
                    return c;
        return null;
    }

}
