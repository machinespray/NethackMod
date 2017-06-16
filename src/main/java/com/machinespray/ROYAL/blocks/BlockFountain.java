package com.machinespray.ROYAL.blocks;

import com.machinespray.ROYAL.entity.TileFountain;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockFountain extends Block implements ITileEntityProvider {

    public BlockFountain() {
        super(Material.ROCK);
        setUnlocalizedName("fountain");
        setRegistryName("royal", "fountain");
    }

    private TileFountain getFountainAt(World world, BlockPos pos) {
        return (TileFountain) world.getTileEntity(pos);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TileFountain();
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return true;
    }
}
