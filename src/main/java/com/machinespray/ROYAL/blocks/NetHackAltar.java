package com.machinespray.ROYAL.blocks;

import com.machinespray.ROYAL.Constants;
import com.machinespray.ROYAL.Main;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.IStateMapper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;

public class NetHackAltar extends Block implements Constants {
    public static final PropertyInteger VARIANT = PropertyInteger.create("variant", 0, 2);
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
        GameRegistry.register(this);
        item = new ItemBlock(this) {
            @Override
            public int getMetadata(int damage) {
                return damage;
            }
        };
        item.setHasSubtypes(true);
        item.setRegistryName(new ResourceLocation(Main.MODID, this
                .getUnlocalizedName()));
        GameRegistry.register(item);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
        list.add(new ItemStack(itemIn));
        list.add(new ItemStack(itemIn, 1, 1));
        list.add(new ItemStack(itemIn, 1, 2));
    }

    public int getType() {
        return type;
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(VARIANT);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(item, 1, state.getValue(VARIANT));
    }

    @SideOnly(Side.CLIENT)
    public void registerClient() {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
        ModelLoader.setCustomModelResourceLocation(item, 1,
                new ModelResourceLocation(item.getRegistryName() + "1", "inventory"));
        ModelLoader.setCustomModelResourceLocation(item, 2,
                new ModelResourceLocation(item.getRegistryName() + "2", "inventory"));
        ModelLoader.setCustomStateMapper(this, new IStateMapper() {
            @Override
            public Map<IBlockState, ModelResourceLocation> putStateModelLocations(Block blockIn) {
                Map<IBlockState, ModelResourceLocation> map = new HashMap<IBlockState, ModelResourceLocation>();
                map.put(blockIn.getStateFromMeta(0), new ModelResourceLocation(getRegistryName().toString()));
                map.put(blockIn.getStateFromMeta(1), new ModelResourceLocation(getRegistryName().toString() + "1"));
                map.put(blockIn.getStateFromMeta(2), new ModelResourceLocation(getRegistryName().toString() + "2"));
                return map;
            }
        });
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(VARIANT, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(VARIANT);
    }

    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{VARIANT});
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos,
                                    IBlockState state, EntityPlayer playerIn, EnumHand hand,
                                    EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (worldIn.isRemote) {
            for (int i = 0; i < 3; i++)
                worldIn.spawnParticle(EnumParticleTypes.CRIT_MAGIC, pos.getX() + Main.random.nextDouble(), pos.getY() + 1 + Main.random.nextDouble(), pos.getZ() + Main.random.nextDouble(), 0, 0.2, 0, 0);
            playerIn.playSound(SoundEvents.ITEM_TOTEM_USE, 0.2f, 2);
        }

        return true;
    }

}
