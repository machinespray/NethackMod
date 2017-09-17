package com.machinespray.ROYAL.altars;

import com.machinespray.ROYAL.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBrewingStand;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.tileentity.TileEntityBrewingStand;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.GameData;

import javax.annotation.Nullable;
import java.util.List;

public class NetHackAltar extends Block implements Constants {
    private ItemBlock item;
    private int type;

    public NetHackAltar(int type) {
        super(Material.IRON);
        this.setCreativeTab(Main.royalTab);
        switch (type) {
            case 0:
                setUnlocalizedName(LAWFUL + ALTAR);
                break;
            case 1:
                setUnlocalizedName(NEUTRAL + ALTAR);
                break;
            case 2:
                setUnlocalizedName(CHAOTIC + ALTAR);
                break;
        }
        this.setRegistryName(Main.MODID, getUnlocalizedName());
        this.type = type;
    }

    public void register() {
        GameData.register_impl(this);
        item = new ItemBlock(this) {
            @Override
            public void addInformation(ItemStack stack, @Nullable World worldIn,
                                       List<String> tooltip, ITooltipFlag advanced) {
                if (type != 1) {
                    tooltip.add("Place a Brewing Stand with Water Bottles in it above this altar for a chance to receive gifts upon sacrificing.");
                } else {
                    tooltip.add("Sacrifice gold at this altar for a chance to receive gifts upon sacrificing.");
                }
            }
        };
        item.setRegistryName(new ResourceLocation(Main.MODID, this
                .getUnlocalizedName()));
        GameData.register_impl(item);
    }

    public int getType() {
        return type;
    }

    @SideOnly(Side.CLIENT)
    public void registerClient() {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos,
                                    IBlockState state, EntityPlayer playerIn, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        /*if (worldIn.isRemote) {
            for (int i = 0; i < 3; i++)
                worldIn.spawnParticle(EnumParticleTypes.CRIT_MAGIC, pos.getX() + Main.random.nextDouble(), pos.getY() + 1 + Main.random.nextDouble(), pos.getZ() + Main.random.nextDouble(), 0, 0.2, 0, 0);
            playerIn.playSound(SoundEvents.ITEM_TOTEM_USE, 0.2f, 2);
        }
        if (!worldIn.isRemote)
            if (playerIn.getHeldItemMainhand().getItem()
                    .equals(Items.GOLD_INGOT)) {
                playerIn.getHeldItemMainhand().setCount(
                        playerIn.getHeldItemMainhand().getCount() - 1);
                playerIn.sendStatusMessage(new TextComponentString(Events.getGods(worldIn.getSeed(), Events.gods)[type] + " accepts your gift!"), true);
                //EntityLightningBolt lightning = new EntityLightningBolt(
                //worldIn, pos.getX(), pos.getY() + 1, pos.getZ(), true);
                //worldIn.addWeatherEffect(lightning);
                if (this.type != 1) {
                    IBlockState brew = worldIn.getBlockState(pos.add(0, 1, 0));
                    if (brew.getBlock() instanceof BlockBrewingStand) {
                        int i = 0;
                        if (brew.getValue(PropertyBool.create("has_bottle_0")))
                            i++;
                        if (brew.getValue(PropertyBool.create("has_bottle_1")))
                            i++;
                        if (brew.getValue(PropertyBool.create("has_bottle_2")))
                            i++;
                        TileEntityBrewingStand tile = ((TileEntityBrewingStand) worldIn
                                .getTileEntity(pos.add(0, 1, 0)));
                        tile.removeStackFromSlot(0);
                        tile.removeStackFromSlot(1);
                        tile.removeStackFromSlot(2);
                        worldIn.destroyBlock(pos.add(0, 1, 0), false);
                        for (int j = 0; j < i; j++) {
                            ItemStack stack = new ItemStack(Items.POTIONITEM, 1);
                            NBTTagCompound nbt = new NBTTagCompound();
                            nbt.setString("Potion", "minecraft:water");
                            nbt.setString(BUC, BLESSED);
                            if (type == 2)
                                if (Main.random.nextInt(2) == 0)
                                    nbt.setString(BUC, CURSED);
                            nbt.setBoolean(BUCI, true);
                            NBTTagCompound display = new NBTTagCompound();
                            NBTTagList lore = new NBTTagList();
                            lore.appendTag(new NBTTagString(nbt
                                    .getString(BUC)));
                            lore.appendTag(new NBTTagString(
                                    "Use \"#dip\" to bless/curse items"));
                            display.setTag("Lore", lore);
                            nbt.setTag("display", display);
                            stack.setTagCompound(nbt);
                            worldIn.spawnEntity(new EntityItem(worldIn, pos
                                    .getX(), pos.getY() + 1, pos.getZ(), stack));
                        }
                    }
                } else {
                    if (Main.random.nextInt(3) == 0) {
                        ItemStack stack = null;
                        if (Main.random.nextInt(2) == 0) {
                            stack = new ItemStack(
                                    RoyalItems.rings.get(Main.random
                                            .nextInt(RoyalItems.rings.size())));
                            while (!((NetHackItem) stack.getItem()).hasUse())
                                stack = new ItemStack(
                                        RoyalItems.rings.get(Main.random
                                                .nextInt(RoyalItems.rings
                                                        .size())));
                        } else {
                            stack = new ItemStack(
                                    RoyalItems.scrolls.get(Main.random
                                            .nextInt(RoyalItems.scrolls.size())));
                            while (!((NetHackItem) stack.getItem()).hasUse())
                                stack = new ItemStack(
                                        RoyalItems.scrolls.get(Main.random
                                                .nextInt(RoyalItems.scrolls
                                                        .size())));
                        }
                        NBTTagCompound nbt = new NBTTagCompound();
                        if (stack.getTagCompound() != null)
                            nbt = stack.getTagCompound();
                        switch (Main.random.nextInt(10)) {
                            case 8:
                                nbt.setString(BUC, BLESSED);
                                break;
                            case 9:
                                nbt.setString(BUC, CURSED);
                                break;
                            default:
                                nbt.setString(BUC, UNCURSED);
                        }
                        stack.setTagCompound(nbt);
                        worldIn.spawnEntity(new EntityItem(worldIn, pos.getX(),
                                pos.getY() + 1, pos.getZ(), stack));
                    }
                }
                    */
        return true;
    }

}
