package com.machinespray.ROYAL.items.scrolls;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.items.RoyalItems;
import com.machinespray.ROYAL.items.randomized.DefaultRandomizedClass;
import com.machinespray.ROYAL.items.randomized.RandomObjectFactory;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScroll extends DefaultRandomizedClass {

    public ItemScroll() {
        super("SCROLL", scrollNames, ScrollAction.values(), new RandomObjectFactory() {
            @Override
            public DefaultRandomizedClass create(String s, DefaultRandomizedClass parent) {
                return new ItemScroll(s, parent);
            }
        });
        this.setCreativeTab(Main.royalTab);
    }

    public ItemScroll(String name, DefaultRandomizedClass parent) {
        super(name, parent);
    }

    @Override
    public String[] initNames(String[] names) {
        String[] newNames = new String[names.length];
        for (int i = 0; i < newNames.length; i++) {
            newNames[i] = names[i].replace(" ", "_");
        }
        return newNames;
    }


    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (hasUse() && playerIn.isSneaking())
            ((ScrollAction) getActualUse()).onItemRightClick(worldIn, playerIn, handIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerClient() {
        for (Item i : items)
            ModelLoader.setCustomModelResourceLocation(i, 0, new ModelResourceLocation(RoyalItems.base.getRegistryName(), "inventory"));
    }

}
