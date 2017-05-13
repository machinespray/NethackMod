package com.machinespray.ROYAL.polymorph;

import com.machinespray.ROYAL.Main;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import java.util.ArrayList;
import java.util.List;

public enum PolyConstants {
    BLOCK_SOFT(Blocks.GRASS, Blocks.DIRT, Blocks.SAND, Blocks.GRAVEL, Blocks.CLAY, Blocks.SAND.getStateFromMeta(1).getBlock());
    private ArrayList<Item> contains = new ArrayList<Item>();

    PolyConstants(Object... args) {
        for (Object i : args) {
            if (i instanceof Item) {
                contains.add((Item) i);
            } else if (i instanceof Block) {
                contains.add(ItemBlock.getItemFromBlock((Block) i));
            } else {
                new Exception("PolyConstant constructor expected a Block or Item instance, but got neither").printStackTrace();
            }

        }
    }

    //public static PolyConstants getGroup(){}

}
