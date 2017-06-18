package com.machinespray.ROYAL.items.scrolls;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.items.NetHackItem;
import com.machinespray.ROYAL.items.RoyalItems;
import com.machinespray.ROYAL.items.randomized.IRandomizedClass;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScroll extends NetHackItem implements Constants, IRandomizedClass {

    public ItemScroll(String unlocalizedName) {
        super(unlocalizedName);
        this.setCreativeTab(Main.royalTab);
    }

    public static void initNames() {
        for (String s : scrollNames) {
            s = s.replace(" ", "_");
            //RoyalItems.scrolls.add(new ItemScroll(s));

        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerClient() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(RoyalItems.base.getRegistryName(), "inventory"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (ScrollAction.getAction(getUnlocalizedName()) != null)
            ScrollAction.getAction(getUnlocalizedName()).onItemRightClick(worldIn, playerIn, handIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean hasUse() {
    //    return Main.proxy.getScrollUse(this) != null;
        return false;
    }

    @Override
    public String type() {
        return null;
    }

    @Override
    public String getUse() {
     //   return Main.proxy.getScrollUse(this);
        return  null;
    }

}
