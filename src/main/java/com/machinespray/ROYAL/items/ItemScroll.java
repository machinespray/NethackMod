package com.machinespray.ROYAL.items;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.RoyalItems;
import com.machinespray.ROYAL.randomized.scroll.ScrollActionGroup;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemScroll extends NetHackItem implements Constants {

    public ItemScroll(String unlocalizedName) {
        super(unlocalizedName);
        this.actionGroup = ScrollActionGroup.INSTANCE;
        this.setCreativeTab(Main.royalTab);
    }

    private ScrollActionGroup actionGroup() {
        return (ScrollActionGroup) actionGroup;
    }

    public static void initNames() {
        for (String s : scrollNames) {
            s = s.replace(" ", "_");
            RoyalItems.scrolls.add(new ItemScroll(s));

        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerClient() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(RoyalItems.base.getRegistryName(), "inventory"));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if (actionGroup.getAction(getUnlocalizedName()) != null)
            actionGroup().getAction(getUnlocalizedName()).onItemRightClick(worldIn, playerIn, handIn);
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    @Override
    public boolean hasUse() {
        return Main.proxy.getUse(this) != null;
    }

    @Override
    public String getUse() {
        return Main.proxy.getUse(this);
    }

    @Override
    public int getID() {
        for (int i = 0; i < scrollNames.length; i++) {
            String temp = getUnlocalizedName().split("\\.")[1].replace("_", " ");
            if (scrollNames[i].equals(temp))
                return i;
        }
        return super.getID();

    }

    @Override
    public String type() {
        return "scroll";
    }

}
