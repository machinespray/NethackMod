package com.machinespray.ROYAL.items.potion;

import com.machinespray.ROYAL.Main;
import com.machinespray.ROYAL.Values;
import com.machinespray.ROYAL.entity.EntityPotionRoyal;
import com.machinespray.ROYAL.polymorph.PolyBlockConstants;
import com.machinespray.ROYAL.polymorph.PolyEntityConstants;
import com.machinespray.ROYAL.polymorph.PolyPlayerData;
import com.machinespray.ROYAL.render.RenderGUIEvent;
import com.machinespray.ROYAL.sync.MessageSendPolyStatus;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.List;

public class PotionPolymorph extends PotionBase {

    public PotionPolymorph() {
        super("polymorph");
    }


    @Override
    void doDrinkAction(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) entityLiving;
            if (worldIn.isRemote) {
                RenderGUIEvent.buffer.add("You feel dead inside!");
            } else {
                Main.WRAPPER_INSTANCE.sendToAll(new MessageSendPolyStatus((EntityPlayer) entityLiving, PolyPlayerData.SILVERFISH));
                PolyPlayerData.setPoly((EntityPlayer) entityLiving, PolyPlayerData.SILVERFISH);
            }
        }
    }

    @Override
    public void onHit(RayTraceResult result, World world, EntityPotionRoyal entity) {
        //Do Block Transformations
        BlockPos pos = result.getBlockPos();
        if (pos == null)
            pos = result.entityHit.getPosition();
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++)
                for (int z = -4; z <= 4; z++) {
                    BlockPos mypos = pos.add(x, y, z);
                    if (Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)) <= 3) {
                        Block b = world.getBlockState(mypos).getBlock();
                        Item I = ItemBlock.getItemFromBlock(b);
                        PolyBlockConstants p = PolyBlockConstants.getGroup(b);
                        if (p != null) {
                            int id = -1;
                            for (int i = 0; i < p.contains.size(); i++) {
                                if (I == p.contains.get(i))
                                    id = i;
                            }
                            int id2 = Values.random.nextInt(p.contains.size());
                            Item i = p.contains.get(id2);
                            while (!(p.priority.get(id) >= p.priority.get(id2))) {
                                id2 = Values.random.nextInt(p.contains.size());
                                i = p.contains.get(id2);
                            }
                            if (i instanceof ItemBlock) {
                                NonNullList<ItemStack> items = NonNullList.create();
                                i.getSubItems(i, i.getCreativeTab(), items);
                                world.setBlockState(mypos, ((ItemBlock) i).getBlock().getStateFromMeta(Values.random.nextInt(items.size())));
                            }

                        }
                    }
                }


        }
        //Do Entity Transformations
        AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().expand(4.0D, 2.0D, 4.0D);
        List<EntityLivingBase> list = world.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb);
        if (!list.isEmpty()) {
            for (EntityLivingBase e : list) {
                if (e.canBeHitWithPotion()) {
                    PolyEntityConstants c = PolyEntityConstants.getGroup(e);
                    if (c != null && e.isEntityAlive()) {
                        Class<? extends EntityLivingBase> E = c.contains.get(Values.random.nextInt(c.contains.size()));
                        try {
                            EntityLivingBase newEntity = E.getConstructor(World.class).newInstance(world);
                            newEntity.setLocationAndAngles(e.posX, e.posY, e.posZ, e.rotationYaw, e.rotationPitch);
                            newEntity.setRotationYawHead(e.getRotationYawHead());
                            world.removeEntity(e);
                            world.spawnEntity(newEntity);
                            world.updateEntity(newEntity);
                        } catch (Exception Ex) {
                            Ex.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    @Override
    public String getUse() {
        return "polymorph";
    }

    public boolean hasUse() {
        return true;
    }
}
